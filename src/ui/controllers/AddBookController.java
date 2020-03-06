package ui.controllers;

import java.util.ArrayList;
import java.util.List;

import business.Author;
import business.Book;
import business.RepositoryFactory;
import business.RepositoryInterface;
import dataaccess.TestData;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import utils.Message;
import utils.Validators;

public class AddBookController extends FormBaseController {
    @FXML
    private TextField txtISBN;//13 digits number e.g. 978-3-16-148410-5
    
    @FXML
    private TextField txtTitle;
    
    @FXML
    private ComboBox<Integer> cmbCheckoutLength;   
    
    @FXML
    private Button btnSave;
    
    @FXML
    private ListView<Author> listAuthor;
    
    private RepositoryInterface<Book> bookRepository;
    
    public AddBookController() {
    	bookRepository = RepositoryFactory.getBookRepository();
    }
     
    public void initialize() {        
        cmbCheckoutLength.getItems().addAll(7, 21);
        cmbCheckoutLength.setValue(7);
        listAuthor.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listAuthor.setCellFactory(new Callback<ListView<Author>, ListCell<Author>>(){
			@Override
			public ListCell<Author> call(ListView<Author> author) {
				final ListCell<Author> cell = new ListCell<Author>(){
                    @Override
                    protected void updateItem(Author author, boolean bln) {
                        super.updateItem(author, bln);
                         
                        if(author != null){
                            setText(author.getFirstName() + " " + author.getLastName());
                        } else {
                            setText(null);
                        }
                    }
  
                };
                 
                return cell;
			}
        	
        });
        listAuthor.setItems(FXCollections.observableList(new TestData().allAuthors));
    }  
    
    @FXML
    private void btnSaveAction(ActionEvent event) {        
        //Validation  
        if (!validate())
            return;        
        List<Author> authors = new ArrayList<Author>();
        for(Author a : listAuthor.getSelectionModel().getSelectedItems()) {
        	authors.add(a);
        }
        //if The data is valid
        Book book = new Book(
			txtISBN.getText(),
			txtTitle.getText(),
			cmbCheckoutLength.getSelectionModel().getSelectedItem(),
			authors
        );
        
        try {
            bookRepository.save(book);
            Message.showSuccessMessage("Add Book", "Saving Book Sucess", "");
            clear();
        } catch (Exception ex) {
            Message.showErrorMessage("Add Book", "Saving Book Failed. Exception message: ",  ex.getMessage());          
            
        }
    }
    
    void clear() {
    	txtISBN.setText("");
    	txtTitle.setText("");
    	cmbCheckoutLength.getSelectionModel().clearSelection();
    	listAuthor.getSelectionModel().clearSelection();
    	cmbCheckoutLength.setValue(7);
    }
    
    @Override
    void validateAllFields() {
        //validate isbn
        if (!isValidIsbn()) {
            if (!invalidFields.contains("ISBN")) {
                invalidFields.add("ISBN");
                txtISBN.setStyle(INVALID_STYLE_BORDER);
            }
        } else {
            invalidFields.remove("ISBN");
            txtISBN.setStyle(VALID_STYLE_BORDER);
        }
        
        //validate title
        if (!isValidTitle()) {
            if (!invalidFields.contains("Title")) {
                invalidFields.add("Title");
                txtTitle.setStyle(INVALID_STYLE_BORDER);
            }
        } else {
            invalidFields.remove("Title");
            txtTitle.setStyle(VALID_STYLE_BORDER);
        }
    }
       
    // validation methods:    
    //ISBN:
    private boolean isValidIsbn(){
        String textValue = txtISBN.getText();
        if (!Validators.isEmpty(textValue) && Validators.isNumericOnly(textValue) && textValue.length() == 13) 
            return true;        
        return false;
    }
    
    //Title, may contain numbers or other symbols (e.g. physics 141 & ..)
    private boolean isValidTitle(){        
        if (Validators.isEmpty(txtTitle.getText())) 
            return false;        
        return true;
    }
}
