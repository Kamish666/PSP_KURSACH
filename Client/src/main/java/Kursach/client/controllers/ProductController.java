package Kursach.client.controllers;

import Kursach.client.Polzovatel;
import Kursach.client.SceneManager;
import Kursach.shared.objects.ProductDto;
import Kursach.shared.objects.Provider;
import com.sun.javafx.menu.MenuItemBase;
import javafx.application.Platform;
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


public class ProductController extends AbstractController{

    public Button backButton;
    public TableView<ProductDto> table;
    public TableColumn<ProductDto, String > column;
    public Button deleteButton;
    public Button editButton;
    public Button addButton;
    public Text idText;
    public Text nameText;
    public Text priceText;
    public Text categoryText;
    public Text manufacturerText;
    public Text providerText;

    Polzovatel polzovatel;

    List<ProductDto> list = new ArrayList<>();

    @FXML
    void initialize() {
        polzovatel = Polzovatel.getInstance();

        backButton.setOnAction((actionEvent -> {
            SceneManager.getPreviousRoot(backButton.getScene());
        }));

        list.add(new ProductDto(234, "Ведьмак 3: Дикая Охота", 69.99, "Игры", "CD Project Red", "Steam"));
        list.add(new ProductDto(123, "Starfield", 12321, "Мусор", "Bethesda Game Studios", "Xbox"));

        column.setCellValueFactory(category -> new SimpleStringProperty(category.getValue().getName()));

        ObservableList<ProductDto> observableList = FXCollections.observableArrayList(list);
        table.setItems(observableList);

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                onClick();
            }
        });
    }

    @FXML
    void onClick() {
        ProductDto item = (ProductDto) table.getSelectionModel().getSelectedItem();

        if (item != null) {
            idText.setText(String.valueOf(item.getId()));
            nameText.setText(item.getName());
            priceText.setText(String.valueOf(item.getPrice()));
            categoryText.setText(item.getCategory());
            manufacturerText.setText(item.getManufacturer());
            providerText.setText(item.getProvider());
        }
    }
}
