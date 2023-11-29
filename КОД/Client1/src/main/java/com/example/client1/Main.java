package com.example.client1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Main extends Application {

    public static void main(String[] args) {
        try {

            Client client = new Client("localhost", 2525);
            client.connect();
            Integer count = client.receiveInt();
            System.out.println(count);

            if(true) return;


            System.out.println("tcp_ip_5.Server connecting...");
            Socket clientSocket = new Socket("127.0.0.1", 2525);
            System.out.println("Connection established...");

            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            ObjectOutputStream coos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream cois = new ObjectInputStream(clientSocket.getInputStream());

            int userIs = cois.readInt();
            System.out.println(userIs);
            if (userIs == 0) {
                System.out.println("нет пользователей");
            } else {
                launch(args);
            }

            coos.close();
            cois.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vhod.fxml"));
        loader.load();
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Your Title");
        primaryStage.setScene(scene);
        Vhod controller = loader.getController();
        primaryStage.show();
    }
}
