package Kursach.client.controllers;

import Kursach.client.Polzovatel;
import Kursach.client.SceneManager;
import Kursach.shared.objects.ManufacturerDto;
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

public class ManufacturerController extends AbstractController {
    public Button backButton;
    public TableView<ManufacturerDto> table;
    public TableColumn<ManufacturerDto, String> column;
    public Button deleteButton;
    public Button editButton;
    public Button addButton;
    public Text idText;
    public Text nameText;
    public Text countryText;

    Polzovatel polzovatel;

    List<ManufacturerDto> list = new ArrayList<>();

    @FXML
    void initialize() {
        polzovatel = Polzovatel.getInstance();

        backButton.setOnAction((actionEvent -> {
            SceneManager.getPreviousRoot(backButton.getScene());
        }));



        column.setCellValueFactory(manufacturer -> new SimpleStringProperty(manufacturer.getValue().getName()));

        ObservableList<ManufacturerDto> observableList = FXCollections.observableArrayList(list);
        table.setItems(observableList);

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                onClick();
            }
        });
    }

    @FXML
    void onClick() {
        ManufacturerDto item = table.getSelectionModel().getSelectedItem();

        if (item != null) {
            idText.setText(String.valueOf(item.getId()));
            nameText.setText(item.getName());
        }
    }
}
