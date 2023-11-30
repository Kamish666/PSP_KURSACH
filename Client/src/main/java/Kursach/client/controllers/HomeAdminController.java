package Kursach.client.controllers;

import Kursach.client.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HomeAdminController extends AbstractController{

    @FXML
    private Button exitButton;

    @FXML
    private Label menuTitle;
    @FXML
    private Button categoryButton;

    @FXML
    private Button clientButton;

    @FXML
    private Button manufacturerButton;

    @FXML
    private Button orderButton;

    @FXML
    private Button productButton;

    @FXML
    private Button providerButton;

    @FXML
    private Button countryButton;

    @FXML
    void initialize() {

        exitButton.setOnAction((actionEvent -> {
            Platform.exit();
        }));


        clientButton.setOnAction((actionEvent -> {
            SceneManager.loadScene(scene, "/client-view.fxml");
        }));
        orderButton.setOnAction(actionEvent -> {
            SceneManager.loadScene(scene, "/order-view.fxml");
        });
        productButton.setOnAction(actionEvent -> {
            SceneManager.loadScene(scene, "/product-view.fxml");
        });
        categoryButton.setOnAction(actionEvent -> {
            SceneManager.loadScene(scene, "/category-view.fxml");
        });
        manufacturerButton.setOnAction(actionEvent -> {
            SceneManager.loadScene(scene, "/manufacturer-view.fxml");
        });
        providerButton.setOnAction(actionEvent -> {
            SceneManager.loadScene(scene, "/provider-view.fxml");
        });
        countryButton.setOnAction(actionEvent -> {
            SceneManager.loadScene(scene, "/country-view.fxml");
        });


    }

}
