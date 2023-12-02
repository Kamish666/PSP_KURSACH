package Kursach.client.controllers.admin;

import Kursach.client.Polzovatel;
import Kursach.client.SceneManager;
import Kursach.client.controllers.AbstractController;
import Kursach.client.controllers.ICrudController;
import Kursach.shared.objects.Country;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Optional;

public class CountryController extends AbstractController implements ICrudController {
    public Button backButton;
    public TableView<Country> table;
    public TableColumn<Country, String> column;
    public Button deleteButton;
    public Button editButton;
    public Button addButton;
    public Text idText;
    public Text nameText;


    ObservableList<Country> observableList;

    Polzovatel client;

    @FXML
    void initialize() {
        client = Polzovatel.getInstance();

        observableList = table.getItems();
        getAll();


        backButton.setOnAction((actionEvent -> {
            SceneManager.getPreviousRoot(scene);
        }));

        column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry()));

        addButton.setOnAction(actionEvent -> onAdd());
        editButton.setOnAction(actionEvent -> onEdit());
        deleteButton.setOnAction(actionEvent -> onDelete());

        table.setOnMouseClicked(event -> onClick());

    }

    @FXML
    void onClick() {
        Country item = (Country) table.getSelectionModel().getSelectedItem();

        if (item != null) {
            idText.setText(String.valueOf(item.getId()));
            nameText.setText(item.getCountry());
            enableButtons(true);
            showFields(true);
        }
    }

    @Override
    public void onAdd() {
        Dialog<Country> addDialog = new Dialog<>();
        addDialog.setTitle("Добавление клиента");
        addDialog.setHeaderText(null);


        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
        addDialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        TextField nameField = new TextField();

        GridPane grid = new GridPane();

        grid.add(new Text("Название"), 0, 0);
        grid.add(nameField, 1, 0);


        addDialog.getDialogPane().setContent(grid);


        addDialog.setResultConverter(button -> {
            if (button == addButtonType) {
                Country country = new Country();
                country.setCountry(nameField.getText());
                return country;
            } else {
                return null;
            }
        });
        Optional<Country> result = addDialog.showAndWait();
        result.ifPresent(country -> {
            polzovatel.send(12);
            polzovatel.send(country);
            getAll();
        });
    }

    @Override
    public void onEdit() {
        Country item = (Country) table.getSelectionModel().getSelectedItem();
        Dialog<Country> editDialog = new Dialog<>();
        editDialog.setTitle("Редактирование клиента");
        editDialog.setHeaderText(null);
        ;

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);

        editDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        TextField nameField = new TextField();
        nameField.setText(item.getCountry());

        GridPane grid = new GridPane();

        grid.add(new Text("Название"), 0, 0);
        grid.add(nameField, 1, 0);

        editDialog.getDialogPane().setContent(grid);

        editDialog.setResultConverter(button -> {
            if (button == saveButtonType) {
                item.setCountry(nameField.getText());
                return item;
            } else {
                return null;
            }
        });

        Optional<Country> result = editDialog.showAndWait();
        result.ifPresent(country -> {
            polzovatel.send(13);
            polzovatel.send(country);
            getAll();
        });
    }

    @Override
    public void onDelete() {
        Country item = (Country) table.getSelectionModel().getSelectedItem();
        Dialog<Country> deleteDialog = new Dialog<>();
        deleteDialog.setTitle("Удаление страны");
        deleteDialog.setHeaderText("Вы уверены, что хотите удалить " + item.getCountry());

        ButtonType deleteButtonType = new ButtonType("Удалить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);

        deleteDialog.getDialogPane().getButtonTypes().addAll(deleteButtonType, cancelButtonType);

        deleteDialog.setResultConverter(button -> {
            if (button == deleteButtonType) {
                return item;
            } else {
                return null;
            }
        });

        Optional<Country> result = deleteDialog.showAndWait();
        result.ifPresent(client -> {
            polzovatel.send(14);
            polzovatel.send(client);
            getAll();
        });
    }

    @Override
    public void getAll() {
        polzovatel.send(11);
        List<Country> countries = (List<Country>) polzovatel.receive();
        observableList.setAll(countries);
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
    }

}
