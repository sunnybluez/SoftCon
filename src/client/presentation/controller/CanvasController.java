package client.presentation.controller;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import server.entity.Coordinate;
import server.entity.Picture;
import server.entity.Shape;
import server.service.PictureService;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class CanvasController implements Initializable{

    @FXML
    private Canvas canvas;

    @FXML
    private Canvas canvas_background;

    @FXML
    private Text recognizeResult;

    @FXML
    private AnchorPane myTagArea;

    @FXML
    private AnchorPane recognitionBord;

    @FXML
    private ChoiceBox<String> picChoiceBox;

    private boolean mouseState = false;

    private Picture pictureWorking;


    private GraphicsContext context;
    private GraphicsContext context_ground;

    private List<List<Coordinate>> coordinate ;
    private List<Coordinate> coordinate_temp;
    private List<Shape> shapes;

//    ArrayList<Image> image_list = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        context = canvas.getGraphicsContext2D();
        context_ground = canvas_background.getGraphicsContext2D();



        newPic();

     }





    @FXML
    private void canvasMouseDown(MouseEvent e){
        mouseState = true;
        context.beginPath();
        context.setStroke(Color.rgb(	135,206,235));
        context.setLineWidth(3);
        coordinate_temp = new ArrayList();
        Coordinate coordinateDown = new Coordinate(e.getX(),e.getY());
        context.moveTo(e.getX(),e.getY());
        coordinate_temp.add(coordinateDown);


    }

    @FXML
    private void canvasMouseMove(MouseEvent e){

        if(mouseState){
            context.lineTo(e.getX(),e.getY());
            context.stroke();
//            coordinate_temp.add(e.getX()+","+e.getY());

            Coordinate coordinateMove = new Coordinate(e.getX(),e.getY());
            coordinate_temp.add(coordinateMove);


        }

    }

    @FXML
    private void canvasMouseUp(){

        coordinate.add(coordinate_temp);
        mouseState = false;
    }

    @FXML
    private void clearCanvas(){

        context_ground.setGlobalBlendMode(BlendMode.SRC_OVER);
        context_ground.clearRect(0,0,700,500);
        context.clearRect(0,0,700,500);

        coordinate = new ArrayList();
        coordinate_temp = new ArrayList();
        shapes = new ArrayList();



    }




    public  void recognize() {
        if(coordinate.size() == 0){
            showDialog(Alert.AlertType.ERROR,"当前画布不存在未识别图形！");
        }else {
            Shape shapeDrawing = new Shape(coordinate);
            coordinate = new ArrayList();
            shapes.add(shapeDrawing);

            Image image_recognized = canvas.snapshot(null,null);

            String typeDrawing = PictureService.recognize(image_recognized);
//        System.out.println(typeDrawing);
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

    public void setRecognition(String type,double x,double y){
//        Label recognition = new Label();
//        recognition.setText(type);
//        recognition.setLayoutX(x);
//        recognition.setLayoutY(y);
//        recognitionBord.getChildren().add(recognition);
        context_ground.fillText(type,x,y);

    }


    public double calculateArea(ArrayList<Integer> x_arr,ArrayList<Integer> y_arr){

        double x1 = x_arr.get(0);
        double y1 = y_arr.get(0);
        double x2 = x_arr.get(1);
        double y2 = y_arr.get(1);
        double x3 = x_arr.get(2);
        double y3 = y_arr.get(2);
        double area = Math.abs((x1*y2+y1*x3+x2*y3)-(x1*y3+y2*x3+y1*x2));
        return area;
    }
    public void changeLayer(Image image){

        Image image_recognized = image;
        context.clearRect(0,0,700,500);

        context_ground.drawImage(image_recognized,0,0);
        context_ground.setGlobalBlendMode(BlendMode.MULTIPLY);
    }


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

        picChoiceBox.getSelectionModel().select(-1);

        showDialog(Alert.AlertType.INFORMATION,"保存成功");

    }

    public List<String> getAllPicId(){
        List picList = PictureService.findAllPicId();
        return picList;
    }

    public void newPic(){

        clearCanvas();
        pictureWorking = null;
//
        List<String> picList = getAllPicId();
        ObservableList picListChoice = FXCollections.observableArrayList(picList);;
        picChoiceBox.setItems(picListChoice);

        picChoiceBox.getSelectionModel().select(-1);
    }


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

    public void drawAllShape(List<Shape> shapeList){
        context_ground.beginPath();
        context.beginPath();
        context_ground.setStroke(Color.rgb(	135,206,235));
        context_ground.setLineWidth(3);
        context.setStroke(Color.rgb(	135,206,235));
        context.setLineWidth(3);
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
//                    System.out.println(coordinateSingle.getCoX()+","+coordinateSingle.getCoY());
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

    public Optional<ButtonType> showDialog(Alert.AlertType type,String message){
        Alert alert = new Alert(type);
        alert.setHeaderText(message);

        return alert.showAndWait();

    }


}
