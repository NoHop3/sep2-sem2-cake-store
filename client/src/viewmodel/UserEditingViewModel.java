package viewmodel;

import common.model.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Model;
import viewmodel.object.UserViewModel;
import viewmodel.viewstate.UserManagementViewState;
import java.time.LocalDate;

public class UserEditingViewModel {

    private Model model;

    // Instance variables for linking and storing elements of the user interface.
    private StringProperty errorProperty;
    private StringProperty emailProperty;
    private StringProperty passwordProperty;
    private StringProperty firstNameProperty;
    private StringProperty lastNameProperty;
    private ObjectProperty<LocalDate> birthdayPickerProperty;
    private ObjectProperty<Boolean> maleGenderButtonProperty;
    private ObjectProperty<Boolean> femaleGenderButtonProperty;
    private StringProperty addEditProperty;
    private StringProperty saveAddButtonProperty;
    private UserManagementViewState userManagementViewState;

    public UserEditingViewModel(Model model, UserManagementViewState viewState) {
        this.model = model;
        this.userManagementViewState = viewState;

        // Initialize the instance variables responsible for storing data of the ui elements.
        addEditProperty = new SimpleStringProperty("Add User");
        saveAddButtonProperty = new SimpleStringProperty("Add");
        errorProperty = new SimpleStringProperty("");
        emailProperty = new SimpleStringProperty("");
        passwordProperty = new SimpleStringProperty("");
        firstNameProperty = new SimpleStringProperty("");
        lastNameProperty = new SimpleStringProperty("");
        birthdayPickerProperty = new SimpleObjectProperty<>();
        maleGenderButtonProperty = new SimpleObjectProperty<>();
        femaleGenderButtonProperty = new SimpleObjectProperty<>();
    }

    public void reset() {
        errorProperty.set("");
        UserViewModel selectedUser = userManagementViewState.getSelectedUser();
        if (selectedUser != null) {
            // Auto complete the fields with the selected user's data.
            emailProperty.set(selectedUser.getEmail().getValue());
            firstNameProperty.set(selectedUser.getFirstName().getValue());
            lastNameProperty.set(selectedUser.getLastName().getValue());
            passwordProperty.set(selectedUser.getPassword().getValue());
            DateTime birthday = selectedUser.getBirthDate().getValue();
            birthdayPickerProperty.setValue(LocalDate.of(birthday.getYear(), birthday.getMonth(), birthday.getDay()));
            if (selectedUser.getGender().getValue().equalsIgnoreCase("M")) {
                maleGenderButtonProperty.set(true);
                femaleGenderButtonProperty.set(false);
            } else {
                maleGenderButtonProperty.set(false);
                femaleGenderButtonProperty.set(true);
            }
            addEditProperty.set("Edit User");
            saveAddButtonProperty.set("Save User");
        } else {
            // Clear the fields and any errors whenever the window reopens.
            emailProperty.set("");
            passwordProperty.set("");
            firstNameProperty.set("");
            lastNameProperty.set("");
            addEditProperty.set("Add User");
            saveAddButtonProperty.set("Add");

            // Default selection of date picker is the present day.
            birthdayPickerProperty.set(LocalDate.now());

            // Default selection for radio buttons.
            maleGenderButtonProperty.set(true);
            femaleGenderButtonProperty.set(false);
        }
    }

    public StringProperty getErrorProperty() {
        return errorProperty;
    }

    public StringProperty getEmailProperty() {
        return emailProperty;
    }

    public StringProperty getPasswordProperty() {
        return passwordProperty;
    }

    public StringProperty getFirstNameProperty() {
        return firstNameProperty;
    }

    public StringProperty getLastNameProperty() {
        return lastNameProperty;
    }

    public StringProperty getAddEditProperty() {
        return addEditProperty;
    }

    public StringProperty getSaveAddButtonProperty() {
        return saveAddButtonProperty;
    }

    public ObjectProperty<LocalDate> getBirthdayPickerProperty() {
        return birthdayPickerProperty;
    }

    public ObjectProperty<Boolean> maleGenderButtonProperty() {
        return maleGenderButtonProperty;
    }

    public ObjectProperty<Boolean> femaleGenderButtonProperty() {
        return femaleGenderButtonProperty;
    }

    public boolean modify() {
        UserViewModel selectedUser = userManagementViewState.getSelectedUser();
        String oldEmail = "";
        boolean isEmployee = false;
        if (selectedUser != null) {
            oldEmail = selectedUser.getEmail().getValue();
            isEmployee = selectedUser.getStatus().getValue().equals("Employee");
        }
        try {
            model.updateUser(oldEmail, isEmployee ? new Employee(
                    emailProperty.getValue(),
                    passwordProperty.getValue(),
                    firstNameProperty.getValue(),
                    lastNameProperty.getValue(),
                    birthdayPickerProperty.get(),
                    maleGenderButtonProperty.getValue() ? 'M' : 'F'
            ) : new Customer(
                    emailProperty.getValue(),
                    passwordProperty.getValue(),
                    firstNameProperty.getValue(),
                    lastNameProperty.getValue(),
                    birthdayPickerProperty.get(),
                    maleGenderButtonProperty.getValue() ? 'M' : 'F'
            ));
            return true;
        } catch (Exception e) {
            errorProperty.set(e.getMessage());
            return false;
        }
    }
}
