package view;

import common.model.DateTime;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import viewmodel.object.UserViewModel;
import viewmodel.UserManagementViewModel;
import java.util.Optional;

public class UserManagementViewController extends ViewController {

    private ViewHandler viewHandler;
    private UserManagementViewModel viewModel;

    @FXML private TableView<UserViewModel> usersTable;
    @FXML private TableColumn<UserViewModel, String> firstNameColumn;
    @FXML private TableColumn<UserViewModel, String> lastNameColumn;
    @FXML private TableColumn<UserViewModel, String> emailColumn;
    @FXML private TableColumn<UserViewModel, DateTime> birthdayColumn;
    @FXML private TableColumn<UserViewModel, String> genderColumn;
    @FXML private TableColumn<UserViewModel, String> statusColumn;

    @FXML private Label usernameLabel;
    @FXML private Button basketButton;
    @FXML private Button managementButton;
    @FXML private Label errorLabel;

    @Override
    protected void init() {
        viewHandler = getViewHandler();
        viewModel = getViewModelFactory().getUserManagementViewModel();

        usernameLabel.textProperty().bind(viewModel.getUsernameProperty());
        basketButton.textProperty().bind(viewModel.getBasketButtonTitleProperty());

        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().getFirstName());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().getLastName());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmail());
        birthdayColumn.setCellValueFactory(cellData -> cellData.getValue().getBirthDate());
        genderColumn.setCellValueFactory(cellData -> cellData.getValue().getGender());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().getStatus());
        usersTable.setItems(viewModel.getUserListTable());
        usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            managementButton.setText((newVal == null ? "ADD" : "EDIT") + " USER");
            viewModel.setSelectedUserProperty(newVal);
        });

        // Bindings for the rest of the user interface elements.
        usernameLabel.textProperty().bind(viewModel.getUsernameProperty());
        basketButton.textProperty().bind(viewModel.getBasketButtonTitleProperty());
        errorLabel.textProperty().bind(viewModel.getErrorProperty());
    }

    @Override
    protected void reset() {
        usersTable.getSelectionModel().clearSelection();
        viewModel.reset();
    }

    @FXML
    private void openCatalogView() {
        try {
            viewHandler.openView(View.CATALOG);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Can not view the catalog at this time. Try later.");
        }
    }

    @FXML
    private void openBasketView() {
        try {
            viewHandler.openView(View.BASKET);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Can not manage basket at this time. Try later.");
        }
    }

    @FXML
    private void openProductManagementView() {
        try {
            viewHandler.openView(View.MANAGEPRODUCTS);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Can not manage products at this time. Try later.");
        }
    }

    @FXML
    public void openOrdersView() {
        try {
            viewHandler.openView(View.ORDERS);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Can not view orders at this time. Try later.");
        }
    }

    @FXML
    private void makeEmployeeButton() {
        viewModel.makeEmployee();
    }

    @FXML
    private void removeUser() {
        if (usersTable.getSelectionModel().selectedItemProperty().getValue() == null) {
            viewModel.getErrorProperty().set("Please select a user to remove first.");
            return;
        }
        if (confirmation(false)) viewModel.removeUser();
    }

    @FXML
    private void fireEmployeeButton() {
        viewModel.fireEmployee();
    }

    @FXML
    private void addEditUser() {
        try {
            if (usersTable.getSelectionModel().selectedItemProperty().getValue() != null) if (!confirmation(true)) return;
            viewHandler.openView(View.MANAGEUSERS);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Can not edit users at this time. Try later.");
        }
    }

    @FXML
    private void deauthenticate() {
        if (!viewModel.deauthenticate()) {
            viewModel.getErrorProperty().set("Could not deauthenticate the user.");
            return;
        }
        try {
            viewHandler.openView(View.AUTHENTICATION);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Could not logout at this time. Try later.");
        }
    }

    private boolean confirmation(boolean action) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to " + (action ? "edit" : "delete") + " this user?");
        Optional<ButtonType> result = alert.showAndWait();
        return (result.isPresent()) && (result.get() == ButtonType.OK);
    }
}
