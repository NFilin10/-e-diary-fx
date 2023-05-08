module com.example.ediaryfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ediaryfx to javafx.fxml;
    exports com.example.ediaryfx;
}