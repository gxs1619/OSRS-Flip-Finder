/**
 * Written by: Griffin Seibold
 * 1/30/2019
 * Item is a representation of an item in the database. An item consists
 * of a name, id, description, selling price, buying price, buying quantity,
 * selling quantity, the profit margin and when it was last updated.
 */
public class Item {
    private String name;
    private int id;
    private String sellPrice;
    private String buyPrice;
    private String sellQuantity;
    private String buyQuantity;
    private String lastUpdated;

    /**
     * The following are basic getters and setter for an Item object.
     */
    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getMargin(){
        return Integer.parseInt(buyPrice)-Integer.parseInt(sellPrice);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getSellQuantity() {
        return sellQuantity;
    }

    public void setSellQuantity(String sellQuantity) {
        this.sellQuantity = sellQuantity;
    }

    public String getBuyQuantity() {
        return buyQuantity;
    }

    public void setBuyQuantity(String buyQuantity) {
        this.buyQuantity = buyQuantity;
    }
}
