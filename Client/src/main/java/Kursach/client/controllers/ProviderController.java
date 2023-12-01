package Kursach.client.controllers;

import Kursach.client.Polzovatel;
import Kursach.client.SceneManager;
import Kursach.shared.objects.Provider;
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

public class ProviderController extends AbstractController{
    public Button backButton;
    public TableView<Provider> table;
    public TableColumn<Provider, String> column;
    public Button deleteButton;
    public Button editButton;
    public Button addButton;
    public Text nameText;
    public Text idText;
    public Text emailText;
    Polzovatel polzovatel;

    List<Provider> list = new ArrayList<>();

    @FXML
    void initialize() {
        polzovatel = Polzovatel.getInstance();

        backButton.setOnAction((actionEvent -> {
            SceneManager.getPreviousRoot(backButton.getScene());
        }));

        list.add(new Provider(69, "Поставщик кала", "kukin@bsuir.by"));
        list.add(new Provider(534, "Поставщик софта", "vadimsun@gmail.com"));

        column.setCellValueFactory(category -> new SimpleStringProperty(category.getValue().getName()));

        ObservableList<Provider> observableList = FXCollections.observableArrayList(list);
        table.setItems(observableList);

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                onClick();
            }
        });
    }

    @FXML
    void onClick() {
        Provider item = (Provider) table.getSelectionModel().getSelectedItem();

        if (item != null) {
            idText.setText(String.valueOf(item.getId()));
            nameText.setText(item.getName());
            emailText.setText(item.getEmail());
        }
    }


}
