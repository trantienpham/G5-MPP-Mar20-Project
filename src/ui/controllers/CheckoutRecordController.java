package ui.controllers;

import java.util.ArrayList;
import java.util.List;

import business.Book;
import business.BookCopy;
import business.CheckoutEntry;
import business.CheckoutRecord;
import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class CheckoutRecordController extends FormBaseController {
	@FXML
    private ComboBox<LibraryMember> member;

    @FXML
    private ComboBox<Book> book;

    @FXML
    private TableView<CheckoutEntry> tableView;

    @FXML
    private Button export;

    @FXML
    private Button ok;
    
    private List<Book> books;
    private List<LibraryMember> members;
    private Book selectedBook;
    private LibraryMember selectedMember;
    
	public void initialize() {
		initComboboxes();
        intiTableView();
        
        loadMasterData();
		loadTableView();

        book.setItems(FXCollections.observableList(books));
        member.setItems(FXCollections.observableList(members));
		tableView
        .getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> handleTableViewDoubleClickAction());
	}
	
	@FXML
    public void handleMemberFilterAction(ActionEvent event) {
		this.selectedMember = member.getSelectionModel().getSelectedItem();
        loadTableView();
    }

    @FXML
    public void handleBookFilterAction(ActionEvent event) {
        this.selectedBook = book.getSelectionModel().getSelectedItem();
        loadTableView();
    }
    
    @FXML
    public void handleClearButtonAction(ActionEvent event) {
    	this.selectedBook = null;
    	this.selectedMember = null;
    	book.getSelectionModel().clearSelection();
    	member.getSelectionModel().clearSelection();
    	loadTableView();
    }

    public void handleTableViewDoubleClickAction() {

//        tableView.setOnMousePressed((MouseEvent event) -> {
//            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
//
//                int rowIndex = tableView.getSelectionModel().getSelectedIndex();
//                ObservableList rowList = (ObservableList) tableView.getItems().get(rowIndex);
//
//                //Retrieving CheckoutID
//                String checkoutKey = rowList.get(0).toString();
//
//                Stage stage = new Stage();
//
//                util.log("Loading detailed record form...");
//
//                try {
//                    String formURL = "FormCheckoutRecordDetails.fxml";
//
//                    Parent root = FXMLLoader.load(getClass().getResource("/view/" + formURL));
//
//                    root.setStyle("-fx-background-color:  #8EC6E7");
//
//                    util.log("Loading " + formURL);
//                    Scene scene = new Scene(root);
//
//                    util.log("Setting scene stage");
//                    stage.setScene(scene);
//
//                    stage.setResizable(false);
//                    stage.setAlwaysOnTop(true);
//
//                    util.log("Showing " + formURL);
//                    stage.show();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }

    private <T> TableColumn<T, ?> getTableColumnByName(TableView<T> tableView, String name) {
        for (TableColumn<T, ?> col : tableView.getColumns()) {
            if (col.getText().equals(name)) {
                return col;
            }
        }
        return null;
    }

    @Override
    void validateAllFields() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void loadTableView() {
    	ControllerInterface ci = new SystemController();
        List<CheckoutEntry> entries = ci.allCheckoutEntries();

        int flag = 1;
        ArrayList<CheckoutEntry> filteredEntries = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            LibraryMember mem = entries.get(i).getCheckoutRecord().getLibraryMember();
            Book b = entries.get(i).getBookCopy().getBook();
            flag = 1;
            if (selectedMember != null && !selectedMember.getMemberId().equalsIgnoreCase(mem.getMemberId())) {
                flag = 0;
            }

            if (selectedBook != null && !b.getIsbn().equalsIgnoreCase(selectedBook.getIsbn())) {
                flag = 0;
            }
            
            if(flag == 1){
                filteredEntries.add(entries.get(i));
            }
        }

        tableView.setItems(FXCollections.observableList(filteredEntries));
        
    }
    
    private void loadMasterData() {
		ControllerInterface ci = new SystemController();
		books = ci.allBooks();
        members = ci.allMembers();
    }
    
    private void initComboboxes() {
        book.setCellFactory(new Callback<ListView<Book>, ListCell<Book>>(){
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
        book.setConverter(new StringConverter<Book>() {
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
        member.setCellFactory(new Callback<ListView<LibraryMember>,ListCell<LibraryMember>>(){
			@Override
			public ListCell<LibraryMember> call(ListView<LibraryMember> book) {
				final ListCell<LibraryMember> cell = new ListCell<LibraryMember>(){
                    @Override
                    protected void updateItem(LibraryMember m, boolean bln) {
                        super.updateItem(m, bln);
                         
                        if(m != null){
                            setText(m.getFullname());
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
        		if (b != null) return b.getFullname();
        		return null;
        	}

			@Override
			public LibraryMember fromString(String arg0) {
				return null;
			}
        });
    }
    
    @SuppressWarnings("unchecked")
    private void intiTableView() {
    	TableColumn<CheckoutEntry, String> checkoutID = (TableColumn<CheckoutEntry, String>) getTableColumnByName(tableView, "Checkout ID");
		TableColumn<CheckoutEntry, CheckoutRecord> member = (TableColumn<CheckoutEntry, CheckoutRecord>) getTableColumnByName(tableView, "Member");
		TableColumn<CheckoutEntry, BookCopy> book = (TableColumn<CheckoutEntry, BookCopy>) getTableColumnByName(tableView, "Book");
		TableColumn<CheckoutEntry, String> Checkout_Date = (TableColumn<CheckoutEntry, String>) getTableColumnByName(tableView, "Checkout Date");
		TableColumn<CheckoutEntry, String> Return_Date = (TableColumn<CheckoutEntry, String>) getTableColumnByName(tableView, "Return Date");
		checkoutID.setCellValueFactory(new PropertyValueFactory<CheckoutEntry, String>("id"));
		member.setCellValueFactory(new PropertyValueFactory<CheckoutEntry, CheckoutRecord>("checkoutRecord"));
		book.setCellValueFactory(new PropertyValueFactory<CheckoutEntry, BookCopy>("bookCopy"));
		Checkout_Date.setCellValueFactory(new PropertyValueFactory<CheckoutEntry, String>("checkoutDate"));
		Return_Date.setCellValueFactory(new PropertyValueFactory<CheckoutEntry, String>("dueDate"));
		member.setCellFactory(param -> {
			return new TableCell<CheckoutEntry, CheckoutRecord>() {
		        @Override
		        public void updateItem(final CheckoutRecord item, boolean empty) {
		            super.updateItem(item, empty);
		            if (item != null) {
		                setText(item.getLibraryMember().getFirstName() + " " + item.getLibraryMember().getLastName());
		            }
		        }
		    };
		});
		book.setCellFactory(param -> {
		    return new TableCell<CheckoutEntry, BookCopy>() {
	            @Override
	            public void updateItem(final BookCopy item, boolean empty) {
	                super.updateItem(item, empty);
	                if (item != null) {
	                    setText(item.getBook().getTitle());
	                }
	            }
	        };
		});
    }
}
