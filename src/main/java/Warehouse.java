public class Warehouse extends Building {
    private Bank bank;
    public Warehouse(Junction junction){
        super(junction);
    }
    /**
     * Sets the bank associated with the warehouse to check for funds during purchases.
     *
     * @param bank The bank to be set.
     */
    public void setBank(Bank bank) {
        this.bank = bank;
    }
    /**
     * Processes a player's request to buy an equipment (e.g., DragonBlade).
     * It checks with the bank if the player has enough funds before proceeding.
     *
     * @param p The player attempting to buy the equipment.
     * @param id The ID of the equipment to buy.
     * @return The purchased Equipment object, or null if the purchase failed.
     */
    public Equipment buyEquipment(Player p, int id) {
        Skeleton.printFunctionCall("Warehouse.buyEquipment");
        Equipment boughtEquipment = null;
        if (bank != null) {
            int price = 50; // Dummy price for skeleton
            boolean success = bank.hasEnoughMoney(p, price);
            if (success) {
                // Here the equipment is created and given to the player (true branch)
            } else {
                // Purchase failed (false branch)
            }
        }
        Skeleton.printReturn();
        return boughtEquipment;
    }

    /**
     * Processes a player's request to buy a new snowplow.
     * Validates available funds via the bank before spawning the vehicle.
     *
     * @param p The player attempting to buy the snowplow.
     * @return The new Snowplow instance, or null if the transaction was unsuccessful.
     */
    public Snowplow buySnowplow(Player p) {
        Skeleton.printFunctionCall("Warehouse.buySnowplow");
        Snowplow boughtSnowplow = null;
        if (bank != null) {
            int price = 500; // Dummy price for skeleton
            boolean success = bank.hasEnoughMoney(p, price);
            if (success) {
                // Here the snowplow is created and given to the player (true branch)
            } else {
                // Purchase failed (false branch)
            }
        }
        Skeleton.printReturn();
        return boughtSnowplow;
    }

    /**
     * Exchanges the current equipment on a snowplow with one from the warehouse stock.
     * The swap only occurs if the snowplow is physically at the warehouse and
     * the requested equipment is in stock.
     *
     * @param s The snowplow requesting the equipment change.
     * @param eq The specific equipment piece to be equipped.
     */
    public void changeEquipment(Snowplow s, Equipment eq) {
        Skeleton.printFunctionCall("Warehouse.changeEquipment");
        if(isAtWarehouse(s)) {
            if(isInStock(eq)) {
                addtoStock(s.getCurrentEquipment());
                s.setEquipment(eq);
                removeFromStock(eq);
            }
        }
        Skeleton.printReturn();
    }

    /**
     * Adds an equipment piece to the warehouse's internal inventory.
     * Usually called when a player swaps out or returns old equipment.
     *
     * @param eq The equipment to add to stock.
     */
    public void addtoStock(Equipment eq) {
        Skeleton.printFunctionCall("Warehouse.addToStock");
        Skeleton.printReturn();
    }

    /**
     * Verifies if a specific snowplow is currently at the warehouse location.
     *
     * @param s The snowplow to check.
     * @return True if the snowplow is at the warehouse, false otherwise.
     */
    public boolean isAtWarehouse(Snowplow s) {
        Skeleton.printFunctionCall("Warehouse.isAtWarehouse");
        Skeleton.printReturn();
        return Skeleton.askBool("A raktárnál van a hókotró?");
    }

    /**
     * Checks if a specific piece of equipment is currently available in the warehouse.
     *
     * @param eq The equipment to look for.
     * @return True if the item is in the inventory, false otherwise.
     */
    public boolean isInStock(Equipment eq) {
        Skeleton.printFunctionCall("Warehouse.isInStock");
        Skeleton.printReturn();
        return Skeleton.askBool("A raktárban van a kotrófej?");
    }

    /**
     * Removes an equipment piece from the warehouse inventory.
     * Called when equipment is assigned to a player or snowplow.
     *
     * @param eq The equipment to remove.
     */
    public void removeFromStock(Equipment eq) {
        Skeleton.printFunctionCall("Warehouse.removeFromStock");
        Skeleton.printReturn();
    }
}
