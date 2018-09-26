package client.presentation.controller;

import client.presentation.quantity.Contants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import server.entity.Coordinate;
import server.entity.Picture;
import server.entity.Shape;
import server.service.PictureService;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 前端界面的Controller
 */
public class CanvasController implements Initializable{

    @FXML
    private Canvas canvas;                        //最上层canvas画板，用于操作“画”动作的逻辑

    @FXML
    private Canvas canvas_background;             //下层canvas画板，用于展示已识别图片

    private GraphicsContext context;              //上层画板的画布

    private GraphicsContext context_ground;       //下层画板的画布

    @FXML
    private ChoiceBox<String> picChoiceBox;       //项目选择器

    private boolean mouseState = false;           //前端变量，监听当前鼠标的状态

    private Picture pictureWorking;               //用户保存当前工作图片（画板）

    private List<List<Coordinate>> coordinate ;   // 当前图片的绘制坐标信息

    private List<Coordinate> coordinateLineNow;   //当前鼠标正在绘制的线信息

    private List<Shape> shapes;                   //当前画布上的图形对象


    /**
     * @param location
     * @param resources
     *
     * 默认加载的init方法
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        context = canvas.getGraphicsContext2D();
        context_ground = canvas_background.getGraphicsContext2D();

        newPic();

     }


    /**
     * @param e
     * 鼠标按下时的监听响应
     */
    @FXML
    private void canvasMouseDown(MouseEvent e){
        mouseState = true;
        context.beginPath();
        context.setStroke(Contants.STROKE_COLOR);
        context.setLineWidth(Contants.STROKE_LINE_WIDTH);
        coordinateLineNow = new ArrayList();
        Coordinate coordinateDown = new Coordinate(e.getX(),e.getY());
        context.moveTo(e.getX(),e.getY());
        coordinateLineNow.add(coordinateDown);


    }

    /**
     * @param e
     * 鼠标按下并移动时的监听响应
     */
    @FXML
    private void canvasMouseMove(MouseEvent e){

        if(mouseState){
            context.lineTo(e.getX(),e.getY());
            context.stroke();
            Coordinate coordinateMove = new Coordinate(e.getX(),e.getY());
            coordinateLineNow.add(coordinateMove);

        }

    }


    /**
     * 鼠标松开时的响应
     */
    @FXML
    private void canvasMouseUp(){

        coordinate.add(coordinateLineNow);
        mouseState = false;
    }


    /**
     * 清空画板
     */
    @FXML
    private void clearCanvas(){

        context_ground.setGlobalBlendMode(BlendMode.SRC_OVER);
        context_ground.clearRect(0,0,Contants.CANVAS_WIDTH,Contants.CANVAS_HEIGHT);
        context.clearRect(0,0,Contants.CANVAS_WIDTH,Contants.CANVAS_HEIGHT);

        coordinate = new ArrayList();
        coordinateLineNow = new ArrayList();
        shapes = new ArrayList();



    }


