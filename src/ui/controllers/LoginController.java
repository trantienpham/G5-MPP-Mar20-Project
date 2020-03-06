package ui.controllers;

import business.ControllerInterface;
import business.LoginException;
import business.SystemController;
import business.SystemUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import utils.Message;

public class LoginController {
	@FXML
    private TextField txtUserName;
    @FXML
    private TextField txtPassword;
    @FXML
    public Button btnCancel;
    @FXML
    private Button btnSignIn;

	public void initialize() {}
    @FXML
    public void handleCloseButtonAction(ActionEvent event) throws Exception {
    	Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    
    @FXML
    public void handleEnterPressed(KeyEvent event) throws Exception {
        if (event.getCode() == KeyCode.ENTER) {
	        handleSignInButtonAction();
        }
    }
    
    @FXML
    public void handleSignInButtonAction() throws Exception {
    	try {
			ControllerInterface c = new SystemController();
			SystemUser u = c.login(txtUserName.getText().trim(), txtPassword.getText().trim());
			if (u != null) {
				showDashboard(u);
			} else {
				Message.showErrorMessage("Library System", "Invalid User/Password", "Username or Password invalid!");
			}
		} catch(LoginException ex) {
			Message.showErrorMessage("Library System", "Error", "There is an error during processing.");
		}
    }
    
    private void showDashboard(SystemUser user) throws Exception {
    	Stage dashboardStage = new Stage();
    	Stage loginStage = (Stage) btnCancel.getScene().getWindow();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/views/Dashboard.fxml"));
    	DashboardController controller = new DashboardController(user);
    	loader.setController(controller);
    	Parent root = (Parent) loader.load();
    	Scene scene = new Scene(root);
    	dashboardStage.setScene(scene);
    	dashboardStage.setResizable(false);
    	loginStage.close();
    	dashboardStage.show();
    }
}
