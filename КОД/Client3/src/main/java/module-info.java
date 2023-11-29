module com.example.client3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.client3 to javafx.fxml;
    exports com.example.client3;
}