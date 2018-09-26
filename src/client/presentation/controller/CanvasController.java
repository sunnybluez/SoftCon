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
