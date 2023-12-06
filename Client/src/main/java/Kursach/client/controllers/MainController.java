package Kursach.client.controllers;

import Kursach.client.Polzovatel;
import Kursach.client.SceneManager;
import Kursach.shared.objects.User;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.stream.Stream;

public class MainController extends AbstractController{
    public Label labelTitle;
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


    public MainController() {
        polzovatel = new Polzovatel("localhost", 2525);
        try {
            polzovatel.connect();
            Polzovatel.setInstance(polzovatel);
            hasAdmin = (Integer) polzovatel.receive() > 0;
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
                polzovatel.send(name);
            }
            polzovatel.send(login);
            polzovatel.send(password);

            User user = (User)polzovatel.receive();
            if(user == null) {
                errorMessage.setText("Неверный логин или пароль");
                errorMessage.setVisible(true);
            } else {

                polzovatel.setCurrentUser(user);
                Scene current = errorMessage.getScene();
                SceneManager.loadScene(current, "/home-view.fxml");
            }
            System.out.println(user);
        });
    }


}
