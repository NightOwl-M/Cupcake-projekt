package app.entities;

public class Cupcake {

    Bottom bottom;
    Topping topping;

    public Cupcake(Bottom bottom, Topping topping){

        this.bottom = bottom;
        this.topping = topping;

    }

    public float getTotalPrice(){

        return "Total price: "+bottom.getBottomPrice()+topping.getToppingPrice()+" DKK";

    }

}
