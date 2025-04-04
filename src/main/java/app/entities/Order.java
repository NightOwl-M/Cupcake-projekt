package app.entities;

import java.time.LocalDate;

public class Order {

controllers_getAllOrders_new
    private int orderId;

    private User user;

    private List<ProductLine> productList;

    private double orderPrice;

    private boolean isPaid;


    public Order(int orderId, User user, List<ProductLine> productList, float orderPrice, boolean isPaid, LocalDate orderDate) {
        this.orderId = orderId;
        this.user = user;
        this.productList = productList;
        this.orderPrice = orderPrice;
        this.isPaid = isPaid;
    }

    public int getOrderId(){
        return orderId;
    }

    public User getUser(){
        return user;
    }

    public List<ProductLine> getProductList(){
        return productList;
    }

    public double getOrderPrice(){
        return orderPrice;
    }

    public boolean getIsPaid(){
        return isPaid;
    }
}