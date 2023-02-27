package com.example.bensmithinventoryapp.display.item;

/**
 * This DTO class contains information related to an item.
 */
public class Item {

    private String itemName;
    private Integer itemQuantity;

    /**
     * Constructor.
     * @param itemName - the item name
     * @param itemQuantity - the item quantity
     */
    public Item (String itemName, Integer itemQuantity) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
    }

    /**
     * Gets item name
     * @return - the item name
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Gets item quantity
     * @return - the item quantity
     */
    public Integer getItemQuantity() {
        return itemQuantity;
    }
}
