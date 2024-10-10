package com.csse3200.game.components.player;

import java.util.Map;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.components.items.PlateComponent;
import com.csse3200.game.components.station.FireExtinguisherHandlerComponent;
import com.csse3200.game.components.TooltipsDisplay;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.InteractableService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.SensorComponent;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  private static final Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second
  private static final float MIN_X_POSITION = 3.52f; // Minimum X position - where the separation border is at
  private static final float MAX_X_POSITION = 15.1f; // Maximum X position - where the right border is at
  private PhysicsComponent physicsComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean moving = false;
  private SensorComponent interactionSensor;
  private InventoryComponent playerInventory;
  private InventoryDisplay displayInventory;

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    interactionSensor = entity.getComponent(SensorComponent.class);
    playerInventory = entity.getComponent(InventoryComponent.class);
    displayInventory = entity.getComponent(InventoryDisplay.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("interact", this::interact);
  }

  @Override
  public void update() {
    Body body = physicsComponent.getBody();
    Vector2 position = body.getPosition();

    if (moving) {
      // Stop if it's at min x position or max x position
      if (position.x < MIN_X_POSITION) {
        position.x = MIN_X_POSITION;
        body.setTransform(MIN_X_POSITION, position.y, body.getAngle());
        stopWalking();
      } else if (position.x > MAX_X_POSITION) {
        position.x = MAX_X_POSITION;
        body.setTransform(MAX_X_POSITION, position.y, body.getAngle());
        stopWalking();
      } else {
        updateSpeed();
      }
    }
    updateInteraction();
  }

  /**
   * Updates the player's interaction with nearby objects. This method checks for the closest
   * interactable object within the sensor's range. If an interactable object is found, it triggers
   * the display of a tooltip with interaction details. If no interactable object is nearby, it hides
   * the tooltip.
   * */
  private void updateInteraction() {
    interactionSensor.update();
    Fixture interactable = interactionSensor.getClosestFixture();
    if (interactable != null) {
      Vector2 objectPosition = interactable.getBody().getPosition();  // Get object position
      String interactionKey = "Press E";
      String itemName = "to interact";
      // Create a TooltipInfo object with the text and position
      TooltipsDisplay.TooltipInfo tooltipInfo = new TooltipsDisplay.TooltipInfo(interactionKey + " " + itemName, objectPosition);

      // Trigger the event with the TooltipInfo object
      entity.getEvents().trigger("showTooltip", tooltipInfo);

    } else {
      entity.getEvents().trigger("hideTooltip");
    }
  }

  private void updateSpeed() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    Vector2 desiredVelocity = walkDirection.cpy().scl(MAX_SPEED);

    if (body.getPosition().x < MIN_X_POSITION || body.getPosition().x > MAX_X_POSITION) {
      // Do not apply any movement if out of bounds
      return;
    }

    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  /**
   * Triggers an interaction event. It holds the logic in how to interact with a given station
   */
  void interact(String type) {
    Map<Entity, Vector2> interactables = InteractableService.getInteractables();

    Entity closestEntity = null;

    // Get the player position
    Vector2 playerPosition = entity.getComponent(PhysicsComponent.class).getBody().getPosition();
    float closestDistance = Float.MAX_VALUE;

    for (Map.Entry<Entity, Vector2> entry : interactables.entrySet()) {
        Entity entity = entry.getKey();
        Vector2 entityPosition = entry.getValue();

        float distance = playerPosition.dst(entityPosition);

        if (/*distance <= 1f &&*/distance < closestDistance) {
            closestDistance = distance;
            closestEntity = entity;
        }
    }
    
    closestEntity.getEvents().trigger("Station Interaction", playerInventory, displayInventory, type);
  }

  /**
   * Moves the player towards a given direction.
   *
   * @param direction direction to move in
   */
  public void walk(Vector2 direction) {
    this.walkDirection = direction;
    moving = true;
  }

  /**
   * Stops the player from walking.
   */
  public void stopWalking() {
    this.walkDirection = Vector2.Zero.cpy();
    updateSpeed();
    moving = false;
  }

  /**
   * Makes the player attack.
   */
  void attack() {
    Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
    attackSound.play();
  }
}
