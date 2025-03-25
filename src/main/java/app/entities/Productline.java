package app.entities;


public class ProductLine {
    private int productLineId;
    private int quantity;
    private float totalPrice;
    private Cupcake cupcake;

    public ProductLine(int productLineId, Cupcake cupcake, int quantity, float totalPrice) {
        this.cupcake = cupcake;
        this.productLineId = productLineId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public ProductLine(Cupcake cupcake, int quantity, float totalPrice) {
        this.cupcake = cupcake;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public ProductLine(Cupcake cupcake, int quantity) {
        this.cupcake = cupcake;
        this.quantity = quantity;
    }

    public ProductLine(int productLineId, Cupcake cupcake) {
        this.productLineId = productLineId;
        this.cupcake = cupcake;
    }

    public Cupcake getCupcake() {
        return cupcake;
    }

    public int getProductLineId() {
        return productLineId;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getTotalPrice() {
        return cupcake.getPrice() * quantity;
    }
}