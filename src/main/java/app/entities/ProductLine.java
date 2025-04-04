package app.entities;

public class ProductLine {
    private int productLineId;
    private int quantity;
    private float totalPrice;
    private String bottomName;
    private float bottomPrice;
    private String toppingName;
    private float toppingPrice;
    private int bottomId;
    private int toppingId;

    // Konstruktører
    public ProductLine(int bottomId, String bottomName, float bottomPrice,
                       int toppingId, String toppingName, float toppingPrice) {
        this.bottomId = bottomId;
        this.bottomName = bottomName;
        this.bottomPrice = bottomPrice;
        this.toppingId = toppingId;
        this.toppingName = toppingName;
        this.toppingPrice = toppingPrice;
        this.totalPrice = bottomPrice + toppingPrice;
        this.quantity = 1;
    }

    public ProductLine(int bottomId, String bottomName, float bottomPrice,
                       int toppingId, String toppingName, float toppingPrice,
                       int quantity) {
        this(bottomId, bottomName, bottomPrice, toppingId, toppingName, toppingPrice);
        this.quantity = quantity;
        this.totalPrice = (bottomPrice + toppingPrice) * quantity;
    }

    // Gettere og settere
    public int getProductLineId() {
        return productLineId;
    }

    public void setProductLineId(int productLineId) {
        this.productLineId = productLineId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateTotalPrice(); // Opdater totalpris ved ændring af antal
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBottomName() {
        return bottomName;
    }

    public void setBottomName(String bottomName) {
        this.bottomName = bottomName;
    }

    public float getBottomPrice() {
        return bottomPrice;
    }

    public void setBottomPrice(float bottomPrice) {
        this.bottomPrice = bottomPrice;
        updateTotalPrice(); // Opdater totalpris ved ændring af bottom pris
    }

    public String getToppingName() {
        return toppingName;
    }

    public void setToppingName(String toppingName) {
        this.toppingName = toppingName;
    }

    public float getToppingPrice() {
        return toppingPrice;
    }

    public void setToppingPrice(float toppingPrice) {
        this.toppingPrice = toppingPrice;
        updateTotalPrice(); // Opdater totalpris ved ændring af topping pris
    }

    public int getBottomId() {
        return bottomId;
    }

    public void setBottomId(int bottomId) {
        this.bottomId = bottomId;
    }

    public int getToppingId() {
        return toppingId;
    }

    public void setToppingId(int toppingId) {
        this.toppingId = toppingId;
    }

    // Hjælpemetode til at opdatere totalprisen
    private void updateTotalPrice() {
        this.totalPrice = (bottomPrice + toppingPrice) * quantity;
    }

    // Ny metode til at beregne enkeltpris (uden quantity)
    public float getUnitPrice() {
        return bottomPrice + toppingPrice;
    }
}