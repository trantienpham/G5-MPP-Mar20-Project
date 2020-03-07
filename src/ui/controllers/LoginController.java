package ui.controllers;

import business.AuthServiceInterface;
import business.LoginException;
import business.RepositoryFactory;
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
	
    private AuthServiceInterface authService = null;
    
    public LoginController() {
    	authService = RepositoryFactory.getAuthService();
    }

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
			SystemUser systemUser = authService.login(txtUserName.getText().trim(), txtPassword.getText().trim());
			if (systemUser != null) {
				gotoDashboard(systemUser);
			} else {
				Message.showErrorMessage("Library System", "Invalid User/Password", "Username or Password invalid!");
			}
		} catch(LoginException ex) {
			Message.showErrorMessage("Library System", "Error", "There is an error during processing.");
		}
    }
    
    private void gotoDashboard(SystemUser user) throws Exception {
    	Stage dashboardStage = new Stage();
    	Stage loginStage = (Stage) btnCancel.getScene().getWindow();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/views/Dashboard.fxml"));
    	DashboardController controller = new DashboardController(user);
    	loader.setController(controller);
    	Parent root = (Parent) loader.load();
    	Scene scene = new Scene(root);
    	dashboardStage.setScene(scene);
    	dashboardStage.setResizable(true);
    	dashboardStage.setWidth(700);
    	dashboardStage.setHeight(500);
    	loginStage.close();
    	dashboardStage.show();
    }
}
