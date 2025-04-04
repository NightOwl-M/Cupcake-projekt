package app.entities;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private String userEmail; // Tilføjet nyt felt
    private List<ProductLine> productLineList = new ArrayList<>();
    private float orderPrice;
    private boolean isPaid;

    public Order(int orderId, int userId, float orderPrice, boolean isPaid) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderPrice = orderPrice;
        this.isPaid = isPaid;
    }

    // Gettere og settere
    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public float getOrderPrice() {
        // Genberegn hvis productLineList er ændret
        if (productLineList != null && !productLineList.isEmpty()) {
            orderPrice = 0;
            for (ProductLine pl : productLineList) {
                orderPrice += pl.getTotalPrice();
            }
        }
        return orderPrice;
    }

    public List<ProductLine> getProductLineList() {
        return productLineList;
    }

    public void setProductLines(List<ProductLine> productLines) {
        this.productLineList = productLines;
        // Opdater ordrepris ved ændring af produktlinjer
        getOrderPrice();
    }

    public void addProductLine(ProductLine productLine) {
        this.productLineList.add(productLine);
        // Opdater ordrepris
        this.orderPrice += productLine.getTotalPrice();
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
