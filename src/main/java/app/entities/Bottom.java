package app.entities;

public class Bottom {
    String bottomName;
    private int bottomId;
    float bottomPrice;

    public Bottom(int bottomId, String bottomName, float bottomPrice) {
        this.bottomId = bottomId;
        this.bottomPrice = bottomPrice;
        this.bottomName = bottomName;

    }

    public Bottom(int bottomId, float bottomPrice) {
        this.bottomId = bottomId;
        this.bottomPrice = bottomPrice;
    }

    public Bottom(String bottomName, float bottomPrice) {
        this.bottomName = bottomName;
        this.bottomPrice = bottomPrice;
    }

    public String getBottomName() {
        return bottomName;
    }

    public float getBottomPrice() {
        return bottomPrice;
    }

    public int getBottomId() {
        return bottomId;
    }

}
