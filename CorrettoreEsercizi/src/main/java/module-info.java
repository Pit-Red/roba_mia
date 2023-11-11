module com.example.correttoreesercizi {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.correttoreesercizi to javafx.fxml;
    exports com.example.correttoreesercizi;
}