
package com.csse3200.game.components.player;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.components.items.ItemComponent;

/**
 * A UI component used to display slots of an InventoryComponent and the
 * items they hold. To use: add this component to an entity that has a
 * InventoryComponent. This component will not work if the entity
 * it is added to does not have a InventoryComponent.
 */
public class InventoryDisplay extends UIComponent {
    private Table table;

    private ArrayList<Stack> slots;


    @Override
    public void create() {
        super.create();

        if (entity != null) {
            // listener for when the InventoryComponent attached to this entity is updated
            entity.getEvents().addListener("updateInventory", this::update);
        }
        addActors();
    }

    /**
     * Creates the required actors to display each inventory slot, and adds them
     * to the UI table.
     */
    private void addActors() {
        // Create table
        table = new Table();
        slots = new ArrayList<>();
        table.bottom().left();
        table.padBottom(10f);

        // create slot images
        for (int i = 0; i < entity.getComponent(InventoryComponent.class).getCapacity(); i++) {
            Stack currentStack = new Stack();
            slots.add(currentStack);
            currentStack.add(new Image(ServiceLocator.getResourceService().getAsset("images/inventory_ui/slot.png", Texture.class)));

            // add item image if there is an item in the slot
            ItemComponent item = entity.getComponent(InventoryComponent.class).getItemAt(i);
            if (item != null) {
                Table itemPadding = new Table();
                //N! cannot access item's parent entity from within another component
                // 3 options:
                // 1. change InventoryComponent to hold Entity types (that we assume have an ItemComponent or derivative)
                // 2. make a get texture method in ItemComponent that returns the texture component's path (this makes ItemComponent
                // now reliant on the entity also having a TextureRenderComponent
                // 3. construct the path to the image using the Item's state variables. This is difficult as there
                // are several classes that extend the ItemComponent, and thus store different variables. Most variables
                // we would require (i.e. chopped, burnt, etc) are not included in the base ItemComponent class.

                String itemTexturePath = item.getTexturePath();
                Image itemImage;


                if (itemTexturePath != null) {
                    itemImage = new Image(ServiceLocator.getResourceService().getAsset(itemTexturePath, Texture.class));
                } else {
                    // placeholder lettuce image if no texture found for item
                    itemImage = new Image(ServiceLocator.getResourceService().getAsset("images/ingredients/raw_lettuce.png", Texture.class));
                }

                itemPadding.add(itemImage).pad(20);
                currentStack.add(itemPadding);
            }
            table.add(currentStack).size(200).padLeft(20);

        }

        stage.addActor(table);
    }

    /**
     * Updates the UI display to reflect the current state of the InventoryComponent
     * of this component's parent entity.
     */
    private void updateDisplay() {

        for (int i = 0; i < slots.size(); i++) {
            Stack currentStack = slots.get(i);
            currentStack.clear();
            currentStack.add(new Image(ServiceLocator.getResourceService().getAsset("images/inventory_ui/slot.png", Texture.class)));

            // add item image if there is an item in the slot
            ItemComponent item = entity.getComponent(InventoryComponent.class).getItemAt(i);
            if (item != null) {
                Table itemPadding = new Table();

                String itemTexturePath = item.getTexturePath();
                Image itemImage;
                if (itemTexturePath != null) {
                    itemImage = new Image(ServiceLocator.getResourceService().getAsset(itemTexturePath, Texture.class));
                } else {
                    // placeholder lettuce image if no texture found for item
                    itemImage = new Image(ServiceLocator.getResourceService().getAsset("images/ingredients/raw_lettuce.png", Texture.class));
                }

                itemPadding.add(itemImage).pad(20);
                currentStack.add(itemPadding);
            }
        }

        }

    /**
     * Updates this InventoryDisplay to reflect the current state of the InventoryComponent
     * of this component's parent entity.
     */
    public void update() {
        // updateLabel(entity.getComponent(InventoryComponent.class).getItemFirst());
        updateDisplay();
        //updateLabel(entity.getComponent(InventoryComponent.class).getItemFirst());
    }

    @Override
    public void draw(SpriteBatch batch)  {
        // changing position on window resizing is handled by the stage
    }

    @Override
    public void dispose() {
        super.dispose();
        table.remove();
        for (Stack slot : slots) {
            slot.remove();
        }
    }

    @Override
    public void setStage(Stage mock) {
        this.stage = mock;
    }
}