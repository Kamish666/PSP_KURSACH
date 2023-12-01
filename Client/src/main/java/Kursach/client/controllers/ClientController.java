package Kursach.client.controllers;

import Kursach.client.Polzovatel;
import Kursach.client.SceneManager;
import Kursach.shared.objects.Client;
import Kursach.shared.objects.Country;
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

public class ClientController extends AbstractController{
    public Button backButton;
    public TableView<Client> table;
    public TableColumn<Client, String> column;
    public Button deleteButton;
    public Button editButton;
    public Button addButton;
    public Text idText;
    public Text nameText;
    public Text emailText;

    List<Client> list = new ArrayList<>();

    Polzovatel polzovatel;

    @FXML
    void initialize() {
        polzovatel = Polzovatel.getInstance();

        backButton.setOnAction((actionEvent -> {
            SceneManager.getPreviousRoot(scene);
        }));

        list.add(new Client(1111, "Кукин Дмитрий Петрович", "kukinthebest3333@gmail.com"));
        list.add(new Client(12321, "Владимир Владмирович", "pubkakapacuk@sobaka.ru"));

        column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        ObservableList<Client> observableList = FXCollections.observableArrayList(list);
        table.setItems(observableList);

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                onClick();
            }
        });
    }

    @FXML
    void onClick() {
        Client item = (Client)table.getSelectionModel().getSelectedItem();

        if (item != null) {
            idText.setText(String.valueOf(item.getId()));
            nameText.setText(item.getName());
            emailText.setText(item.getEmail());
        }
    }
}
