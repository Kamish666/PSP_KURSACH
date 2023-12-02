package Kursach.client.controllers;

import Kursach.client.Polzovatel;
import Kursach.client.SceneManager;
import Kursach.shared.objects.Order;
import Kursach.shared.objects.OrderDto;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class OrderController extends AbstractController {
    public Button backButton;
    public TableView<OrderDto> table;
    public TableColumn<OrderDto, String> column;
    public Button deleteButton;
    public Button editButton;
    public Button addButton;
    public Text idText;
    public Text productTest;
    public Text clientText;
    public Text dateText;
    public Text amountText;

    Polzovatel polzovatel;

    List<OrderDto> list = new ArrayList<>();

    @FXML
    void initialize() {
        polzovatel = Polzovatel.getInstance();

        backButton.setOnAction((actionEvent -> {
            SceneManager.getPreviousRoot(backButton.getScene());
        }));


        column.setCellValueFactory(category -> new SimpleStringProperty(String.valueOf(category.getValue().getId())));

        ObservableList<OrderDto> observableList = FXCollections.observableArrayList(list);
        table.setItems(observableList);

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                onClick();
            }
        });
    }

    @FXML
    void onClick() {

    }

}
