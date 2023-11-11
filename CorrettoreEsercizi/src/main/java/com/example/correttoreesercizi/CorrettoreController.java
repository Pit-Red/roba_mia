package com.example.correttoreesercizi;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CorrettoreController {
    @FXML
    private Label exercise;
    @FXML
    private Button confirm_button;
    @FXML
    private Button newex_button;
    @FXML
    private TextField answer;

    DataModel dataModel = new DataModel();
    @FXML
    protected void onNewExButtonClick() {
        exercise.setText(dataModel.getNewEx());
    }
    @FXML
    protected void onConfirmButtonClick() {
        if(dataModel.checkRisposta(answer.getText())) {
            CorrettoreApplication.showAlert("risposta corretta");
            onNewExButtonClick();
        }
        else
            CorrettoreApplication.showAlert("risposta errata, riprova");
        answer.setText("");
    }
}