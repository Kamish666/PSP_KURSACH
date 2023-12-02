package Kursach.client.controllers.admin;

import Kursach.client.Polzovatel;
import Kursach.client.SceneManager;
import Kursach.client.controllers.AbstractController;
import Kursach.client.controllers.ICrudController;
import Kursach.shared.objects.Country;
import Kursach.shared.objects.ManufacturerDto;
import Kursach.shared.objects.ProductCategory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Optional;

public class ManufacturerController extends AbstractController implements ICrudController {
    public Button backButton;
    public TableView<ManufacturerDto> table;
    public TableColumn<ManufacturerDto, String> column;
    public Button deleteButton;
    public Button editButton;
    public Button addButton;
    public Text idText;
    public Text nameText;
    public Text countryNameText;

    Polzovatel polzovatel;

    ObservableList<ManufacturerDto> observableList;

    List<Country> countries;

    @FXML
    void initialize() {
        polzovatel = Polzovatel.getInstance();

        backButton.setOnAction((actionEvent -> {
            SceneManager.getPreviousRoot(backButton.getScene());
        }));


        column.setCellValueFactory(manufacturer -> new SimpleStringProperty(manufacturer.getValue().getName()));

        observableList = table.getItems();
        countries = (List<Country>) polzovatel.receive(11);
        getAll();

        table.setOnMouseClicked(event -> onClick());

        addButton.setOnAction(actionEvent -> onAdd());
        editButton.setOnAction(actionEvent -> onEdit());
        deleteButton.setOnAction(actionEvent -> onDelete());
    }

    @FXML
    void onClick() {
        ManufacturerDto item = (ManufacturerDto) table.getSelectionModel().getSelectedItem();

        if (item != null) {
            idText.setText(String.valueOf(item.getId()));
            nameText.setText(item.getName());
            countryNameText.setText(item.getCountryName());
            enableButtons(true);
            showFields(true);

        }
    }

    @Override
    public void onAdd() {
        Dialog<ManufacturerDto> addDialog = new Dialog<>();
        addDialog.setTitle("Добавление производителя");
        addDialog.setHeaderText(null);


        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
        addDialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        TextField nameField = new TextField();
        ChoiceBox<Country> countryChoiceBox = new ChoiceBox<>();
        countryChoiceBox.getItems().addAll(countries);


        GridPane grid = new GridPane();

        grid.add(new Text("Название организации"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Text("Страна"), 0, 1);
        grid.add(countryChoiceBox, 1, 1);

        addDialog.getDialogPane().setContent(grid);


        addDialog.setResultConverter(button -> {
            if (button == addButtonType) {
                ManufacturerDto manufacturer = new ManufacturerDto();
                Country selectedCountry = countryChoiceBox.getValue();
                System.out.println(selectedCountry);
                manufacturer.setName(nameField.getText());
                manufacturer.setCountry(selectedCountry);
                return manufacturer;
            } else {
                return null;
            }
        });
        Optional<ManufacturerDto> result = addDialog.showAndWait();
        result.ifPresent(manufacturer -> {
            polzovatel.send(62);
            polzovatel.send(manufacturer);
            getAll();
        });
    }

    @Override
    public void onEdit() {
        ManufacturerDto item = (ManufacturerDto) table.getSelectionModel().getSelectedItem();
        Dialog<ManufacturerDto> editDialog = new Dialog<>();
        editDialog.setTitle("Редактирование производителя");
        editDialog.setHeaderText(null);;

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);

        editDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        TextField nameField = new TextField();
        ChoiceBox<Country> countryChoiceBox = new ChoiceBox<>();
        countryChoiceBox.getItems().addAll(countries);

        GridPane grid = new GridPane();

        grid.add(new Text("Название организации"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Text("Страна"), 0, 1);
        grid.add(countryChoiceBox, 1, 1);

        editDialog.getDialogPane().setContent(grid);

        editDialog.setResultConverter(button -> {
            if(button == saveButtonType) {
                item.setName(nameField.getText());
                item.setCountry(countryChoiceBox.getValue());
                return item;
            }
            else {
                return null;
            }
        });

        Optional<ManufacturerDto> result = editDialog.showAndWait();
        result.ifPresent(category -> {
            polzovatel.send(63);
            polzovatel.send(category);
            getAll();
        });

    }
    @Override
    public void onDelete() {
        ManufacturerDto item = (ManufacturerDto) table.getSelectionModel().getSelectedItem();
        Dialog<ManufacturerDto> deleteDialog = new Dialog<>();
        deleteDialog.setTitle("Удаление производителя");
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

        Optional<ManufacturerDto> result = deleteDialog.showAndWait();
        result.ifPresent(manufacturer -> {
            polzovatel.send(64);
            polzovatel.send(manufacturer);
            getAll();
        });

    }

    @Override
    public void getAll() {
        polzovatel.send(61);
        List<ManufacturerDto> clients = (List<ManufacturerDto>) polzovatel.receive();
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
        countryNameText.setVisible(visible);
    }
}
