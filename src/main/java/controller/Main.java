package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_layout.fxml"));
        Scene scene = new Scene(root, 600, 300);
        scene.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");
        scene.getStylesheets().add("/css/jfoenix-design.css");
        scene.getStylesheets().add("/css/jfoenix-fonts.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
