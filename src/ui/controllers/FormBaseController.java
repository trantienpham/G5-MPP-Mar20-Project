package ui.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class FormBaseController {
    @FXML
    Button cancelButton;

    List<String> invalidFields;    
    private final Alert errorValidationAlert;
    
    static final String INVALID_STYLE_TEXT = "-fx-text-fill: red;-fx-font-weight: bold;";
    static final String VALID_STYLE_TEXT = "-fx-text-fill: black;-fx-font-weight: black;";
    static final String INVALID_STYLE_BACKGROUND = "-fx-background-color: red;";
    static final String VALID_STYLE_BACKGROUND = "-fx-background-color: white;";
    static final String INVALID_STYLE_BORDER = "-fx-border-color: red;";
    static final String VALID_STYLE_BORDER = "-fx-border-color: gray;";

    FormBaseController() {
        invalidFields = new ArrayList<>();
        errorValidationAlert = new Alert(Alert.AlertType.ERROR);
        errorValidationAlert.setTitle("Error Dialog");
        errorValidationAlert.setHeaderText("Following fields are invalid: ");
    }
    
    public void initialize() {}
    
    @FXML
    public void handleCancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    boolean validate() {
        validateAllFields();
        if (!invalidFields.isEmpty()) {
            errorValidationAlert.setContentText(invalidFields.toString());
            errorValidationAlert.showAndWait();
            return false;
        }
        return true;
    }
    abstract void validateAllFields();
}
