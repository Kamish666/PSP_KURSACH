package Kursach.client.controllers.admin;

import Kursach.client.Polzovatel;
import Kursach.client.SceneManager;
import Kursach.client.controllers.AbstractController;
import Kursach.client.controllers.ICrudController;
import Kursach.shared.objects.Client;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ClientController extends AbstractController implements ICrudController {
    public Button backButton;
    public TableView<Client> table;
    public TableColumn<Client, String> column;
    public Button deleteButton;
    public Button editButton;
    public Button addButton;
    public Text idText;
    public Text nameText;

    public Text emailText;

    ObservableList<Client> observableList;

    Polzovatel polzovatel;

    @FXML
    void initialize() {
        polzovatel = Polzovatel.getInstance();


        backButton.setOnAction((actionEvent -> {
            SceneManager.getPreviousRoot(scene);
        }));

        column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        observableList = table.getItems();
        getAll();
        table.setOnMouseClicked(event -> onClick());

        addButton.setOnAction(actionEvent -> onAdd());
        editButton.setOnAction(actionEvent -> onEdit());
        deleteButton.setOnAction(actionEvent -> onDelete());
    }

    @FXML
    void onClick() {
        Client item = (Client) table.getSelectionModel().getSelectedItem();

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
        Dialog<Client> addDialog = new Dialog<>();
        addDialog.setTitle("Добавление клиента");
        addDialog.setHeaderText(null);


        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
        addDialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        TextField nameField = new TextField();
        TextField emailField = new TextField();

        GridPane grid = new GridPane();

        grid.add(new Text("Имя"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Text("email"), 0, 1);
        grid.add(emailField, 1, 1);

        addDialog.getDialogPane().setContent(grid);


        addDialog.setResultConverter(button -> {
            if (button == addButtonType) {
                Client client = new Client();
                client.setName(nameField.getText());
                client.setEmail(emailField.getText());
                return client;
            } else {
                return null;
            }
        });
        Optional<Client> result = addDialog.showAndWait();
        result.ifPresent(client -> {
            polzovatel.send(42);
            polzovatel.send(client);
            getAll();
        });
    }

    @Override
    public void onEdit() {
        Client item = (Client) table.getSelectionModel().getSelectedItem();
        Dialog<Client> editDialog = new Dialog<>();
        editDialog.setTitle("Редактирование клиента");
        editDialog.setHeaderText(null);;

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);

        editDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        TextField nameField = new TextField();
        nameField.setText(item.getName());
        TextField emailField = new TextField();
        emailField.setText(item.getEmail());

        GridPane grid = new GridPane();

        grid.add(new Text("Имя"), 0, 0);
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

        Optional<Client> result = editDialog.showAndWait();
        result.ifPresent(client -> {
            polzovatel.send(43);
            polzovatel.send(client);
            getAll();
        });

    }
    @Override
    public void onDelete() {
        Client item = (Client) table.getSelectionModel().getSelectedItem();
        Dialog<Client> deleteDialog = new Dialog<>();
        deleteDialog.setTitle("Удаление клиента");
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

        Optional<Client> result = deleteDialog.showAndWait();
        result.ifPresent(client -> {
            polzovatel.send(44);
            polzovatel.send(client);
            getAll();
        });

    }

    @Override
    public void getAll() {
        polzovatel.send(41);
        List<Client> clients = (List<Client>) polzovatel.receive();
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
        emailText.setVisible(visible);
    }

}
