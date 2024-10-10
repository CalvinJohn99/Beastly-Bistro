package com.csse3200.game.components.station;

import java.util.ArrayList;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.items.IngredientComponent;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.InventoryDisplay;
import com.csse3200.game.components.station.loader.StationAcceptableItemsGetter;

public class StationItemHandlerComponent extends Component {
    /**
     * String type - storing type of station
     * StationInventoryComponent inventory component - instance of inventory for this station
     * acceptableItems - ArrayList which contains all accepted items or it null
     */
    protected final String type;
    protected InventoryComponent inventoryComponent;
    protected ArrayList<String> acceptableItems;

    // General TODO:
    // Add trigger calls to external for failed interactions
    // Processing in Inventory component, animation, timing and mapping
    // Create subclass for each station where needed, eg classic bench will need
    // to call add second component method that we don't want all stations to be able to access

    /**
     * General constructor
     * @param type - storing type of station
     */
    public StationItemHandlerComponent(String type) {
        this.type = type;
        this.acceptableItems = StationAcceptableItemsGetter.getAcceptableItems(type);
    }

    /**
     *  Called on creation of the station to allow outside interaction within the station.
     *  Adds the listener for set current item and for remove current item.
     */
    @Override
    public void create() {
        entity.getEvents().addListener("Station Interaction", this::handleInteraction);
        inventoryComponent = entity.getComponent(InventoryComponent.class);
    }

    /**
     * Gets the type of station
     * @return station type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Checks if the item can be accepted
     * @param item to check if you can be accepted
     * @return true if it can be accepted, false otherwise.
     */
    public boolean isItemAccepted(ItemComponent item) {
        // If the acceptable items is null the station is assumed to be able
        // to carry any item
        if (this.acceptableItems == null) {
            return true;
        }

        String itemName = item.getItemName().toLowerCase();

        for (String acceptableItem : this.acceptableItems) {
            if (acceptableItem.equals(itemName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Handles any interaction with station, using current state of player and station
     * inventory to determine intended interaction
     * @param playerInventoryComponent reference to player inventory component
     * @param inventoryDisplay reference to individual inventory display
     * @param type the type of interaction attempt
     */
    public void handleInteraction(InventoryComponent playerInventoryComponent, InventoryDisplay inventoryDisplay, String type) {
        switch (type) {
            case "default" -> this.handleInteractionDefault(playerInventoryComponent);
            case "chop" -> this.handleInteractionChop();
            case "stopChop" -> this.handleInteractionStopChop();
            default -> {
                // Do nothing, other options aren't relevant...
            }
        }
    }

    /**
     * Function to handle the default interaction between a player and a station.
     * @param playerInventoryComponent: the inventory component of the player
     */
    private void handleInteractionDefault(InventoryComponent playerInventoryComponent) {
        // Input to station
        if (playerInventoryComponent.isFull()) {
            ItemComponent item = playerInventoryComponent.getItemFirst();
            // Check item is accepted
            if (isItemAccepted(item)) {
                this.stationReceiveItem(item, playerInventoryComponent);
            }
        // Output from station
        } else if (inventoryComponent.isFull()) {
            // Player wants item from station
            this.stationGiveItem(playerInventoryComponent);
        }
    }

    private void handleInteractionChop() {
        // Attempt to start chopping the ingredient
        entity.getEvents().trigger("Chop Ingredient");
    }

    private void handleInteractionStopChop() {
        // Attempt to stop chopping the ingredient
        entity.getEvents().trigger("Stop Chopping Ingredient");
    }

    /**
     *
     * @return current Item being stored
     */
    public ItemComponent peek() {
        return this.inventoryComponent.getItemFirst();
    }

    /**
     * Function to start the updating of items when the station receives an item
     */
    private void onReceiveItem() {
        switch (type) {
            case "oven", "stove":
                cookingStationReceiveItem();
                break;
            case "cutting board", "blender":
                break; // Don't do anything since chopping is manual now :)
            default:
                break;
        }
    }

    /**
     * Function to be called when a cooking station receives the item
     */
    private void cookingStationReceiveItem() {
        // First check the item is actually available and working
        ItemComponent item = inventoryComponent.getItemFirst();

        if (item.getEntity().getComponent(IngredientComponent.class) == null
                || !item.getEntity().getComponent(IngredientComponent.class).getIsCookable()) {
            return; // Item doesn't exit or isn't bookable
        }

        // We know item exists and is bookable
        entity.getEvents().trigger("Cook Ingredient");
    }

    /**
     * Function to stop the updating of item when the station gives an item
     * back to the user
     */
    private void onGiveItem() {
        switch (type) {
            case "oven", "stove":
                cookingStationGiveItem();
                break;
            case "cutting board", "blender":
                choppingStationGiveItem();
                break;
            default:
                break;
        }
    }

    /**
     * Function for a cooking station to stop cooking an item when it is removed
     * from a station
     */
    private void cookingStationGiveItem() {
        // First check the item is actually available and working
        ItemComponent item = inventoryComponent.getItemFirst();

        if (item.getEntity().getComponent(IngredientComponent.class) == null
                || !item.getEntity().getComponent(IngredientComponent.class).getIsCookable()) {
            return; // Item doesn't exit or isn't bookable
        }

        // We know item exists and is bookable
        entity.getEvents().trigger("Stop Cooking Ingredient");
    }

    /**
     * Function for a chopping station to stop chopping an item when it is 
     * removed from the station.
     */
    private void choppingStationGiveItem() {
        // First check the item is actually available and working
        ItemComponent item = inventoryComponent.getItemFirst();

        if (item.getEntity().getComponent(IngredientComponent.class) == null
                || !item.getEntity().getComponent(IngredientComponent.class).getIsChoppable()) {
            return; // Item doesn't exit or isn't patchable
        }

        // We know item exits and is patchable
        entity.getEvents().trigger("Stop Chopping Ingredient");
    }

    /**
         Takes the item from the player and stores in station
         @param playerInventoryComponent reference to player inventory
     */
    public void stationReceiveItem(ItemComponent item, InventoryComponent playerInventoryComponent) {
        this.inventoryComponent.addItemAt(item, 0);
        // 0 by default, same position as getItemFirst()
        playerInventoryComponent.removeAt(0);
        
        // Need timers and animation start here, for Animations and Timer task ticket
        onReceiveItem();
    }

    /**
        Takes the item from the station, and returns the old item
        @param playerInventoryComponent reference to player inventory
     */
    public void stationGiveItem(InventoryComponent playerInventoryComponent) {
        onGiveItem();

        ItemComponent item = inventoryComponent.getItemFirst();
        playerInventoryComponent.addItemAt(item,0);
        // Remove single item in station
        this.inventoryComponent.removeAt(0);

        entity.getEvents().trigger("interactionEnd");
    }

}
