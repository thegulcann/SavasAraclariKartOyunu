module IkinciProje {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.ikinciProje to javafx.fxml;
    exports com.ikinciProje;
}
