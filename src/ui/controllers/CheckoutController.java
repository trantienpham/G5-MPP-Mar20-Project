package ui.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import business.Book;
import business.CheckoutInterface;
import business.LibraryMember;
import business.RepositoryFactory;
import business.RepositoryInterface;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import utils.Validators;

public class CheckoutController extends FormBaseController {
	@FXML
    private Text firstName;//set when member ID selected    
    
    @FXML
    private Text lastName;
    
    
    @FXML
    private Button exportButton;

    @FXML
    private ComboBox<Book> boxBook;

    @FXML
    private ComboBox<LibraryMember> member;
    
    @FXML
    private DatePicker dueDate;
    
    private RepositoryInterface<LibraryMember> memberRepository;
    private RepositoryInterface<Book> bookRepository;
    private CheckoutInterface checkoutRepository;
    
    public CheckoutController() {
    	memberRepository = RepositoryFactory.getMemberRepository();
    	bookRepository = RepositoryFactory.getBookRepository();
    	checkoutRepository = RepositoryFactory.getCheckoutRecordRepository();
    }

	public void initialize() {
        member.setCellFactory(param -> {
			return new ListCell<LibraryMember>(){
                @Override
                protected void updateItem(LibraryMember b, boolean bln) {
                    super.updateItem(b, bln);
                     
                    if(b != null){
                        setText(b.getFirstName() + " " + b.getLastName());
                    } else {
                        setText(null);
                    }
                }

            };
        });
        member.setConverter(new StringConverter<LibraryMember>() {
        	@Override
        	public String toString(LibraryMember b) {
        		if (b != null) return b.getFirstName() + " " + b.getLastName();
        		return null;
        	}

			@Override
			public LibraryMember fromString(String arg0) {
				return null;
			}
        });        
        //set onchange action of comboBox
        member.valueProperty().addListener(new ChangeListener<LibraryMember>() {
            @SuppressWarnings("rawtypes")
			@Override
            public void changed(ObservableValue ov, LibraryMember oldValue, LibraryMember newValue) {
            	if (newValue != null) {
	                firstName.setText("First Name: " + newValue.getFirstName());
	                lastName.setText("Last Name: " + newValue.getLastName());
            	}
            }
        });

		List<LibraryMember> members = memberRepository.getAll();
        member.setItems(FXCollections.observableArrayList(members));

        List<Book> availableBooks = bookRepository.getAll().stream().filter(b -> b.isAvailable()).collect(Collectors.toList());
        boxBook.setItems(FXCollections.observableArrayList(availableBooks));
        boxBook.setCellFactory(param -> {
			return new ListCell<Book>(){
                @Override
                protected void updateItem(Book b, boolean bln) {
                    super.updateItem(b, bln);
                     
                    if(b != null){
                        setText(b.getTitle() + " (" + b.getIsbn() + ")");
                    } else {
                        setText(null);
                    }
                }

            };
        });
        boxBook.setConverter(new StringConverter<Book>() {
        	@Override
        	public String toString(Book b) {
        		if (b != null) return b.getTitle() + " (" + b.getIsbn() + ")";
        		return null;
        	}

			@Override
			public Book fromString(String arg0) {
				return null;
			}
        });
	}
	
	@FXML
    public void handleSubmitAction() throws Exception{
        //Fields Validation        
        if (!validate())
            return; 
        
        Book book = boxBook.getValue();
        LibraryMember mem = member.getValue();
        LocalDate date = dueDate.getValue();
        if(book.isAvailable() && mem != null){
        	checkoutRepository.checkout(book, mem, date);
        	clear();
        }
    }

    @Override
    void validateAllFields() {        
        //validate member
        if (!isValidMember()) {
            if (!invalidFields.contains("ID")) {
                invalidFields.add("ID");
                member.setStyle(INVALID_STYLE_BORDER);                
            }
        } else {
            invalidFields.remove("ID");
            member.setStyle(VALID_STYLE_BORDER);
        }
        
        //validate Book
        if (!isValidBook()) {
            if (!invalidFields.contains("Book")) {
                invalidFields.add("Book");
                boxBook.setStyle(INVALID_STYLE_BORDER);                
            }
        } else {
            invalidFields.remove("Book");
            boxBook.setStyle(VALID_STYLE_BORDER);
        }
        
        //validate Due Date
        if (!isValidDueDate()) {
            if (!invalidFields.contains("Due Date")) {
                invalidFields.add("Due Date");
                dueDate.setStyle(INVALID_STYLE_BORDER);
            }
        } else {
            invalidFields.remove("Due Date");
             dueDate.setStyle(VALID_STYLE_BORDER);
        }
        
    }
    
    //Fields validation methods:
    
    //ComboBox (ID, member). To be valid it must be selected
    private boolean isValidMember(){       
        return !Validators.isEmpty(member.getSelectionModel().getSelectedItem().getMemberId());
    }
    
    //Book field. To be valid it must be selected
    private boolean isValidBook(){        
        return !Validators.isEmpty(boxBook.getSelectionModel().getSelectedItem().getIsbn());
    }
    
    //Due Date field. To be valid it must be filled
    private boolean isValidDueDate(){       
        return Validators.isValidDatePickerValue(dueDate.getValue());       
    }
    
    private void clear() {
    	boxBook.getSelectionModel().clearSelection();
    	member.getSelectionModel().clearSelection();
    	dueDate.setValue(null);
        firstName.setText("");
        lastName.setText("");
    }
}
