package ui.controllers;

import java.util.List;

import business.Address;
import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;
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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MemberController {
    @FXML
    private TableView<LibraryMember> membersTableView;
	@SuppressWarnings("unchecked")
	public void initialize() {
        TableColumn<LibraryMember, String> memberId = (TableColumn<LibraryMember, String>) getTableColumnByName(membersTableView, "Member Id");
        TableColumn<LibraryMember, String> fullname = (TableColumn<LibraryMember, String>) getTableColumnByName(membersTableView, "Fullname");
        TableColumn<LibraryMember, Address> address = (TableColumn<LibraryMember, Address>) getTableColumnByName(membersTableView, "Address");
        TableColumn<LibraryMember, String> action = (TableColumn<LibraryMember, String>) getTableColumnByName(membersTableView, "Action");
        
        ControllerInterface ci = new SystemController();
        List<LibraryMember> members = ci.allMembers();
        membersTableView.setItems(FXCollections.observableList(members));
        
        memberId.setCellValueFactory(new PropertyValueFactory<LibraryMember, String>("memberId"));
        fullname.setCellValueFactory(new PropertyValueFactory<LibraryMember, String>("fullname"));
        address.setCellValueFactory(new PropertyValueFactory<LibraryMember, Address>("address"));
        address.setCellFactory(new Callback<TableColumn<LibraryMember, Address>, TableCell<LibraryMember, Address>>() {
            @Override
            public TableCell<LibraryMember, Address> call(TableColumn<LibraryMember, Address> btnCol) {
                return new TableCell<LibraryMember, Address>() {
                    final Label label = new Label();

                    @Override
                    public void updateItem(final Address address, boolean empty) {
                        if (address != null) {
                            label.setText(address.getStreet() + ", " + address.getCity() + " " + address.getState() + ", " + address.getZip());
                            this.setGraphic(label);
                        }
                    }
                };
            }
        });
        action.setCellValueFactory(new PropertyValueFactory<LibraryMember, String>("memberId"));
        action.setCellFactory(new Callback<TableColumn<LibraryMember, String>, TableCell<LibraryMember, String>>() {
            @Override
            public TableCell<LibraryMember, String> call(TableColumn<LibraryMember, String> btnCol) {
                return new TableCell<LibraryMember, String>() {
                    final Button button = new Button();

                    {
                        button.setText("Edit");
                    }

                    @Override
                    public void updateItem(final String memberId, boolean empty) {
                        if (memberId != null) {
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                	try {
                                		for(LibraryMember m:members) {
                                			if (m.getMemberId().equalsIgnoreCase(memberId)) {
                                				showEditmember(m);
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

	public void showEditmember(LibraryMember member) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/views/FormMember.fxml"));
        Parent root = (Parent) loader.load();
        FormMemberController controller = loader.<FormMemberController>getController();
        controller.setMember(member);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
