package Kursach.client.controllers.admin;

import Kursach.client.Polzovatel;
import Kursach.client.SceneManager;
import Kursach.client.controllers.AbstractController;
import Kursach.client.controllers.ICrudController;
import Kursach.shared.objects.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ProductController extends AbstractController implements ICrudController {

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

    ObservableList<ProductDto> observableList;

    List<ProductCategory> categories;
    List<ManufacturerDto> manufactures;
    List<Provider> providers;

    @FXML
    void initialize() {
        polzovatel = Polzovatel.getInstance();

        backButton.setOnAction((actionEvent -> {
            SceneManager.getPreviousRoot(backButton.getScene());
        }));

        categories = (List<ProductCategory>) polzovatel.receive(31);
        manufactures = (List<ManufacturerDto>) polzovatel.receive(61);
        providers = (List<Provider>) polzovatel.receive(21);

        column.setCellValueFactory(category -> new SimpleStringProperty(category.getValue().getName()));

        observableList = table.getItems();
        getAll();

        addButton.setOnAction(actionEvent -> onAdd());
        editButton.setOnAction(actionEvent -> onEdit());
        deleteButton.setOnAction(actionEvent -> onDelete());

        table.setOnMouseClicked(event -> onClick());
    }

    @FXML
    void onClick() {
        ProductDto item = (ProductDto) table.getSelectionModel().getSelectedItem();
        if (item != null) {
            idText.setText(String.valueOf(item.getId()));
            nameText.setText(item.getName());
            categoryText.setText(item.getCategory().getCategory());
            manufacturerText.setText(item.getManufacturer().getName());
            providerText.setText(item.getProvider().getName());
            enableButtons(true);
            showFields(true);

        }
    }

    @Override
    public void onAdd() {
        Dialog<ProductDto> addDialog = new Dialog<>();
        addDialog.setTitle("Добавление продукта");
        addDialog.setHeaderText(null);


        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
        addDialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        TextField nameField = new TextField();
        ChoiceBox<ProductCategory> categoryChoiceBox = new ChoiceBox<>();
        ChoiceBox<ManufacturerDto> manufacturerChoiceBox = new ChoiceBox<>();
        ChoiceBox<Provider> providerChoiceBox = new ChoiceBox<>();
        categoryChoiceBox.getItems().addAll(categories);
        manufacturerChoiceBox.getItems().addAll(manufactures);
        providerChoiceBox.getItems().addAll(providers);

        GridPane grid = new GridPane();

        grid.add(new Text("Название продукта"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Text("Категория"), 0, 1);
        grid.add(categoryChoiceBox, 1, 1);

        grid.add(new Text("Производитель"), 0, 2);
        grid.add(manufacturerChoiceBox, 1, 2);

        grid.add(new Text("Поставщик"), 0, 3);
        grid.add(providerChoiceBox, 1, 3);

        addDialog.getDialogPane().setContent(grid);


        addDialog.setResultConverter(button -> {
            if (button == addButtonType) {
                ProductDto product = new ProductDto();
                ProductCategory selectedCategory = categoryChoiceBox.getValue();
                Provider selectedProvider = providerChoiceBox.getValue();

                ManufacturerDto manufacturerDto = manufacturerChoiceBox.getValue();
                Manufacturer selectedManufacturer = new Manufacturer(manufacturerDto.getId(), manufacturerDto.getName(), manufacturerDto.getCountry().getId());

                product.setName(nameField.getText());
                product.setCategory(selectedCategory);
                product.setManufacturer(selectedManufacturer);
                product.setProvider(selectedProvider);
                return product;
            } else {
                return null;
            }
        });
        Optional<ProductDto> result = addDialog.showAndWait();
        result.ifPresent(product -> {
            polzovatel.send(72);
            polzovatel.send(product);
            getAll();
        });
    }

    @Override
    public void onEdit() {
        ProductDto item = (ProductDto) table.getSelectionModel().getSelectedItem();
        Dialog<ProductDto> editDialog = new Dialog<>();
        editDialog.setTitle("Редактирование продукта");
        editDialog.setHeaderText(null);;

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);

        editDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        TextField nameField = new TextField();
        ChoiceBox<ProductCategory> categoryChoiceBox = new ChoiceBox<>();
        ChoiceBox<ManufacturerDto> manufacturerChoiceBox = new ChoiceBox<>();
        ChoiceBox<Provider> providerChoiceBox = new ChoiceBox<>();
        categoryChoiceBox.getItems().addAll(categories);
        manufacturerChoiceBox.getItems().addAll(manufactures);
        providerChoiceBox.getItems().addAll(providers);

        GridPane grid = new GridPane();

        grid.add(new Text("Название продукта"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Text("Категория"), 0, 1);
        grid.add(categoryChoiceBox, 1, 1);

        grid.add(new Text("Производитель"), 0, 2);
        grid.add(manufacturerChoiceBox, 1, 2);

        grid.add(new Text("Поставщик"), 0, 3);
        grid.add(providerChoiceBox, 1, 3);

        editDialog.getDialogPane().setContent(grid);

        editDialog.setResultConverter(button -> {
            if(button == saveButtonType) {
                ManufacturerDto manufacturerDto = manufacturerChoiceBox.getValue();
                Manufacturer selectedManufacturer = new Manufacturer(manufacturerDto.getId(), manufacturerDto.getName(), manufacturerDto.getCountry().getId());

                item.setName(nameField.getText());
                item.setProvider(providerChoiceBox.getValue());
                item.setCategory(categoryChoiceBox.getValue());
                item.setManufacturer(selectedManufacturer);

                return item;
            }
            else {
                return null;
            }
        });

        Optional<ProductDto> result = editDialog.showAndWait();
        result.ifPresent(product -> {
            polzovatel.send(73);
            polzovatel.send(product);
            getAll();
        });

    }
    @Override
    public void onDelete() {
        ProductDto item = (ProductDto) table.getSelectionModel().getSelectedItem();
        Dialog<ProductDto> deleteDialog = new Dialog<>();
        deleteDialog.setTitle("Удаление продукта");
        deleteDialog.setHeaderText("Вы уверены, что хотите удалить " + item.getName());

        ButtonType deleteButtonType = new ButtonType("Удалить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);

        deleteDialog.getDialogPane().getButtonTypes().addAll(deleteButtonType, cancelButtonType);

        deleteDialog.setResultConverter(button -> {
            if(button == deleteButtonType) {
                return item;
            }
            else {
                return null;
            }
        });

        Optional<ProductDto> result = deleteDialog.showAndWait();
        result.ifPresent(manufacturer -> {
            polzovatel.send(74);
            polzovatel.send(manufacturer);
            getAll();
        });

    }

    @Override
    public void getAll() {
        polzovatel.send(71);
        List<ProductDto> clients = (List<ProductDto>) polzovatel.receive();
        observableList.setAll(clients);
        enableButtons(false);
        showFields(false);
    }


    @Override
    public void enableButtons(boolean enable) {
        editButton.setDisable(!enable);
        deleteButton.setDisable(!enable);
    }

    @Override
    public void showFields(boolean visible) {
        idText.setVisible(visible);
        nameText.setVisible(visible);
        manufacturerText.setVisible(visible);
        providerText.setVisible(visible);
        categoryText.setVisible(visible);
    }
}
