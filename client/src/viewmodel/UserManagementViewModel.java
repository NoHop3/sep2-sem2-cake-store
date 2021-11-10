package viewmodel;

import common.model.Customer;
import common.model.Employee;
import common.model.User;
import common.utility.observer.event.ObserverEvent;
import common.utility.observer.listener.LocalListener;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Model;
import viewmodel.object.UserViewModel;
import viewmodel.viewstate.UserManagementViewState;

public class UserManagementViewModel implements LocalListener<String, Object> {

    private Model model;
    private UserManagementViewState viewState;

    private ObservableList<UserViewModel> userListTable;

    private StringProperty errorProperty;
    private StringProperty usernameProperty;
    private StringProperty basketButtonTitleProperty;

    public UserManagementViewModel(Model model, UserManagementViewState viewState) {
        this.model = model;
        this.viewState = viewState;

        this.userListTable = FXCollections.observableArrayList();

        // Initialize the instance variables responsible for storing data of the other ui elements.
        errorProperty = new SimpleStringProperty();
        usernameProperty = new SimpleStringProperty("");
        basketButtonTitleProperty = new SimpleStringProperty("Basket");

        model.addListener(this);
    }

    public void reset() {
        errorProperty.set("");

        // Deselect any selected user if window reopens.
        viewState.setSelectedUser(null);

        if (!model.wasDataQueriedFor("UserManagement")) {
            userListTable.clear();
            try {
                model.getAllRegisteredUsers().getAllUsers().forEach(user -> userListTable.add(new UserViewModel(user)));
            } catch (Exception e) {
                errorProperty.set(e.getMessage());
            }
            try {
                usernameProperty.set("Employee ● " + model.getAuthenticatedUser().getFullName());
            } catch (Exception e) {
                usernameProperty.set("");
                errorProperty.set(e.getMessage());
            }
        }

        // Updates the number of products indicator in the basket button title.
        int tmp = model.getAllProductsInBasket().size();
        basketButtonTitleProperty.set(tmp == 0 ? "Basket" : "Basket (" + tmp + ")");
    }

    public StringProperty getUsernameProperty() {
        return usernameProperty;
    }

    public StringProperty getBasketButtonTitleProperty() {
        return basketButtonTitleProperty;
    }

    public ObservableList<UserViewModel> getUserListTable() {
        return userListTable;
    }

    public StringProperty getErrorProperty() {
        return errorProperty;
    }

    public void setSelectedUserProperty(UserViewModel userViewModel) {
        viewState.setSelectedUser(userViewModel);
    }

    public void makeEmployee() {
        UserViewModel selectedUser = viewState.getSelectedUser();
        try {
            if (selectedUser == null) throw new Exception("Please select a customer to make an employee.");
            if (selectedUser.getStatus().getValue().equals("Employee")) throw new Exception("Selected user is already an employee.");
        } catch (Exception e) {
            errorProperty.set(e.getMessage());
            return;
        }
        try {
            model.updateUser(selectedUser.getEmail().getValue(), new Employee(
                    selectedUser.getEmail().getValue(),
                    selectedUser.getPassword().getValue(),
                    selectedUser.getFirstName().getValue(),
                    selectedUser.getLastName().getValue(),
                    selectedUser.getBirthDate().getValue(),
                    selectedUser.getGender().getValue().equalsIgnoreCase("M") ? 'M' : 'F'
            ));
        } catch (Exception e) {
            errorProperty.set(e.getMessage());
        }
    }

    public void removeUser() {
        String tmp = viewState.getSelectedUser().getEmail().getValue();
        try {
            if (tmp.equals(model.getAuthenticatedUser().getEmail())) throw new IllegalStateException("You cannot delete yourself.");
            model.removeUser(viewState.getSelectedUser().getEmail().getValue());
        } catch (Exception e) {
            errorProperty.set(e.getMessage());
        }
    }

    public void fireEmployee() {
        UserViewModel selectedUser = viewState.getSelectedUser();
        try {
            if (selectedUser == null) throw new Exception("Please select an employee to be fired.");
            if (selectedUser.getStatus().getValue().equals("Customer")) throw new Exception("Selected user is not an employee! Why fire customers?");
            if (selectedUser.getEmail().getValue().equals(model.getAuthenticatedUser().getEmail())) throw new IllegalStateException("You cannot fire yourself.");
        } catch (Exception e) {
            errorProperty.set(e.getMessage());
            return;
        }
        try {
            model.updateUser(selectedUser.getEmail().getValue(), new Customer(
                    selectedUser.getEmail().getValue(),
                    selectedUser.getPassword().getValue(),
                    selectedUser.getFirstName().getValue(),
                    selectedUser.getLastName().getValue(),
                    selectedUser.getBirthDate().getValue(),
                    selectedUser.getGender().getValue().equalsIgnoreCase("M") ? 'M' : 'F'
            ));
        } catch (Exception e) {
            errorProperty.set(e.getMessage());
        }
    }

    public boolean deauthenticate() {
        return model.deauthenticate();
    }

    @Override
    public void propertyChange(ObserverEvent<String, Object> event) {
        Platform.runLater(() -> {
            switch (event.getPropertyName()) {
                case "newUser" : {
                    userListTable.add(new UserViewModel((User) event.getValue2()));
                    break;
                }
                case "deletedUser" : {
                    UserViewModel find = null;
                    for (UserViewModel user : userListTable) if (user.getEmail().getValue().equals(event.getValue1())) find = user;
                    if (find != null) userListTable.remove(find);
                    break;
                }
            }
        });
    }
}
