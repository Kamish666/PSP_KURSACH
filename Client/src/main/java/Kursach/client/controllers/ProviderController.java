package Kursach.client.controllers;

import Kursach.client.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ProviderController extends AbstractController{
    public Button backButton;

    @FXML
    void initialize() {
        backButton.setOnAction((actionEvent -> {
            SceneManager.getPreviousRoot(backButton.getScene());
        }));
    }
}
