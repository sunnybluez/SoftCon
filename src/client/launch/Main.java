package client.launch;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../presentation/resource/sample.fxml"));
        primaryStage.setTitle("SoftCon");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }


    /**
     * @param args
     *
     * main方法初始化
     */
    public static void main(String[] args) {
        launch(args);
    }
}
