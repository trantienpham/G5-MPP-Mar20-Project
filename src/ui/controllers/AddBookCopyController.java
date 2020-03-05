package ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import utils.Validators;

public class AddBookCopyController extends FormBaseController {
    @FXML
    private TextField isbnField;
    
    @FXML
    private TextField titleField;

    @FXML
    private TextField copyNumField;
    
    @FXML
    private Button addButton;

	public void initialize() {
		isbnField.setDisable(true);        
        titleField.setDisable(true);
	}
	
	@FXML
    private void btnAddBookCopyAction(ActionEvent event) {
        
        //Validation  
        if ( !validate() ) // if not valid don't continu, just return to the form
            return;   
        
//        BookCopy copy = new BookCopy();
//        copy.setCopynumber(copyNumField.getText());
//        copy.setIsbn(isbnField.getText());
//        Book book = new Book();
//        book.setIsbn(isbnField.getText());
//        copy.setBook(book);
//        copy.setAvailable(true);
//        try {
//            bookCopyDao.addBookCopy(copy);
//             Message.showSuccessMessage("Add Book Copy", "Book Copy Added Sucessfully to book", ""); 
//        } catch (IOException ex) {
//            Message.showErrorMessage("Add Book", "Saving Book Copy Failed. Exception message: ",  ex.getMessage());                      
//        }
    }

    public TextField getIsbnField() {
        return isbnField;
    }

    public TextField getTitleField() {
        return titleField;
    }

    @Override
    void validateAllFields() {
        //validate isbn
        if (!isValidCopyNumber()) {
            if (!invalidFields.contains("Copy Number")) {
                invalidFields.add("Copy Number");
                copyNumField.setStyle(INVALID_STYLE_BORDER);
            }
        } else {
            invalidFields.remove("Copy Number");
            copyNumField.setStyle(VALID_STYLE_BORDER);
        }
    }
    
    //Fields validation methods:
    
    //Copy Number: valid if non empty and isAlphaNumerice
    private boolean isValidCopyNumber(){
        String textValue = copyNumField.getText();
        if (!Validators.isEmpty(textValue) && Validators.isAlphaNumeric(textValue)) 
            return true;        
        return false;
    }
}
