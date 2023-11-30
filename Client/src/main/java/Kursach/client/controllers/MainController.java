package Kursach.client.controllers;

import Kursach.client.Polzovatel;
import Kursach.client.SceneManager;
import Kursach.shared.objects.User;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.stream.Stream;

public class MainController extends AbstractController{
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

    Polzovatel client;

    public MainController() {
        client = new Polzovatel("localhost", 2525);
        try {
            client.connect();
            Polzovatel.setInstance(client);
            hasAdmin = client.receiveInt() > 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void initialize() {

        if(!hasAdmin) {
            nameField.setVisible(true);
            logInButton.setText("Регистрация");
        }
        else {
            nameField.setVisible(false);
            logInButton.setText("Вход");
        }

        errorMessage.setOnKeyTyped(keyEvent -> {
            errorMessage.setVisible(false);
        });

        Stream.of(nameField, loginField, passwordField)
                        .forEach(textField -> {
                            textField.setOnKeyTyped((keyEvent) -> {
                                errorMessage.setVisible(false);
                                errorMessage.setFill(Color.RED);
                            });
                        });

        String regex = "[A-Za-z0-9_]+";
        logInButton.setOnAction(actionEvent -> {
            String login = loginField.getText();
            String password = passwordField.getText();
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
            if(user == null) {
                errorMessage.setText("Неверный логин или пароль");
                errorMessage.setVisible(true);
            } else {
                errorMessage.setText("Вхожу!!1");
                errorMessage.setFill(Color.GREEN);
                errorMessage.setVisible(true);

                Scene current = errorMessage.getScene();
                SceneManager.loadScene(current, "/home-admin-view.fxml");
            }
            System.out.println(user);
        });
    }


}
