package Kursach.client.controllers.admin;

import Kursach.client.Polzovatel;
import Kursach.client.SceneManager;
import Kursach.client.controllers.AbstractController;
import Kursach.client.controllers.ICrudController;
import Kursach.shared.objects.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class OrderController extends AbstractController implements ICrudController {
    public Button backButton;
    public TableView<OrderDto> table;
    public TableColumn<OrderDto, String> column;
    public Button deleteButton;
    public Button editButton;
    public Button addButton;

    public Text idText;
    public Text productNameText;
    public Text amountText;
    public Text clientNameText;
    public Text dateTimeText;

    ObservableList<OrderDto> observableList;

    List<ProductDto> products;
    List<Client> clients;

    @FXML
    private DatePicker datePicker = new DatePicker();

    @FXML
    private Spinner<Integer> hourSpinner = new Spinner<>();

    @FXML
    private Spinner<Integer> minuteSpinner =new Spinner<>();

    @FXML
    void initialize() {
        polzovatel = Polzovatel.getInstance();

        products = (List<ProductDto>) polzovatel.receive(71);
        clients = (List<Client>) polzovatel.receive(41);

        addButton.setOnAction(actionEvent -> onAdd());
        editButton.setOnAction(actionEvent -> onEdit());
        deleteButton.setOnAction(actionEvent -> onDelete());
        backButton.setOnAction((actionEvent -> {
            SceneManager.getPreviousRoot(backButton.getScene());
        }));

        column.setCellValueFactory(category -> new SimpleStringProperty(String.valueOf(category.getValue().getId())));

        observableList = table.getItems();
        getAll();

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                onClick();
            }
        });

        SpinnerValueFactory<Integer> hourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        hourSpinner.setValueFactory(hourFactory);
        hourSpinner.setEditable(true);
        hourSpinner.setPrefWidth(60);
        setTextFieldLimit(hourSpinner.getEditor(), 2);

        SpinnerValueFactory<Integer> minuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        minuteSpinner.setValueFactory(minuteFactory);
        minuteSpinner.setEditable(true);
        minuteSpinner.setPrefWidth(60);
        setTextFieldLimit(minuteSpinner.getEditor(), 2);

        datePicker.setConverter(new StringConverter<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            @Override
            public String toString(LocalDate object) {
                if (object == null) {
                    return "";
                }
                return formatter.format(object);
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return LocalDate.parse(string, formatter);
            }
        });
    }

    @FXML
    void onClick() {
        OrderDto item = (OrderDto) table.getSelectionModel().getSelectedItem();
        if (item != null) {
            idText.setText(String.valueOf(item.getId()));
            productNameText.setText(item.getProduct().getName());
            clientNameText.setText(item.getClient().getName());
            amountText.setText(String.valueOf(item.getAmount()));
            dateTimeText.setText(Order.dateToString(item.getDateTime()));
            enableButtons(true);
            showFields(true);

        }
    }

    @Override
    public void onAdd() {
        Dialog<OrderDto> addDialog = new Dialog<>();
        addDialog.setTitle("Создание заказа");
        addDialog.setHeaderText(null);

        ButtonType addButtonType = new ButtonType("Создать", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
        addDialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        TextField amountTextField = new TextField();
        ChoiceBox<ProductDto> productDtoChoiceBox = new ChoiceBox<>();
        ChoiceBox<Client> clientChoiceBox = new ChoiceBox<>();
        productDtoChoiceBox.getItems().addAll(products);
        clientChoiceBox.getItems().addAll(clients);


        GridPane grid = new GridPane();

        grid.add(new Text("Клиент"), 0, 0);
        grid.add(clientChoiceBox, 1, 0);

        grid.add(new Text("Продукт"), 0, 1);
        grid.add(productDtoChoiceBox, 1, 1);

        grid.add(new Text("Количество"), 0, 2);
        grid.add(amountTextField, 1, 2);


        grid.add(new Text("Дата и Время"), 0, 3);
        grid.add(datePicker, 1, 3);
        grid.add(hourSpinner, 2, 3);
        grid.add(new Text(":"), 3, 3);
        grid.add(minuteSpinner, 4, 3);

        datePicker.setValue(LocalDateTime.now().toLocalDate());
        hourSpinner.getValueFactory().setValue(LocalDateTime.now().getHour());
        minuteSpinner.getValueFactory().setValue(LocalDateTime.now().getMinute());

        addDialog.getDialogPane().setContent(grid);


        addDialog.setResultConverter(button -> {
            if (button == addButtonType) {
                OrderDto order = new OrderDto();

                ProductDto productDto = productDtoChoiceBox.getValue();
                Product selectedProduct = new Product(productDto);
                Client selectedClient = clientChoiceBox.getValue();

                LocalDate selectedDate = datePicker.getValue();
                int selectedHour = hourSpinner.getValue();
                int selectedMinute = minuteSpinner.getValue();

                LocalDateTime selectedDateTime = LocalDateTime.of(selectedDate, LocalTime.of(selectedHour, selectedMinute));

                order.setProduct(selectedProduct);
                order.setClient(selectedClient);
                order.setAmount(Integer.parseInt(amountTextField.getText()));
                order.setDateTime(selectedDateTime);
                return order;
            } else {
                return null;
            }
        });
        Optional<OrderDto> result = addDialog.showAndWait();
        result.ifPresent(order -> {
            polzovatel.send(82);
            polzovatel.send(order);
            getAll();
        });
    }

    @Override
    public void onEdit() {
        OrderDto item = table.getSelectionModel().getSelectedItem();
        Dialog<OrderDto> editDialog = new Dialog<>();
        editDialog.setTitle("Редактирование заказа");
        editDialog.setHeaderText(null);

        ButtonType addButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
        editDialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        TextField amountTextField = new TextField();
        amountTextField.setText(String.valueOf(item.getAmount()));
        ChoiceBox<Product> productChoiceBox = new ChoiceBox<>();
        products.forEach(productDto -> productChoiceBox.getItems().add(new Product(productDto)));
        productChoiceBox.setValue(item.getProduct());
        ChoiceBox<Client> clientChoiceBox = new ChoiceBox<>();
        clientChoiceBox.getItems().addAll(clients);
        clientChoiceBox.setValue(item.getClient());

        GridPane grid = new GridPane();

        grid.add(new Text("Клиент"), 0, 0);
        grid.add(clientChoiceBox, 1, 0);

        grid.add(new Text("Продукт"), 0, 1);
        grid.add(productChoiceBox, 1, 1);

        grid.add(new Text("Количество"), 0, 2);
        grid.add(amountTextField, 1, 2);


        grid.add(new Text("Дата и Время"), 0, 3);
        grid.add(datePicker, 1, 3);
        grid.add(hourSpinner, 2, 3);
        grid.add(new Text(":"), 3, 3);
        grid.add(minuteSpinner, 4, 3);

        datePicker.setValue(item.getDateTime().toLocalDate());
        hourSpinner.getValueFactory().setValue(item.getDateTime().getHour());
        minuteSpinner.getValueFactory().setValue(item.getDateTime().getMinute());

        editDialog.getDialogPane().setContent(grid);

        editDialog.setResultConverter(button -> {
            if (button == addButtonType) {

                Product selectedProduct = productChoiceBox.getValue();
                Client selectedClient = clientChoiceBox.getValue();

                LocalDate selectedDate = datePicker.getValue();
                int selectedHour = hourSpinner.getValue();
                int selectedMinute = minuteSpinner.getValue();

                LocalDateTime selectedDateTime = LocalDateTime.of(selectedDate, LocalTime.of(selectedHour, selectedMinute));

                item.setProduct(selectedProduct);
                item.setClient(selectedClient);
                item.setAmount(Integer.parseInt(amountTextField.getText()));
                item.setDateTime(selectedDateTime);
                return item;
            } else {
                return null;
            }
        });
        Optional<OrderDto> result = editDialog.showAndWait();
        result.ifPresent(order -> {
            polzovatel.send(83);
            polzovatel.send(order);
            getAll();
        });
    }
    @Override
    public void onDelete() {
        OrderDto item = (OrderDto) table.getSelectionModel().getSelectedItem();
        Dialog<OrderDto> deleteDialog = new Dialog<>();
        deleteDialog.setTitle("Удаление заказа");
        deleteDialog.setHeaderText("Вы уверены, что хотите удалить заказ под номером " + item.getId());

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

        Optional<OrderDto> result = deleteDialog.showAndWait();
        result.ifPresent(order -> {
            polzovatel.send(84);
            polzovatel.send(order);
            getAll();
        });

    }

    @Override
    public void getAll() {
        polzovatel.send(81);
        List<OrderDto> orders = (List<OrderDto>) polzovatel.receive();
        observableList.setAll(orders);
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
        productNameText.setVisible(visible);
        clientNameText.setVisible(visible);
        dateTimeText.setVisible(visible);
        amountText.setVisible(visible);
    }

    private void setTextFieldLimit(TextField textField, int maxLength) {
        TextFormatter<Integer> formatter = new TextFormatter<>(
                new IntegerStringConverter(), null,
                change -> {
                    String newText = change.getControlNewText();
                    if (newText.length() <= maxLength && newText.matches("\\d*")) {
                        return change;
                    }
                    return null;
                });
        textField.setTextFormatter(formatter);
    }
}
