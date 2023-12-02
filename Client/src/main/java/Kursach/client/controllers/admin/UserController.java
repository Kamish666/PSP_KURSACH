package Kursach.client.controllers.admin;

import Kursach.client.SceneManager;
import Kursach.client.controllers.AbstractController;
import Kursach.client.controllers.ICrudController;
import Kursach.shared.objects.Client;
import Kursach.shared.objects.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserController extends AbstractController implements ICrudController {

    public TableView<User> table;
    public TableColumn<User, String> column;

    public Button deleteButton;
    public Button backButton;
    public Button editButton;
    public Button addButton;
    public Text idText;
    public Text nameText;
    public Text loginText;
    public Text passwordText;
    public Text roleText;


    ObservableList<User> observableList;

    @FXML
    void initialize() {
        backButton.setOnAction(actionEvent -> SceneManager.getPreviousRoot(scene));
        addButton.setOnAction(actionEvent -> onAdd());
        editButton.setOnAction(actionEvent -> onEdit());
        deleteButton.setOnAction(actionEvent -> onDelete());

        column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        observableList = table.getItems();
        getAll();
        table.setOnMouseClicked(event -> onClick());


    }

    private void onClick() {
        User item = (User) table.getSelectionModel().getSelectedItem();

        if (item != null) {
            idText.setText(String.valueOf(item.getId()));
            nameText.setText(item.getName());
            loginText.setText(item.getLogin());
            passwordText.setText(item.getPassword());
            roleText.setText(item.getRole() == 1 ? "Пользователь" : "Администратор");
            enableButtons(true);
            showFields(true);
        }
    }

    @Override
    public void onAdd() {
        Dialog<User> addDialog = new Dialog<>();
        addDialog.setTitle("Добавление пользователя");
        addDialog.setHeaderText(null);

        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
        addDialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        TextField nameField = new TextField();
        TextField loginField = new TextField();
        TextField passwordField = new TextField();
        ChoiceBox<String> roleChoiceBox = new ChoiceBox<>();
        roleChoiceBox.getItems().addAll(Arrays.asList("Пользователь", "Администратор"));


        GridPane grid = new GridPane();

        grid.add(new Text("Имя"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Text("Логин"), 0, 1);
        grid.add(loginField, 1, 1);

        grid.add(new Text("Пароль"), 0, 2);
        grid.add(passwordField, 1, 2);

        grid.add(new Text("Роль"), 0, 3);
        grid.add(roleChoiceBox, 1, 3);

        addDialog.getDialogPane().setContent(grid);


        addDialog.setResultConverter(button -> {
            if (button == addButtonType) {
                User user = new User();
                user.setName(nameField.getText());
                user.setLogin(loginField.getText());
                user.setPassword(passwordField.getText());
                user.setRole(roleChoiceBox.getValue() == "Пользователь" ? 1 : 2);
                return user;
            } else {
                return null;
            }
        });
        Optional<User> result = addDialog.showAndWait();
        result.ifPresent(user -> {
            polzovatel.send(52);
            polzovatel.send(user);
            getAll();
        });
    }

    @Override
    public void onEdit() {
        User item = table.getSelectionModel().getSelectedItem();
        Dialog<User> edit = new Dialog<>();
        edit.setTitle("Редактирование пользователя");
        edit.setHeaderText(null);

        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
        edit.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        TextField nameField = new TextField();
        nameField.setText(item.getName());
        TextField loginField = new TextField();
        loginField.setText(item.getLogin());
        TextField passwordField = new TextField();
        passwordField.setText(item.getPassword());
        ChoiceBox<String> roleChoiceBox = new ChoiceBox<>();
        roleChoiceBox.getItems().addAll(Arrays.asList("Пользователь", "Администратор"));
        roleChoiceBox.setValue(item.getRole() == 1 ? "Пользователь" : "Администратор");


        GridPane grid = new GridPane();

        grid.add(new Text("Имя"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Text("Логин"), 0, 1);
        grid.add(loginField, 1, 1);

        grid.add(new Text("Пароль"), 0, 2);
        grid.add(passwordField, 1, 2);

        grid.add(new Text("Роль"), 0, 3);
        grid.add(roleChoiceBox, 1, 3);

        edit.getDialogPane().setContent(grid);


        edit.setResultConverter(button -> {
            if (button == addButtonType) {
                item.setName(nameField.getText());
                item.setLogin(loginField.getText());
                item.setPassword(passwordField.getText());
                item.setRole(roleChoiceBox.getValue() == "Пользователь" ? 1 : 2);
                return item;
            } else {
                return null;
            }
        });
        Optional<User> result = edit.showAndWait();
        result.ifPresent(user -> {
            polzovatel.send(53);
            polzovatel.send(user);
            getAll();
        });
    }

    @Override
    public void onDelete() {
        User item = (User) table.getSelectionModel().getSelectedItem();
        Dialog<User> deleteDialog = new Dialog<>();
        deleteDialog.setTitle("Удаление пользователя");
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

        Optional<User> result = deleteDialog.showAndWait();
        result.ifPresent(client -> {
            polzovatel.send(54);
            polzovatel.send(client);
            getAll();
        });
    }

    @Override
    public void getAll() {
        polzovatel.send(51);
        List<User> users = (List<User>) polzovatel.receive();
        observableList.setAll(users);
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
        loginText.setVisible(visible);
        passwordText.setVisible(visible);
        roleText.setVisible(visible);

    }
}
