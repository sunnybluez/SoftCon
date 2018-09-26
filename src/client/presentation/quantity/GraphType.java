package client.presentation.quantity;

public enum GraphType {
    Rect("长方形"),
    Circle("圆"),
    Square("正方形"),
    Triangle("三角形");

    private String type;
    private GraphType(String type){
        this.type = type;
    }


    public String getType() {
        return type;
    }
}
