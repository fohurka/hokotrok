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
     * It checks with the bank if the player has enough funds.
     *
     * @param p The player attempting to buy the equipment.
     * @param id The ID of the equipment to buy.
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
     * It checks with the bank if the player has enough funds.
     *
     * @param p The player attempting to buy the snowplow.
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

    public void addtoStock(Equipment eq) {
        Skeleton.printFunctionCall("Warehouse.addToStock");
        Skeleton.printReturn();
    }

    public boolean isAtWarehouse(Snowplow s) {
        Skeleton.printFunctionCall("Warehouse.isAtWarehouse");
        Skeleton.printReturn();
        return Skeleton.askBool("Are you at the warehouse?");
    }

    public boolean isInStock(Equipment eq) {
        Skeleton.printFunctionCall("Warehouse.isInStock");
        Skeleton.printReturn();
        return Skeleton.askBool("Is the equipment in stock?");
    }

    public void removeFromStock(Equipment eq) {
        Skeleton.printFunctionCall("Warehouse.removeFromStock");
        Skeleton.printReturn();
    }
}
