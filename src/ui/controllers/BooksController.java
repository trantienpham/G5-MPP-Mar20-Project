package ui.controllers;

import java.util.List;
import java.util.stream.Collectors;

import business.AuthorizationLevel;
import business.Book;
import business.ControllerInterface;
import business.SystemController;
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

public class BooksController {
    @FXML
    private TableView<Book> booksTableView;
	@SuppressWarnings("unchecked")
	public void initialize() {
        TableColumn<Book, String> isbn = (TableColumn<Book, String>) getTableColumnByName(booksTableView, "ISBN");
        TableColumn<Book, String> title = (TableColumn<Book, String>) getTableColumnByName(booksTableView, "Title");
        TableColumn<Book, Book> authors = (TableColumn<Book, Book>) getTableColumnByName(booksTableView, "Authors");
        TableColumn<Book, Book> copyNumber = (TableColumn<Book, Book>) getTableColumnByName(booksTableView, "Copy Numbers");
        TableColumn<Book, Book> add_copy = (TableColumn<Book, Book>) getTableColumnByName(booksTableView, "Add Copy");
        
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
        boolean isVisible = SystemController.currentAuth != null && (SystemController.currentAuth == AuthorizationLevel.ADMIN || SystemController.currentAuth == AuthorizationLevel.BOTH);
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
                            		showAddBookCopy(book);
                            	} catch(Exception e) {}
                            }
                        });
                        this.setGraphic(button);
                    }
                }
            };
        });
        
        loadBooks();
	}

	private void loadBooks() {
        ControllerInterface ci = new SystemController();
        List<Book> books = ci.allBooks();
        booksTableView.getItems().clear();
        booksTableView.setItems(FXCollections.observableList(books));
	}
	private <T> TableColumn<T, ?> getTableColumnByName(TableView<T> tableView, String name) {
        for (TableColumn<T, ?> col : tableView.getColumns()) {
            if (col.getText().equals(name)) {
                return col;
            }
        }
        return null;
    }

	public void showAddBookCopy(Book book) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/views/BookCopy.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        BookCopyController controller = fxmlLoader.<BookCopyController>getController();
        controller.setBook(book);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setOnCloseRequest(param -> {
        	loadBooks();
            booksTableView.refresh();
        });
        stage.show();
    }
}
