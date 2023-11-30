package Kursach.client.controllers;

import Kursach.client.Client;
import Kursach.client.Main;
import Kursach.shared.objects.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class MainController {
    @FXML
    private Button logInButton;
    @FXML
    private TextField loginField;
    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text errorMessage;

    boolean hasAdmin;

    Client client;

    @FXML
    void initialize() {
        if(!hasAdmin) {
            nameField.setVisible(true);
        }

        String regex = "[A-Za-z0-9_]+";
        logInButton.setOnAction(actionEvent -> {
            String login = loginField.getText();
            String password = loginField.getText();
            String name = nameField.getText();
            if (!login.matches(regex)) {
                errorMessage.setText("Некорректный логин");
                errorMessage.setVisible(true);
                return;
            }
            if (!password.matches(regex)) {
                errorMessage.setText("Некорректный пароль");
                errorMessage.setVisible(true);
                return;
            }
            if(!hasAdmin && name.length() < 1) {
                errorMessage.setText("Некорректный логин");
                errorMessage.setVisible(true);
                return;
            }
            if(!hasAdmin) {
                client.send(name);
            }
            client.send(login);
            client.send(password);


            System.out.println("sent data");
            User user = (User)client.receive();
            if(user != null) {

            } else {

            }
            System.out.println(user);

            errorMessage.setOnKeyTyped(keyEvent -> {
                errorMessage.setVisible(false);
            });
        });

    }

    public MainController() {
        client = new Client("localhost", 2525);
        try {
            client.connect();
            hasAdmin = client.receiveInt() > 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
