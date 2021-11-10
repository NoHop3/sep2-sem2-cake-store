package viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Model;

public class AuthenticationViewModel {

    private Model model;

    // Instance variables for linking and storing elements of the user interface.
    private StringProperty emailProperty;
    private StringProperty passwordProperty;
    private StringProperty errorProperty;

    public AuthenticationViewModel(Model model) {
        this.model = model;

        // Initialize the instance variables responsible for storing data of the ui elements.
        emailProperty = new SimpleStringProperty("");
        passwordProperty = new SimpleStringProperty("");
        errorProperty = new SimpleStringProperty("");
    }

    public void reset() {
        // Clear the fields and any errors whenever the window reopens.
        emailProperty.set("");
        passwordProperty.set("");
        errorProperty.set("");
    }

    public StringProperty getEmailProperty() {
        return emailProperty;
    }

    public StringProperty getPasswordProperty() {
        return passwordProperty;
    }

    public StringProperty getErrorProperty() {
        return errorProperty;
    }

    public boolean authenticate() {
        boolean toReturn;
        try {
            model.authenticate(emailProperty.getValue(), passwordProperty.getValue());
            toReturn = true;
        } catch (Exception e) {
            errorProperty.set(e.getMessage());
            toReturn = false;
        }
        emailProperty.set("");
        passwordProperty.set("");
        return toReturn;
    }
}
