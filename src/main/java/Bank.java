import java.util.HashMap;

public class Bank {
    private Warehouse warehouse;
    private HashMap<Player, Integer> accounts;
    public Bank() {
        accounts = new HashMap<>();
    }
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
        int currentAmount = accounts.getOrDefault(p, 0);
        accounts.put(p, currentAmount + amount);
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
        int currentAmount = accounts.getOrDefault(p, 0);
        return currentAmount >= amount;
    }

    public int getBalance(Player p) {
        return accounts.getOrDefault(p, 0);
    }

    public HashMap<Player, Integer> getAccounts() {
        return accounts;
    }
}
