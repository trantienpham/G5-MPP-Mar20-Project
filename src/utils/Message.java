package utils;

import javafx.scene.control.Alert;

public class Message {

    private static Alert successAlert =  new Alert(Alert.AlertType.INFORMATION);
    private static Alert errorAlert = new Alert(Alert.AlertType.ERROR);
   

    public static void showSuccessMessage(String messageTtitle, String messageHeader, String messageContent) {
        successAlert.setTitle(messageTtitle);
        successAlert.setHeaderText(messageContent);
        successAlert.setContentText(messageHeader);
        successAlert.showAndWait();
    }

    public static void showErrorMessage(String messageTtitle, String messageHeader, String messageContent) {
        errorAlert.setTitle(messageTtitle);        
        errorAlert.setHeaderText(messageHeader);
        errorAlert.setContentText(messageContent);
        errorAlert.showAndWait();

    }

}