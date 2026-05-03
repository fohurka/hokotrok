import java.rmi.server.Skeleton;
import java.util.ArrayList;
import java.util.List;

public class Warehouse extends Building {
    private int[] equipmentPrices = {2000, 2500, 3000, 2750, 3500, 3000};
    private int snowplowPrice = 10000;
    private List<Equipment> stock = new ArrayList<>();
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
    public Equipment buyEquipment(SnowplowPlayer p, int id) {
        Equipment boughtEquipment = null;
        if (bank != null) {
            if (bank.hasEnoughMoney(p, equipmentPrices[id - 1])) {
                // Here the equipment is created and given to the player (true branch)
                switch (id) {
                case 1:
                    boughtEquipment = new Sweeper(p);
                    break;
                case 2:
                    boughtEquipment = new Impeller(p);
                    break;
                case 3:
                    boughtEquipment = new Salter(p);
                    break;
                case 4:
                    boughtEquipment = new IceBreaker(p);
                    break;
                case 5:
                    boughtEquipment = new DragonBlade(p);
                    break;
                default:
                    boughtEquipment = new Gritter(p);
                    break;
                }
                bank.pay(p, -equipmentPrices[id - 1]/10); // Deduct the cost from the player's account
                stock.add(boughtEquipment);
            } else {
                // Purchase failed (false branch)
            }
        }
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
        if(!(p instanceof SnowplowPlayer)) return null;
        Snowplow boughtSnowplow = null;
        if (bank != null) {
            if (bank.hasEnoughMoney(p, snowplowPrice)) {
                // Here the snowplow is created and given to the player (true branch)
                bank.pay(p, -snowplowPrice/10); // Deduct the cost from the player's account
                boughtSnowplow = new Snowplow();
                boughtSnowplow.setLocation(this);
            } else {
                // Purchase failed (false branch)
            }
        }
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
        if(isAtWarehouse(s)) {
            if(isInStock(eq)) {
                addtoStock(s.getCurrentEquipment());
                s.setEquipment(eq);
                removeFromStock(eq);
            }
        }
    }

    /**
     * Adds an equipment piece to the warehouse's internal inventory.
     * Usually called when a player swaps out or returns old equipment.
     *
     * @param eq The equipment to add to stock.
     */
    public void addtoStock(Equipment eq) {
        stock.add(eq);
    }

    /**
     * Verifies if a specific snowplow is currently at the warehouse location.
     *
     * @param s The snowplow to check.
     * @return True if the snowplow is at the warehouse, false otherwise.
     */
    public boolean isAtWarehouse(Snowplow s) {
        return s.getLocation() == this;
    }

    /**
     * Checks if a specific piece of equipment is currently available in the warehouse.
     *
     * @param eq The equipment to look for.
     * @return True if the item is in the inventory, false otherwise.
     */
    public boolean isInStock(Equipment eq) {
        return stock.contains(eq);
    }

    /**
     * Removes an equipment piece from the warehouse inventory.
     * Called when equipment is assigned to a player or snowplow.
     *
     * @param eq The equipment to remove.
     */
    public void removeFromStock(Equipment eq) {
        if(stock.contains(eq)) {
            stock.remove(eq);
        }
    }

    public List<Equipment> getStock() {
        return stock;
    }
}
