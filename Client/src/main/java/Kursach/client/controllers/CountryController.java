package Kursach.client.controllers;

import Kursach.client.Polzovatel;
import Kursach.client.SceneManager;
import Kursach.shared.objects.Country;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CountryController extends AbstractController{
    public Button backButton;
    public TableView<Country> table;
    public TableColumn<Country,String> column;
    public Button deleteButton;
    public Button editButton;
    public Button addButton;
    public Text idText;
    public Text nameText;

    @FXML
    void onClick(String name) {

    }

    List<Country> list = new ArrayList<>();

    Polzovatel client;

    @FXML
    void initialize() {
        client = Polzovatel.getInstance();

        backButton.setOnAction((actionEvent -> {
            SceneManager.getPreviousRoot(scene);
        }));

        list = getList();
        list = new ArrayList<>();
        list.add(new Country(1, "Беларусь"));
        list.add(new Country(2, "Россия"));

        column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry()));

        ObservableList<Country> observableList = FXCollections.observableArrayList(list);
        table.setItems(observableList);

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                onClick();
            }
        });
    }

    @FXML
    void onClick() {
        Country selectedCountry = (Country)table.getSelectionModel().getSelectedItem();

        if (selectedCountry != null) {
            idText.setText(String.valueOf(selectedCountry.getId()));
            nameText.setText(selectedCountry.getCountry());
        }
    }

    public List<Country> getList() {
        try {
            client.sendInt(11);
            Object result = client.receive();
            System.out.println(result);
            if (result.getClass().equals(list.getClass())) {
                return (List<Country>)result;
            }
            else throw new Exception("не ожидал" + result.getClass());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
