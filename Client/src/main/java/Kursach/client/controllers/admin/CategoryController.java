package Kursach.client.controllers.admin;

import Kursach.client.Polzovatel;
import Kursach.client.SceneManager;
import Kursach.client.controllers.AbstractController;
import Kursach.client.controllers.ICrudController;
import Kursach.shared.objects.Client;
import Kursach.shared.objects.ProductCategory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Optional;

public class CategoryController extends AbstractController implements ICrudController {

    public Button backButton;
    public TableView<ProductCategory> table;
    public TableColumn<ProductCategory, String> column;
    public Button deleteButton;
    public Button editButton;
    public Button addButton;
    public Text idText;
    public Text nameText;
    public Text descText;

    ObservableList<ProductCategory> observableList;

    @FXML
    void initialize() {
        polzovatel = Polzovatel.getInstance();

        backButton.setOnAction((actionEvent -> {
            SceneManager.getPreviousRoot(backButton.getScene());
        }));

        column.setCellValueFactory(category -> new SimpleStringProperty(category.getValue().getCategory()));

        observableList = table.getItems();
        getAll();

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                onClick();
            }
        });

        addButton.setOnAction(actionEvent -> onAdd());
        editButton.setOnAction(actionEvent -> onEdit());
        deleteButton.setOnAction(actionEvent -> onDelete());
    }

    @FXML
    void onClick() {
        ProductCategory item = (ProductCategory) table.getSelectionModel().getSelectedItem();

        if (item != null) {
            idText.setText(String.valueOf(item.getId()));
            nameText.setText(item.getCategory());
            descText.setText(item.getDefinition());
            enableButtons(true);
            showFields(true);

        }
    }

    @Override
    public void onAdd() {
        Dialog<ProductCategory> addDialog = new Dialog<>();
        addDialog.setTitle("Добавление категории");
        addDialog.setHeaderText(null);


        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
        addDialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        TextField nameField = new TextField();
        TextField descField = new TextField();

        GridPane grid = new GridPane();

        grid.add(new Text("Имя категории"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Text("Описание"), 0, 1);
        grid.add(descField, 1, 1);

        addDialog.getDialogPane().setContent(grid);


        addDialog.setResultConverter(button -> {
            if (button == addButtonType) {
                ProductCategory client = new ProductCategory();
                client.setCategory(nameField.getText());
                client.setDefinition(descField.getText());
                return client;
            } else {
                return null;
            }
        });
        Optional<ProductCategory> result = addDialog.showAndWait();
        result.ifPresent(productCategory -> {
            polzovatel.send(32);
            polzovatel.send(productCategory);
            getAll();
        });
    }

    @Override
    public void onEdit() {
        ProductCategory item = (ProductCategory) table.getSelectionModel().getSelectedItem();
        Dialog<ProductCategory> editDialog = new Dialog<>();
        editDialog.setTitle("Редактирование категории");
        editDialog.setHeaderText(null);;

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);

        editDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        TextField nameField = new TextField();
        nameField.setText(item.getCategory());
        TextField descField = new TextField();
        descField.setText(item.getDefinition());

        GridPane grid = new GridPane();

        grid.add(new Text("Имя"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Text("email"), 0, 1);
        grid.add(descField, 1, 1);

        editDialog.getDialogPane().setContent(grid);

        editDialog.setResultConverter(button -> {
            if(button == saveButtonType) {
                item.setCategory(nameField.getText());
                item.setDefinition(descField.getText());
                return item;
            }
            else {
                return null;
            }
        });

        Optional<ProductCategory> result = editDialog.showAndWait();
        result.ifPresent(category -> {
            polzovatel.send(33);
            polzovatel.send(category);
            getAll();
        });

    }
    @Override
    public void onDelete() {
        ProductCategory item = (ProductCategory) table.getSelectionModel().getSelectedItem();
        Dialog<ProductCategory> deleteDialog = new Dialog<>();
        deleteDialog.setTitle("Удаление категории");
        deleteDialog.setHeaderText("Вы уверены, что хотите удалить " + item.getCategory());

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

        Optional<ProductCategory> result = deleteDialog.showAndWait();
        result.ifPresent(category -> {
            polzovatel.send(34);
            polzovatel.send(category);
            getAll();
        });

    }

    @Override
    public void getAll() {
        polzovatel.send(31);
        List<ProductCategory> clients = (List<ProductCategory>) polzovatel.receive();
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
        descText.setVisible(visible);
    }
}
