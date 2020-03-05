package ui.controllers;

import java.util.List;

import business.Auth;
import business.Book;
import business.ControllerInterface;
import business.SystemController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class BooksController {
    @FXML
    private TableView<Book> booksTableView;
	@SuppressWarnings("unchecked")
	public void initialize() {
        TableColumn<Book, String> isbn = (TableColumn<Book, String>) getTableColumnByName(booksTableView, "ISBN");
        TableColumn<Book, String> title = (TableColumn<Book, String>) getTableColumnByName(booksTableView, "Title");
        TableColumn<Book, String> authors = (TableColumn<Book, String>) getTableColumnByName(booksTableView, "Authors");
        TableColumn<Book, String> add_copy = (TableColumn<Book, String>) getTableColumnByName(booksTableView, "Add Copy");
        
        ControllerInterface ci = new SystemController();
        List<Book> books = ci.allBooks();
        booksTableView.setItems(FXCollections.observableList(books));
        
        isbn.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));
        title.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        boolean isVisible = SystemController.currentAuth != null && (SystemController.currentAuth == Auth.ADMIN || SystemController.currentAuth == Auth.BOTH);
        add_copy.setVisible(isVisible);
        add_copy.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));
        add_copy.setCellFactory(new Callback<TableColumn<Book, String>, TableCell<Book, String>>() {
            @Override
            public TableCell<Book, String> call(TableColumn<Book, String> btnCol) {
                return new TableCell<Book, String>() {
                    final Button button = new Button();

                    {
                        button.setText("Add Copy");
                    }

                    @Override
                    public void updateItem(final String isbn, boolean empty) {
                        if (isbn != null) {
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                	try {
                                		for(Book b:books) {
                                			if (b.getIsbn().equalsIgnoreCase(isbn)) {
                                        		showAddBookCopy(b);
                                				return;
                                			}
                                		}
                                	} catch (Exception e) {}
                                }
                            });
                            this.setGraphic(button);
                        }
                    }
                };
            }
        });
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/views/AddBookCopy.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        AddBookCopyController controller = fxmlLoader.<AddBookCopyController>getController();
        controller.getIsbnField().setText(book.getIsbn());
        controller.getTitleField().setText(book.getTitle());
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
