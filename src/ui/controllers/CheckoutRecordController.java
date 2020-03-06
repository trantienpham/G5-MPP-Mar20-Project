package ui.controllers;

import java.util.List;

import business.Book;
import business.BookCopy;
import business.CheckoutEntry;
import business.CheckoutInterface;
import business.CheckoutRecord;
import business.LibraryMember;
import business.RepositoryFactory;
import business.RepositoryInterface;
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
import utils.TableColumnUtil;

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
    private RepositoryInterface<LibraryMember> memberRepository;
    private RepositoryInterface<Book> bookRepository;
    private CheckoutInterface checkoutRepository;

    public CheckoutRecordController() {
    	memberRepository = RepositoryFactory.getMemberRepository();
    	bookRepository = RepositoryFactory.getBookRepository();
    	checkoutRepository = RepositoryFactory.getCheckoutRecordRepository();
    }

	public void initialize() {
		initComboboxes();
        intiTableView();
        
        loadMasterData();
		loadTableView();

        book.setItems(FXCollections.observableList(books));
        member.setItems(FXCollections.observableList(members));
		tableView
        .getSelectionModel()
        .selectedItemProperty();
	}
	
	@FXML
    public void handleMemberFilterAction(ActionEvent event) {
		this.selectedMember = member.getSelectionModel().getSelectedItem();
        loadTableView();
        tableView.refresh();
    }

    @FXML
    public void handleBookFilterAction(ActionEvent event) {
        this.selectedBook = book.getSelectionModel().getSelectedItem();
        loadTableView();
        tableView.refresh();
    }
    
    @FXML
    public void handleClearButtonAction(ActionEvent event) {
    	this.selectedBook = null;
    	this.selectedMember = null;
    	book.getSelectionModel().clearSelection();
    	member.getSelectionModel().clearSelection();
    	loadTableView();
        tableView.refresh();
    }

    @Override
    void validateAllFields() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void loadTableView() {
    	String isbn = selectedBook != null ? selectedBook.getIsbn() : "";
    	String memberId = selectedMember != null ? selectedMember.getMemberId() : "";
        List<CheckoutEntry> entries = checkoutRepository.searchBy(isbn, memberId);
        tableView.setItems(FXCollections.observableList(entries));
    }
    
    private void loadMasterData() {
		books = bookRepository.getAll();
        members = memberRepository.getAll();
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
    	TableColumn<CheckoutEntry, String> checkoutID = (TableColumn<CheckoutEntry, String>) TableColumnUtil.getTableColumnByName(tableView, "Checkout ID");
		TableColumn<CheckoutEntry, CheckoutRecord> member = (TableColumn<CheckoutEntry, CheckoutRecord>) TableColumnUtil.getTableColumnByName(tableView, "Member");
		TableColumn<CheckoutEntry, BookCopy> book = (TableColumn<CheckoutEntry, BookCopy>) TableColumnUtil.getTableColumnByName(tableView, "Book");
		TableColumn<CheckoutEntry, String> Checkout_Date = (TableColumn<CheckoutEntry, String>) TableColumnUtil.getTableColumnByName(tableView, "Checkout Date");
		TableColumn<CheckoutEntry, String> Return_Date = (TableColumn<CheckoutEntry, String>) TableColumnUtil.getTableColumnByName(tableView, "Return Date");
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
