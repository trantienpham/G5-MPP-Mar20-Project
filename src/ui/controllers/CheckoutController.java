package ui.controllers;

import java.util.ArrayList;
import java.util.List;

import business.Book;
import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.util.Callback;
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

	public void initialize() {
		ControllerInterface ci = new SystemController();
		List<LibraryMember> members = ci.allMembers();
        member.setItems(FXCollections.observableArrayList(members));
        member.setCellFactory(new Callback<ListView<LibraryMember>, ListCell<LibraryMember>>(){
			@Override
			public ListCell<LibraryMember> call(ListView<LibraryMember> book) {
				final ListCell<LibraryMember> cell = new ListCell<LibraryMember>(){
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
                 
                return cell;
			}
        	
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

        List<Book> books = ci.allBooks();
        List<Book> availableBook = new ArrayList<Book>();
        for(Book b : books) {
        	if (b.isAvailable()) {
				availableBook.add(b);
        	}
        }
        boxBook.setItems(FXCollections.observableArrayList(availableBook));
        boxBook.setCellFactory(new Callback<ListView<Book>, ListCell<Book>>(){
			@Override
			public ListCell<Book> call(ListView<Book> book) {
				final ListCell<Book> cell = new ListCell<Book>(){
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
                 
                return cell;
			}
        	
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
        if(book.isAvailable() && mem != null){
        	ControllerInterface ci = new SystemController();
        	ci.checkoutBookByMember(book, mem, dueDate.getValue());
        	clear();
        }
    }

    @FXML
    public void handleExportButtonAction(ActionEvent event) {

//        Stage stage = new Stage();
//
//        util.log("Exporting data to file .csv...");
//
//        String defaultFileName = "LibrarySystemExport.csv";
//
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save file");
//        fileChooser.setInitialFileName(defaultFileName);
//        File savedFile = fileChooser.showSaveDialog(stage);
//        if (savedFile != null) {
//            try {
//                saveFileRoutine(savedFile);
//            } catch (Exception e) {
//                e.printStackTrace();
//                //actionStatus.setText("An ERROR occurred while saving the file!");
//                return;
//            }
//            //actionStatus.setText("File saved: " + savedFile.toString());
//        } else {
//            //actionStatus.setText("File save cancelled.");
//        }
//
//        util.log("Export complete");
    }

//    private void saveFileRoutine(File file) throws Exception {
//
//        try {
//            // Creates a new file and writes the txtArea contents into it
//            String txt = "Text content";
//
//            file.createNewFile();
//            FileWriter writer = new FileWriter(file);
//            writer.write(txt);
//            writer.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
