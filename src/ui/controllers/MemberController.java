package ui.controllers;

import java.util.List;

import business.Address;
import business.LibraryMember;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.TableColumnUtil;

public class MemberController {
    @FXML
    private TableView<LibraryMember> membersTableView;
    
    private RepositoryInterface<LibraryMember> memberRepository;
    
    public MemberController() {
    	memberRepository = RepositoryFactory.getMemberRepository();
    }

	public void initialize() {
        initComponents();
        loadMembers();
	}
	
	@SuppressWarnings("unchecked")
	private void initComponents() {
		TableColumn<LibraryMember, String> memberId = (TableColumn<LibraryMember, String>) TableColumnUtil.getTableColumnByName(membersTableView, "Member Id");
        TableColumn<LibraryMember, String> fullname = (TableColumn<LibraryMember, String>) TableColumnUtil.getTableColumnByName(membersTableView, "Fullname");
        TableColumn<LibraryMember, Address> address = (TableColumn<LibraryMember, Address>) TableColumnUtil.getTableColumnByName(membersTableView, "Address");
        TableColumn<LibraryMember, LibraryMember> action = (TableColumn<LibraryMember, LibraryMember>) TableColumnUtil.getTableColumnByName(membersTableView, "Action");
        
        memberId.setCellValueFactory(new PropertyValueFactory<LibraryMember, String>("memberId"));
        fullname.setCellValueFactory(new PropertyValueFactory<LibraryMember, String>("fullname"));
        address.setCellValueFactory(new PropertyValueFactory<LibraryMember, Address>("address"));
        address.setCellFactory(param -> {
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
        });
        action.setCellValueFactory(param -> {
        	CellDataFeatures<LibraryMember, LibraryMember> cellData = (CellDataFeatures<LibraryMember, LibraryMember>) param;
            return new SimpleObjectProperty<LibraryMember>(cellData.getValue());
        });
        action.setCellFactory(param -> {
        	return new TableCell<LibraryMember, LibraryMember>() {
                final Button button = new Button();

                {
                    button.setText("Edit");
                    button.getStyleClass().add("btn-add-copy");
                }

                @Override
                public void updateItem(final LibraryMember member, boolean empty) {
                    if (member != null) {
                        button.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                            	try {
                            		openEditMember(member);
                            	} catch (Exception e) {}
                            }
                        });
                        this.setGraphic(button);
                    }
                }
            };
        });
	}
	private void loadMembers() {
        List<LibraryMember> members = memberRepository.getAll();
        membersTableView.setItems(FXCollections.observableList(members));
	}

	public void openEditMember(LibraryMember member) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/views/FormMember.fxml"));
        Parent root = (Parent) loader.load();
        FormMemberController controller = loader.<FormMemberController>getController();
        controller.setMember(member);
        controller.setReloadMembersHandler(x -> {
        	loadMembers();
        	membersTableView.refresh();
        	return null;
        });
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
