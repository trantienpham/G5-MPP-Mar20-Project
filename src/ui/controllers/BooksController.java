package ui.controllers;

import java.util.List;
import java.util.stream.Collectors;

import business.AuthServiceInterface;
import business.AuthorizationLevel;
import business.Book;
import business.RepositoryFactory;
import business.RepositoryInterface;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.TableColumnUtil;

public class BooksController {
    @FXML
    private TableView<Book> booksTableView;
    
    private RepositoryInterface<Book> bookRepository;
    private AuthServiceInterface authService;

    public BooksController() {
    	bookRepository = RepositoryFactory.getBookRepository();
    	authService = RepositoryFactory.getAuthService();
    }
    
	public void initialize() {
		initComponents();
        loadBooks();
	}

	private void loadBooks() {
        List<Book> books = bookRepository.getAll();
        booksTableView.getItems().clear();
        booksTableView.setItems(FXCollections.observableList(books));
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		TableColumn<Book, String> isbn = (TableColumn<Book, String>) TableColumnUtil.getTableColumnByName(booksTableView, "ISBN");
        TableColumn<Book, String> title = (TableColumn<Book, String>) TableColumnUtil.getTableColumnByName(booksTableView, "Title");
        TableColumn<Book, Book> authors = (TableColumn<Book, Book>) TableColumnUtil.getTableColumnByName(booksTableView, "Authors");
        TableColumn<Book, Book> copyNumber = (TableColumn<Book, Book>) TableColumnUtil.getTableColumnByName(booksTableView, "Copy Numbers");
        TableColumn<Book, Book> add_copy = (TableColumn<Book, Book>) TableColumnUtil.getTableColumnByName(booksTableView, "Add Copy");
        
        isbn.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));
        title.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        authors.setCellValueFactory(param -> {
        	CellDataFeatures<Book, Book> cellData = (CellDataFeatures<Book, Book>) param;
            return new SimpleObjectProperty<Book>(cellData.getValue());
        });
        authors.setCellFactory(param -> {
        	return new TableCell<Book, Book>() {
            	final Label label = new Label();
            	@Override
                public void updateItem(final Book book, boolean empty) {
                    if (book != null) {
                    	List<String> authors = book.getAuthors().stream().map(x -> x.getFirstName() + " " + x.getLastName()).collect(Collectors.toList());
                    	label.setText(String.join("\n", authors));
                    	setGraphic(label);
                    }
                }
            };
        });
        copyNumber.setCellValueFactory(param -> {
        	CellDataFeatures<Book, Book> cellData = (CellDataFeatures<Book, Book>) param;
            return new SimpleObjectProperty<Book>(cellData.getValue());
        });
        copyNumber.setCellFactory(param -> {
        	return new TableCell<Book, Book>() {
            	final Label label = new Label();
            	@Override
                public void updateItem(final Book book, boolean empty) {
                    if (book != null) {
                    	label.setText(String.valueOf(book.getNumCopies()));
                    	setGraphic(label);
                    }
                }
            };
        });
        AuthorizationLevel currentAuth = authService.getCurrentAuth();
        boolean isVisible = currentAuth != null && (currentAuth == AuthorizationLevel.ADMIN || currentAuth == AuthorizationLevel.BOTH);
        add_copy.setVisible(isVisible);
        add_copy.setCellValueFactory(param -> {
        	CellDataFeatures<Book, Book> cellData = (CellDataFeatures<Book, Book>) param;
            return new SimpleObjectProperty<Book>(cellData.getValue());
        });
        add_copy.setCellFactory(param -> {
        	return new TableCell<Book, Book>() {
                final Button button = new Button();

                {
                    button.setText("Add Copy");
                }

                @Override
                public void updateItem(final Book book, boolean empty) {
                    if (book != null) {
                        button.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                            	try {
                            		openAddBookCopy(book);
                            	} catch(Exception e) {}
                            }
                        });
                        this.setGraphic(button);
                    }
                }
            };
        });
	}

	public void openAddBookCopy(Book book) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/views/BookCopy.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        BookCopyController controller = fxmlLoader.<BookCopyController>getController();
        controller.setBook(book);
        controller.setReloadBooksHandler(new Callback<String, String>() {
			@Override
			public String call(String arg0) {
				loadBooks();
	            booksTableView.refresh();
				return null;
			}
        });
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
