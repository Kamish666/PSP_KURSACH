package Kursach.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;

public class Main extends Application {

    public static void main(String[] args) {
        System.out.println("started client");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL url = Main.class.getResource("/main-view.fxml");
        System.out.println(url);
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/mаin-view.fxml"));
//        Parent root = loader.load();
//        Scene scene = new Scene(root);
//        primaryStage.setTitle("Your Title");
//        primaryStage.setScene(scene);
//        primaryStage.show();

        primaryStage.setScene(new Scene(loader.getRoot()));
        primaryStage.show();
        System.out.println("showing scene");
    }
}
