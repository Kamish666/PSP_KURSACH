package com.example.client1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class Vhod {
    @FXML
    private Button logInButton;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text errorMessage;

    @FXML
    void initialize() {
        logInButton.setOnAction(actionEvent -> {
            String regex = "[A-Za-z0-9_]+";
            String login = loginField.getText();
            String password = loginField.getText();
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


        });


    }

    @FXML
    private void handleVhodButtonClick() {
        String enteredLogin = loginField.getText();
        String enteredPassword = passwordField.getText();
        if (enteredLogin.isEmpty() || enteredPassword.isEmpty()) {
            System.out.println("Ошибка: Введите логин и пароль");
        } else {
            System.out.println("Логин: " + enteredLogin);
            System.out.println("Пароль: " + enteredPassword);

            // Здесь можно добавить код для выполнения других действий после успешного ввода
        }
    }
}
