package app.entities;

public class ProductLine {
    private int productLineId;
    private int quantity;
    private float totalPrice;
    private String bottomName;
    private float bottomPrice;
    private String toppingName;
    private float toppingPrice;

    public ProductLine(int bottomId, String bottomName, float bottomPrice, int toppingId, String toppingName, float toppingPrice, int quantity) {
        this.bottomName = bottomName;
        this.bottomPrice = bottomPrice;
        this.toppingName = toppingName;
        this.toppingPrice = toppingPrice;
        this.quantity = quantity;
        this.totalPrice = (bottomPrice + toppingPrice) * quantity;
    }

    public String getBottomName() {
        return bottomName;
    }

    public float getBottomPrice() {
        return bottomPrice;
    }

    public String getToppingName() {
        return toppingName;
    }

    public float getToppingPrice() {
        return toppingPrice;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }
}

