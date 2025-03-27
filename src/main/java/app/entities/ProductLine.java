package app.entities;

public class ProductLine {
    private int productLineId;
    private int quantity;
    private float totalPrice;
    private String bottomName;
    private float bottomPrice;
    private String toppingName;
    private float toppingPrice;

    // Konstruktor
    public ProductLine(int bottomId, String bottomName, float bottomPrice, int toppingId, String toppingName, float toppingPrice) {
        this.bottomName = bottomName;
        this.bottomPrice = bottomPrice;
        this.toppingName = toppingName;
        this.toppingPrice = toppingPrice;
        this.totalPrice = bottomPrice + toppingPrice; // Mit svar på udregning af bottom og topping.
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

}

