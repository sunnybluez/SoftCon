package client.presentation.quantity;


import javafx.scene.paint.Color;

/**
 * 常量类 管理 某些常量
 */
public final class Contants {
    private Contants() {
    }

    public static final double RECT_LOW_RATIO = 0.75; //面积识别中矩形面积最低占比
    public static final double CIRCLE_LOW_RATIO = 0.55; //面积识别中圆形面积最低占比
    public static final double WIDTH_HEIGHT_LOW_RATIO = 0.83; //面积识别中正方形宽高比最低比值
    public static final double WIDTH_HEIGHT_HIGH_RATIO = 1.2; //面积识别中正方形宽高比最高比值

    public static final Color STROKE_COLOR = Color.rgb(	135,206,235);   //前端画线的线色
    public static final int   STROKE_LINE_WIDTH = 3;                                    //前端画线的线长

    public static final int CANVAS_WIDTH = 700; //前端画板宽
    public static final int CANVAS_HEIGHT = 500; //前端画板高

    public static final String ERROR_MESSAGE = "当前画布不存在未识别图形"; //画板识别错误提示信息
    public static final String SUCCESS_MESSAGE = "保存成功"; //画板识别错误提示信息

}
