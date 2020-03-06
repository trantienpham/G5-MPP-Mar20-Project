package ui.controllers;

import business.Book;
import business.RepositoryFactory;
import business.RepositoryInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import utils.Message;
import utils.Validators;

public class BookCopyController extends FormBaseController {
    @FXML
    private TextField isbnField;
    
    @FXML
    private TextField titleField;

    @FXML
    private TextField copyNumField;
    
    @FXML
    private Button addButton;
    
    private Book book;
    private RepositoryInterface<Book> bookRepository;
	
	public BookCopyController() {
		bookRepository = RepositoryFactory.getBookRepository();
	}

	public void initialize() {
		isbnField.setDisable(true);        
        titleField.setDisable(true);
	}
	
	@FXML
    private void btnAddBookCopyAction(ActionEvent event) {
        //Validation  
        if ( !validate() || this.book == null)
            return;   
        
        try {
            this.book.addCopy();
            bookRepository.save(this.book);
            Message.showSuccessMessage("Add Book Copy", "Book Copy Added Sucessfully to book", ""); 
        } catch (Exception ex) {
            Message.showErrorMessage("Add Book", "Saving Book Copy Failed. Exception message: ",  ex.getMessage());                      
        }
    }
	
	public void setBook(Book book) {
		this.book = book;
		isbnField.setText(book.getIsbn());
		titleField.setText(book.getTitle());
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
    
    private Callback<String, String> callback;
    public void setReloadBooksHandler(Callback<String, String> callback) {
    	this.callback = callback;
    }
    
    @Override
    public void handleCancelButtonAction(ActionEvent event) {
    	if (this.callback != null) this.callback.call(null);
    	super.handleCancelButtonAction(event);
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
