package app.entities;

import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private List<ProductLine> productLineList;
    private float orderPrice;
    private boolean isPaid;

    public Order(int orderId, int userId, float orderPrice, boolean isPaid) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderPrice = orderPrice;
        this.isPaid = isPaid;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public float getOrderPrice() {
        this.orderPrice = 0;
        for (ProductLine productLine : productLineList) {
            orderPrice += productLine.getTotalPrice();
        }
        return orderPrice;
    }

    public void setProductLineList(List<ProductLine> productLineList) {
        this.productLineList = productLineList;
    }
}