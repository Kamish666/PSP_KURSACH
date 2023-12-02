package Kursach.client.controllers.admin;

import Kursach.client.Polzovatel;
import Kursach.client.SceneManager;
import Kursach.client.controllers.AbstractController;
import Kursach.client.controllers.ICrudController;
import Kursach.shared.objects.Client;
import Kursach.shared.objects.Provider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProviderController extends AbstractController implements ICrudController {
    public Button backButton;
    public TableView<Provider> table;
    public TableColumn<Provider, String> column;
    public Button deleteButton;
    public Button editButton;
    public Button addButton;

    public Text nameText;
    public Text idText;
    public Text emailText;

    ObservableList<Provider> observableList;

    @FXML
    void initialize() {
        polzovatel = Polzovatel.getInstance();

        backButton.setOnAction((actionEvent -> {
            SceneManager.getPreviousRoot(backButton.getScene());
        }));

        observableList = table.getItems();
        getAll();

        addButton.setOnAction(actionEvent -> onAdd());
        editButton.setOnAction(actionEvent -> onEdit());
        deleteButton.setOnAction(actionEvent -> onDelete());

        column.setCellValueFactory(category -> new SimpleStringProperty(category.getValue().getName()));
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
            enableButtons(true);
            showFields(true);
        }
    }

    @Override
    public void onAdd() {
        Dialog<Provider> addDialog = new Dialog<>();
        addDialog.setTitle("Добавление поставщика");
        addDialog.setHeaderText(null);


        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
        addDialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        TextField nameField = new TextField();
        TextField emailField = new TextField();

        GridPane grid = new GridPane();

        grid.add(new Text("Название"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Text("email"), 0, 1);
        grid.add(emailField, 1, 1);

        addDialog.getDialogPane().setContent(grid);


        addDialog.setResultConverter(button -> {
            if (button == addButtonType) {
                Provider client = new Provider();
                client.setName(nameField.getText());
                client.setEmail(emailField.getText());
                return client;
            } else {
                return null;
            }
        });
        Optional<Provider> result = addDialog.showAndWait();
        result.ifPresent(provider -> {
            polzovatel.send(22);
            polzovatel.send(provider);
            getAll();
        });
    }

    @Override
    public void onEdit() {
        Provider item = (Provider) table.getSelectionModel().getSelectedItem();
        Dialog<Provider> editDialog = new Dialog<>();
        editDialog.setTitle("Редактирование поставщика");
        editDialog.setHeaderText(null);;

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);

        editDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        TextField nameField = new TextField();
        nameField.setText(item.getName());
        TextField emailField = new TextField();
        emailField.setText(item.getEmail());

        GridPane grid = new GridPane();

        grid.add(new Text("Название"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Text("email"), 0, 1);
        grid.add(emailField, 1, 1);

        editDialog.getDialogPane().setContent(grid);

        editDialog.setResultConverter(button -> {
            if(button == saveButtonType) {
                item.setName(nameField.getText());
                item.setEmail(emailField.getText());
                return item;
            }
            else {
                return null;
            }
        });

        Optional<Provider> result = editDialog.showAndWait();
        result.ifPresent(provider -> {
            polzovatel.send(23);
            polzovatel.send(provider);
            getAll();
        });

    }
    @Override
    public void onDelete() {
        Provider item = (Provider) table.getSelectionModel().getSelectedItem();
        Dialog<Provider> deleteDialog = new Dialog<>();
        deleteDialog.setTitle("Удаление поставщика");
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

        Optional<Provider> result = deleteDialog.showAndWait();
        result.ifPresent(provider -> {
            polzovatel.send(24);
            polzovatel.send(provider);
            getAll();
        });

    }

    @Override
    public void getAll() {
        polzovatel.send(21);
        List<Provider> list = (List<Provider>)polzovatel.receive();
        observableList.setAll(list);
        showFields(false);
        enableButtons(false);
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
        emailText.setVisible(visible);
    }
}
