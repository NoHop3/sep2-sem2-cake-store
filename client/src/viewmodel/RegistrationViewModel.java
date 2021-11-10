package viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Model;
import java.time.LocalDate;

public class RegistrationViewModel {

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

    public RegistrationViewModel(Model model) {
        this.model = model;

        // Initialize the instance variables responsible for storing data of the ui elements.
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
        // Clear the fields and any errors whenever the window reopens.
        errorProperty.set("");
        emailProperty.set("");
        passwordProperty.set("");
        firstNameProperty.set("");
        lastNameProperty.set("");

        // Default selection of date picker is the present day.
        birthdayPickerProperty.set(LocalDate.now());

        // Default selection for radio buttons.
        maleGenderButtonProperty.set(true);
        femaleGenderButtonProperty.set(false);
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

    public ObjectProperty<LocalDate> getBirthdayPickerProperty() {
        return birthdayPickerProperty;
    }

    public ObjectProperty<Boolean> maleGenderButtonProperty() {
        return maleGenderButtonProperty;
    }

    public ObjectProperty<Boolean> femaleGenderButtonProperty() {
        return femaleGenderButtonProperty;
    }

    public boolean register() {
        try {
            model.register(emailProperty.getValue(), passwordProperty.getValue(), firstNameProperty.getValue(), lastNameProperty.getValue(), birthdayPickerProperty.getValue(), maleGenderButtonProperty.getValue() ? 'm' : 'f');
            return true;
        } catch (Exception e) {
            errorProperty.set(e.getMessage());
            return false;
        }
    }
}
