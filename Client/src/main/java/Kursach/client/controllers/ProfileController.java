package Kursach.client.controllers;

import Kursach.client.SceneManager;
import Kursach.shared.objects.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ProfileController extends AbstractController{

    public Button saveButton;
    public Button resetButton;
    public Button backButton;
    public TextField nameField;
    public TextField loginField;
    public TextField passwordField;

    User loggedUser = new User();

    @FXML
    void initialize() {
        resetUser();


        backButton.setOnAction((actionEvent -> {
            SceneManager.getPreviousRoot(backButton.getScene());
        }));

        saveButton.setOnAction(actionEvent -> {
            updateUser();
        });

        resetButton.setOnAction(actionEvent -> {
            resetUser();
        });

    }

    void resetUser() {
        loggedUser = getUser();

        nameField.setText(loggedUser.getName());
        loginField.setText(loggedUser.getLogin());
        passwordField.setText(loggedUser.getPassword());
    }

    User getUser() {
        polzovatel.send(91);
        return (User)polzovatel.receive();
    }

    void updateUser() {
        loggedUser.setName(nameField.getText());
        loggedUser.setLogin(loginField.getText());
        loggedUser.setPassword(passwordField.getText());

        polzovatel.send(93);
        polzovatel.send(loggedUser);
        polzovatel.setCurrentUser(loggedUser);
    }

}