    /**
     * 识别按钮的响应
     */
    @FXML
    private  void recognize() {
        if(coordinate.size() == 0){
            showDialog(Alert.AlertType.ERROR,Contants.ERROR_MESSAGE);
        }else {
            Shape shapeDrawing = new Shape(coordinate);
            coordinate = new ArrayList();
            shapes.add(shapeDrawing);

            Image image_recognized = canvas.snapshot(null,null);

            String typeDrawing = PictureService.recognize(image_recognized);
            shapeDrawing.setType(typeDrawing);
            shapeDrawing.setRecognized(true);
            changeLayer(image_recognized);

            setRecognition(shapeDrawing.getType(),shapeDrawing.getBarycenter().getCoX(),shapeDrawing.getBarycenter().getCoY());

//        context_ground.clearRect(0,0,700,500);
//        context.clearRect(0,0,700,500);
//        drawAllShape(shapes);


//        System.out.println(typeDrawing);

//
//
//        double shape_center_x = ((double)(right + left )) / 2 / width * canvas_background.getWidth();
//        double shape_center_y = ((double) (buttom + top)) / 2 / height * canvas_background.getHeight();
//        System.out.println((right + left ) / 2 / width+""+(buttom + top) / 2 / height);
//        setRecognition(type,shape_center_x-11,shape_center_y-11);
//
//
//        context.clearRect(0,0,700,500);
//        image_list.add(image_recognized);
//        context_ground.beginPath();
//        context_ground.drawImage(image_recognized,0,0);
//        context_ground.setGlobalBlendMode(BlendMode.MULTIPLY);
////        context_ground.closePath();
//        System.out.println(right);
//
//
////        context_ground.beginPath();
//        switch (type){
//            case "矩形":
//                context_ground.setStroke(Color.RED);
//                context_ground.setLineWidth(3);
//                context_ground.moveTo(left,top);
//                context_ground.lineTo(right,top);
//                context_ground.lineTo(right,buttom);
//                context_ground.lineTo(left,buttom);
//                context_ground.lineTo(left,top);
//                context_ground.stroke();
//
//                break;
//            case "正方形":
//                context_ground.setStroke(Color.RED);
//                context_ground.setLineWidth(3);
//                context_ground.moveTo(left,top);
//                context_ground.lineTo(right,top);
//                context_ground.lineTo(right,buttom);
//                context_ground.lineTo(left,buttom);
//                context_ground.lineTo(left,top);
//                context_ground.stroke();
//                break;
//            case "圆":
//                context_ground.setStroke(Color.RED);
//                context_ground.setLineWidth(3);
//                context_ground.strokeOval(left,top,right - left,right - left);
//                break;
//            case "三角形":
//                ArrayList<Integer> x_arr = new ArrayList<>();
//                ArrayList<Integer> y_arr = new ArrayList<>();
//
//                int count_one = 0;
//                int count_two = 0;
//                int count_three = 0;
//                int count_four = 0;
//                int count_all = 0;
//                for(int i=0;i<height;i++) {
//                    for (int j = 0; j < width; j++) {//行扫描
//                        if(count_one==0){
//                            if(j == left){
//                                int dip = bi.getRGB(j, i);
//                                if(dip!= -1){
//                                    x_arr.add(j);
//                                    y_arr.add(i);
//                                    count_one++;
//                                    count_all++;
//                                    System.out.println(1);
//                                }
//
//                            }
//
//                        }
//                        if(count_two == 0){
//                            if(j == right){
//                                int dip = bi.getRGB(j, i);
//                                if(dip!= -1){
//                                    x_arr.add(j);
//                                    y_arr.add(i);
//                                    count_two++;
//                                    count_all++;
//                                    System.out.println(2);
//                                }
//                            }
//
//                        }
//                        if(count_three == 0){
//                            if(i == top){
//                                int dip = bi.getRGB(j, i);
//                                if(dip!= -1){
//                                    x_arr.add(j);
//                                    y_arr.add(i);
//                                    count_three++;
//                                    count_all++;
//                                    System.out.println(3);
//                                }
//                            }
//
//                        }
//                        if(count_four == 0){
//                            if(i == buttom){
//                                int dip = bi.getRGB(j, i);
//                                if(dip!= -1){
//                                    x_arr.add(j);
//                                    y_arr.add(i);
//                                    count_four++;
//                                    count_all++;
//                                    System.out.println(4);
//                                }
//                            }
////                            count_four++;
//                        }
//
//                        if(count_all == 4){
//                            break;
//                        }
//
//                    }
//                }
//                System.out.println(left+","+right+","+top+","+buttom);
//                System.out.println(x_arr);
//                System.out.println(y_arr);
//
//
//                context_ground.setStroke(Color.RED);
//                context_ground.setLineWidth(3);
//                context_ground.strokePolygon(new double[]{x_arr.get(0), x_arr.get(1), x_arr.get(2),x_arr.get(3)},
//                        new double[]{y_arr.get(0),y_arr.get(1),y_arr.get(2),y_arr.get(3)}, 4);
//                if(x_arr.size() == 4){
//                    ArrayList<Double> area_list = new ArrayList<>();
//                    for (int i = 0 ;i< 4;i++){
//                        ArrayList<Integer> x_temp = new ArrayList<>();
//                        ArrayList<Integer> y_temp = new ArrayList<>();
//                        for (int j = 0 ; j<4;j++){
//                            if(j!=i){
//                                x_temp.add(x_arr.get(j));
//                                y_temp.add(y_arr.get(j));
//                            }
//                        }
//                        System.out.println(x_temp);
//                        System.out.println(y_temp);
//                        double area_t = calculateArea(x_temp,y_temp);
//                        area_list.add(area_t);
//
//                    }
//                    System.out.println(area_list+"s");
//                    double max = Collections.max(area_list);
//                    int indes_max = area_list.indexOf(max);
//                    x_arr.remove(indes_max);
//                    y_arr.remove(indes_max);
//
//                }
//
//                context_ground.setStroke(Color.GREEN);
////                context_ground.strokePolygon(new double[]{x_arr.get(0), x_arr.get(1), x_arr.get(2)},
////                        new double[]{y_arr.get(0),y_arr.get(1),y_arr.get(2)}, 3);
////                context_ground.s
//
//
//                break;
//            default:
//                break;
//        }

//        context.closePath();
        }
    }


    /**
     * @param type  图形的类型
     * @param x     图形重心x坐标
     * @param y     图形重心的y坐标
     *
     *    在画板中加载图形的识别信息
     */
    private void setRecognition(String type,double x,double y){
        context_ground.fillText(type,x,y);

    }


    private double calculateArea(ArrayList<Integer> x_arr,ArrayList<Integer> y_arr){

        double x1 = x_arr.get(0);
        double y1 = y_arr.get(0);
        double x2 = x_arr.get(1);
        double y2 = y_arr.get(1);
        double x3 = x_arr.get(2);
        double y3 = y_arr.get(2);
        double area = Math.abs((x1*y2+y1*x3+x2*y3)-(x1*y3+y2*x3+y1*x2));
        return area;
    }

