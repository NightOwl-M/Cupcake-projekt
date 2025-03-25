package app.entities;

public class Productline {

    private int orderLineId;
    private int quantity;
    private Cupcake cupcake;
    private float totalPrice;

    public Productline(int orderLineId, int quantity, Cupcake cupcake, float totalPrice){

        this.orderLineId = orderLineId;
        this.quantity = quantity;
        this.cupcake = cupcake;
        this.totalPrice = totalPrice;

    }

    public int getOrderLineId() {

        return orderLineId;

    }

    public int getQuantity() {

        return quantity;

    }

    public Cupcake getCupcake() {

        return cupcake;

    }

    public float getTotalPrice() {

        return totalPrice;

    }

}
