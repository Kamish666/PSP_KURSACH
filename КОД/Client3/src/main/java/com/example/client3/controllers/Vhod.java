package com.example.client3.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Vhod {
    @FXML
    private Button vhod;
    @FXML
    private TextField login;
    @FXML
    private PasswordField pasword;

    @FXML
    private void handleVhodButtonClick() {
        String enteredLogin = login.getText();
        String enteredPassword = pasword.getText();
        if (enteredLogin.isEmpty() || enteredPassword.isEmpty()) {
            System.out.println("Ошибка: Введите логин и пароль");
        } else {
            System.out.println("Логин: " + enteredLogin);
            System.out.println("Пароль: " + enteredPassword);

            // Здесь можно добавить код для выполнения других действий после успешного ввода
        }
    }
}