    /**
     * @param image
     *
     * 更改识别图形的画布 从canvas 到canvas_ground
     */
    private void changeLayer(Image image){

        Image image_recognized = image;
        context.clearRect(0,0,Contants.CANVAS_WIDTH,Contants.CANVAS_HEIGHT);

        context_ground.drawImage(image_recognized,0,0);
        context_ground.setGlobalBlendMode(BlendMode.MULTIPLY);
    }


    /**
     * 保存当前所画图
     */
    @FXML
    public void savePicture(){

        if(coordinate.size() != 0){
            Shape shapeUnRecognized = new Shape(coordinate,false);
            shapes.add(shapeUnRecognized);
        }

       if(pictureWorking ==null){
           SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
           String id = df.format(new Date());

           pictureWorking = new Picture(id,shapes);
       }else {
            pictureWorking.setShapes(shapes);
       }
        PictureService.savePicture(pictureWorking);

        newPic();

        picChoiceBox.getSelectionModel().select(-1);   //空项目

        showDialog(Alert.AlertType.INFORMATION,Contants.SUCCESS_MESSAGE);

    }

    /**
     * @return
     *
     * 得到所有所画图的id列表
     */
    private List<String> getAllPicId(){
        List picList = PictureService.findAllPicId();
        return picList;
    }

    /**
     * 新建图的响应
     */
    @FXML
    private void newPic(){

        clearCanvas();
        pictureWorking = null;
//
        List<String> picList = getAllPicId();
        ObservableList picListChoice = FXCollections.observableArrayList(picList);;
        picChoiceBox.setItems(picListChoice);

        picChoiceBox.getSelectionModel().select(-1);
    }


    /**
     * 加载当前图上所有的图形及识别信息
     *
     */
    public void loadPic(){


        int selectedIndex = picChoiceBox.getSelectionModel().getSelectedIndex();
        if(selectedIndex !=-1){
            clearCanvas();

            String picIdSelected = picChoiceBox.getSelectionModel().getSelectedItem().toString();
            pictureWorking = PictureService.findPicture(picIdSelected);
            shapes = pictureWorking.getShapes();

            drawAllShape(shapes);

            for (int  i = 0 ; i<shapes.size();i++){
                Shape shapeSingle = shapes.get(i);
                if(!shapeSingle.isRecognized()){
                    coordinate = shapeSingle.getCoordinates();
                    shapes.remove(i);
                    break;
                }

            }
        }




    }

    /**
     * @param shapeList
     *
     * 将所有的图形信息转化到画板上展示
     */
    public void drawAllShape(List<Shape> shapeList){
        context_ground.beginPath();
        context.beginPath();
        context_ground.setStroke(Contants.STROKE_COLOR);
        context_ground.setLineWidth(Contants.STROKE_LINE_WIDTH);
        context.setStroke(Contants.STROKE_COLOR);
        context.setLineWidth(Contants.STROKE_LINE_WIDTH);
        for (int i = 0;i<shapeList.size();i++){
            Shape shapeSingle = shapeList.get(i);
            List<List<Coordinate>> coordinatesGet = shapeSingle.getCoordinates();
//            System.out.println(coordinatesGet.size());
            for (int  j= 0;j<coordinatesGet.size();j++){

                List<Coordinate> lineSingle = coordinatesGet.get(j);

                for (int n = 0;n<lineSingle.size();n++){
                    Coordinate coordinateSingle = lineSingle.get(n);
                    if(n==0){
                        if(shapeSingle.isRecognized()){
                            context_ground.moveTo(coordinateSingle.getCoX(),coordinateSingle.getCoY());
                        }else {
                            context.moveTo(coordinateSingle.getCoX(),coordinateSingle.getCoY());
                        }
                    }else {
                        if(shapeSingle.isRecognized()){
                            context_ground.lineTo(coordinateSingle.getCoX(),coordinateSingle.getCoY());
                        }else {
                            context.lineTo(coordinateSingle.getCoX(),coordinateSingle.getCoY());
                        }
                    }
                    context_ground.stroke();
                    context.stroke();
               }
            }

            if(shapeSingle.isRecognized()){
                context_ground.fillText(shapeSingle.getType(),shapeSingle.getBarycenter().getCoX(),shapeSingle.getBarycenter().getCoY());
            }

        }
        context_ground.closePath();
        context.closePath();
        context_ground.setGlobalBlendMode(BlendMode.MULTIPLY);
    }


    /**
     * @param type   对话窗类别
     * @param message 提示信息
     * @return          对话窗交互结果
     *
     * javafx的对话框 提醒是否保存及结果等信息
     */
    private Optional<ButtonType> showDialog(Alert.AlertType type,String message){
        Alert alert = new Alert(type);
        alert.setHeaderText(message);

        return alert.showAndWait();

    }


}
