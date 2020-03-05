package ui.controllers;

import java.util.Optional;

import business.Auth;
import business.LibraryMember;
import business.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DashboardController {
    @FXML
    private Button checkoutButton;

    @FXML
    private Button checkoutRecordButton;

    @FXML
    private Button addMemberButton;

    @FXML
    private Button editMemberButton;

    @FXML
    private Button addBookButton;

    @FXML
    private Button books;

    @FXML
    private Button logoffButton;

    @FXML
    public Button closeButton;
    
    @FXML
    private Label userLoggedLabel;
    
    private User user;
	public DashboardController(User user) {
		this.user = user;
	}
	public void initialize() {
        Auth auth = this.user.getAuthorization();
        String role = "";
        checkoutButton.setDisable(true);
        checkoutRecordButton.setDisable(true);
        addMemberButton.setDisable(true);
        editMemberButton.setDisable(true);
        addBookButton.setDisable(true);
        books.setDisable(true);
        
        if (user.getAuthorization() == Auth.BOTH)
            role = "ADMINISTRATOR/LIBRARIAN";
        else
            role = user.getAuthorization().toString();
        userLoggedLabel.setText(user.getId() + " (" + role.toLowerCase() + ")");
        if ((auth == Auth.ADMIN) || (auth == Auth.BOTH) ) {
            addMemberButton.setDisable(false);
            editMemberButton.setDisable(false);
        }

        if ((auth == Auth.LIBRARIAN) || (auth == Auth.BOTH) ) {
            checkoutButton.setDisable(false);
            checkoutRecordButton.setDisable(false);
            addBookButton.setDisable(false);
            books.setDisable(false);
        }
	}
	public User getUser() {
		return user;
	}
	private Object launchForm(String formURL) throws Exception {
       Stage stage = new Stage();
       FXMLLoader loader = new FXMLLoader(getClass().getResource(formURL));
       Parent root = (Parent) loader.load();
       root.setStyle("-fx-background-color:  #8EC6E7");
       Scene scene = new Scene(root);
       stage.setScene(scene);
       stage.setResizable(false);
       stage.show();
       return loader.getController();
	}
    @FXML
    public void handleLogoffButtonAction(ActionEvent event) throws Exception {
    	showLogin();
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Library System");
        alert.setHeaderText("Close Library System");
        alert.setContentText("Are you sure you want close the system?");

        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }
    @FXML
    public void handleCheckoutButtonAction(ActionEvent event) throws Exception {
    	launchForm("/ui/views/Checkout.fxml");
    }

    @FXML
    public void handleCheckoutRecordButtonAction(ActionEvent event) throws Exception {
    	launchForm("/ui/views/CheckoutRecord.fxml");
    }
    
    @FXML
    public void handleAddMemberButtonAction(ActionEvent event) throws Exception {
    	launchForm("/ui/views/FormMember.fxml");
    }
    
    @FXML
    public void handleEditMemberButtonAction(ActionEvent event) throws Exception {
    	FormMemberController controller = (FormMemberController) launchForm("/ui/views/FormMember.fxml");
        LibraryMember member = null;
        controller.setMember(member);
    }

    @FXML
    public void handleAddBookButtonAction(ActionEvent event) throws Exception {
    	launchForm("/ui/views/AddBook.fxml");
    }
    
    @FXML
    public void handleGetAllBooksButtonAction(ActionEvent event) throws Exception {
    	launchForm("/ui/views/books.fxml");
    }
    
    private void showLogin() throws Exception {
    	Stage loginStage = new Stage();
        Stage dashboardStage = (Stage) closeButton.getScene().getWindow();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/views/Login.fxml"));
    	Parent root = (Parent) loader.load();
    	root.setStyle("-fx-background-color:  #8EC6E7");
    	Scene scene = new Scene(root);
    	loginStage.setScene(scene);
    	loginStage.setResizable(false);
    	dashboardStage.close();
    	loginStage.show();
    }
}
