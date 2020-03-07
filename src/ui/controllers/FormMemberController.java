package ui.controllers;

import java.util.Random;

import business.Address;
import business.LibraryMember;
import business.RepositoryFactory;
import business.RepositoryInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import utils.Message;
import utils.Validators;

public class FormMemberController extends FormBaseController {
	@FXML
	private Label labelHead;

    @FXML
    private ComboBox<String> state;
    
    @FXML
    private ComboBox<String> city;
    
    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;
    
    @FXML
    private TextField phoneNumber;
    
    @FXML
    private Button add;
    
    @FXML
    private TextField street;
    
    @FXML   
    private TextField zip;
    
    private LibraryMember member;
    
    private RepositoryInterface<LibraryMember> memberRepository;
    
    public FormMemberController() {
    	memberRepository = RepositoryFactory.getMemberRepository();
    }

	public void initialize() {
		state.getItems().clear();

        state.getItems().addAll(
                "IOWA",
                "Texas",
                "Illinoi",
                "Canada",
                "Ohio");
        
        city.getItems().clear();

        city.getItems().addAll(
                "Iowa City",
                "Los Angles",
                "San Fransisco",
                "Chicago",
                "San Diego");
	}
	private Callback<String, String> callback;
	public void setReloadMembersHandler(Callback<String, String> callback) {
    	this.callback = callback;
    }

	@FXML
    private void addMember(ActionEvent event) {
        //Fields Validation        
        if ( !validate() )
            return; 
        
        LibraryMember libraryMember = new LibraryMember(
    		this.member != null ? this.member.getMemberId() : String.valueOf(new Random().nextInt(1000)),
    		firstName.getText(),
    		lastName.getText(),
    		phoneNumber.getText(),
    		new Address(street.getText(),city.getValue().toString(),state.getValue().toString(),zip.getText())
        );
        try {
        	memberRepository.save(libraryMember);
            Message.showSuccessMessage("Add Member", "Saving Member Sucess", "");
            clear();
            if (this.callback != null) this.callback.call(null);
        } catch (Exception ex) {
            Message.showErrorMessage("Add Member", "Saving Member Failed. Exception message: ",  ex.getMessage());          
            
        }
        
    }

	void clear() {
		firstName.setText("");
		lastName.setText("");
		phoneNumber.setText("");
		street.setText("");
		city.setValue(null);
		state.setValue(null);
		zip.setText("");
	}
	@Override
	void validateAllFields() {
		//validate firstName
        if (!isValidFirstName()) {
            if (!invalidFields.contains("First Name")) {
                invalidFields.add("First Name");
                firstName.setStyle(INVALID_STYLE_BORDER);                
            }
        } else {
            invalidFields.remove("First Name");
            firstName.setStyle(VALID_STYLE_BORDER);
        }
        
        //validate last name
        if (!isValidLastName()) {
            if (!invalidFields.contains("Last Name")) {
                invalidFields.add("Last Name");
                lastName.setStyle(INVALID_STYLE_BORDER);                
            }
        } else {
            invalidFields.remove("Last Name");
            lastName.setStyle(VALID_STYLE_BORDER);
        }
        
        //validate phone number
        if (!isValidLastName()) {
            if (!invalidFields.contains("Phone Number")) {
                invalidFields.add("Phone Number");
                phoneNumber.setStyle(INVALID_STYLE_BORDER);                
            }
        } else {
            invalidFields.remove("Phone Number");
            phoneNumber.setStyle(VALID_STYLE_BORDER);
        }
        
        //validate state
        if (!isValidState()) {
            if (!invalidFields.contains("State")) {
                invalidFields.add("State");
                state.setStyle(INVALID_STYLE_BORDER);                
            }
        } else {
            invalidFields.remove("State");
            state.setStyle(VALID_STYLE_BORDER);
        }
        
        //validate City
        if (!isValidCity()) {
            if (!invalidFields.contains("City")) {
                invalidFields.add("City");
                city.setStyle(INVALID_STYLE_BORDER);                
            }
        } else {
            invalidFields.remove("City");
            city.setStyle(VALID_STYLE_BORDER);
        }
        
        //validate Street
        if (!isValidStreet()) {
            if (!invalidFields.contains("Street")) {
                invalidFields.add("Street");
                street.setStyle(INVALID_STYLE_BORDER);                
            }
        } else {
            invalidFields.remove("Street");
            street.setStyle(VALID_STYLE_BORDER);
        }
        
        //validate Zip
        if (!isValidZip()) {
            if (!invalidFields.contains("Zip")) {
                invalidFields.add("Zip");
                zip.setStyle(INVALID_STYLE_BORDER);                
            }
        } else {
            invalidFields.remove("Zip");
            zip.setStyle(VALID_STYLE_BORDER);
        }
	}

    //firstName:
    private boolean isValidFirstName(){
        String textValue = firstName.getText();
        if (!Validators.isEmpty(textValue) && Validators.isAlphabetOnly(textValue) )  
            return true;        
        return false;
    }
    
    //firstName:
    private boolean isValidLastName(){
        String textValue = lastName.getText();
        if (!Validators.isEmpty(textValue) && Validators.isAlphabetOnly(textValue) ) 
            return true;        
        return false;
    }
    
    //State field. To be valid it must be selected
    private boolean isValidState(){              
        return !Validators.isEmpty(state.getValue());
    }
    
    //City field. To be valid it must be selected
    private boolean isValidCity(){              
        return !Validators.isEmpty(city.getValue());
    }
    
    //City field. To be valid it must be non-empty
    private boolean isValidStreet(){              
        return !Validators.isEmpty(street.getText());
    }
    
    //zip: 5 numbers only
    private boolean isValidZip(){
        String textValue = zip.getText();
        if (!Validators.isEmpty(textValue) && Validators.isNumericOnly(textValue) && textValue.length() == 5) 
            return true;        
        return false;
    }

	public LibraryMember getMember() {
		return member;
	}

	public void setMember(LibraryMember member) {
		this.member = member;
		add.setText("Save");
		labelHead.setText("Edit Member");
    	street.setText(this.member.getAddress().getStreet());
    	city.setValue(this.member.getAddress().getCity());
    	state.setValue(this.member.getAddress().getState());
    	zip.setText(this.member.getAddress().getZip());
    	firstName.setText(this.member.getFirstName());
    	lastName.setText(this.member.getLastName());
    	phoneNumber.setText(this.member.getTelephone());
	}

}
