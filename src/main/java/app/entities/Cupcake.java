package app.entities;

public class Cupcake {
    private Bottom bottom;
    private Topping topping;

    public Cupcake(Bottom bottom, Topping topping) {
        this.bottom = bottom;
        this.topping = topping;
    }

    public int getPrice() {
        return (int) (bottom.getBottomPrice() + topping.getToppingPrice());
    }

    public Bottom getBottom() {

        return bottom;
    }

    public Topping getTopping() {
        return topping;
    }
}
