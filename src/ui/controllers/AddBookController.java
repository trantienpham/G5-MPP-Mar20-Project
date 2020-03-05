package ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
    
     
    public void initialize() {        
        cmbCheckoutLength.getItems().addAll(7, 21); //possible values of the comboBox
        cmbCheckoutLength.setValue(7);//set default
    }  
    
    @FXML
    private void btnSaveAction(ActionEvent event) {        
        //Validation  
//        if (!validate())
//            return;        
//        
//        //if The data is valid
//        Book book = new Book();
//        book.setIsbn(txtISBN.getText());
//        book.setTitle(txtTitle.getText());
//        System.out.println("Continue Implement Save");
//        
//        List<BookCopy> copiesList = new ArrayList<>();   
//        
//        BookDao bookDao = new BookDao();       
//        try {
//            bookDao.addBook(book);
//            Message.showSuccessMessage("Add Book", "Saving Book Sucess", "");            
//        } catch (IOException ex) {
//            Message.showErrorMessage("Add Book", "Saving Book Failed. Exception message: ",  ex.getMessage());          
//            
//        }
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
