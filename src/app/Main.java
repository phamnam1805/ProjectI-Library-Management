package app;

import DAO.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try{
            DatabaseConnection.connect();
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("QLTV - Phạm Văn Nam - 20183599");
            primaryStage.setHeight(scene.getWindow().getHeight());
            primaryStage.setWidth(scene.getWindow().getWidth());
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
