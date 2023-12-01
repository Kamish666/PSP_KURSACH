package Kursach.client.controllers;

import Kursach.client.Polzovatel;
import Kursach.client.SceneManager;
import Kursach.shared.objects.ProductCategory;
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

public class CategoryController extends AbstractController{

    public Button backButton;
    public TableView<ProductCategory> table;
    public TableColumn<ProductCategory, String> column;
    public Button deleteButton;
    public Button editButton;
    public Button addButton;
    public Text idText;
    public Text nameText;
    public Text descText;

    List<ProductCategory> list = new ArrayList<>();
    Polzovatel polzovatel;

    @FXML
    void initialize() {
        polzovatel = Polzovatel.getInstance();

        backButton.setOnAction((actionEvent -> {
            SceneManager.getPreviousRoot(backButton.getScene());
        }));

        list.add(new ProductCategory(1, "Мясо", "Продукт мясосодержащий"));
        list.add(new ProductCategory(25, "Порнография", "Студент пишет курсовую работу. ЖЕСТЬ!!!"));

        column.setCellValueFactory(category -> new SimpleStringProperty(category.getValue().getCategory()));

        ObservableList<ProductCategory> observableList = FXCollections.observableArrayList(list);
        table.setItems(observableList);

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                onClick();
            }
        });
    }

    @FXML
    void onClick() {
        ProductCategory item = (ProductCategory) table.getSelectionModel().getSelectedItem();

        if (item != null) {
            idText.setText(String.valueOf(item.getId()));
            nameText.setText(item.getCategory());
            descText.setText(item.getDefinition());
        }
    }
}
