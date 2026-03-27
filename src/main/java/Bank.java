public class Bank {
    private Warehouse warehouse;
    /**
     * Sets the warehouse associated with the bank.
     *
     * @param warehouse The warehouse to be set.
     */
    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
    /**
     * Pays a specified amount of money to the given player.
     *
     * @param p The player receiving the money.
     * @param amount The amount of money to be paid.
     */
    public void pay(Player p, int amount) {
        Skeleton.printFunctionCall("Bank.pay");
        //not implemented yet
        Skeleton.printReturn();
    }

    /**
     * Checks if the player has enough money to complete a purchase.
     * In the skeleton, it asks the tester for the internal state.
     *
     * @param p The player attempting the purchase.
     * @param amount The required amount of money.
     * @return true if the player has enough money, false otherwise.
     */
    public boolean hasEnoughMoney(Player p, int amount) {
        Skeleton.printFunctionCall("Bank.hasEnoughMoney");

        boolean hasMoney = Skeleton.askBool("Van elegendo penze a jatekosnak a vasarlashoz?");

        Skeleton.printReturn();
        return hasMoney;
    }
}
