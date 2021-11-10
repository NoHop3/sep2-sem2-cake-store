package viewmodel.object;

import common.model.DateTime;
import common.model.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserViewModel {

    private StringProperty email;
    private StringProperty password;
    private StringProperty firstName;
    private StringProperty lastName;
    private ObjectProperty<DateTime> birthDate;
    private StringProperty gender;
    private StringProperty status;

    public UserViewModel(User user) {
        email = new SimpleStringProperty(user.getEmail());
        password = new SimpleStringProperty(user.getPassword());
        firstName = new SimpleStringProperty(user.getFirstName());
        lastName = new SimpleStringProperty(user.getLastName());
        birthDate = new SimpleObjectProperty<>(user.getBirthday());
        gender = new SimpleStringProperty("" + user.getGender());
        status = new SimpleStringProperty(user.isEmployee() ? "Employee" : "Customer");
    }

    public StringProperty getEmail() {
        return email;
    }

    public StringProperty getPassword() {
        return password;
    }

    public StringProperty getFirstName() {
        return firstName;
    }

    public StringProperty getLastName() {
        return lastName;
    }

    public ObjectProperty<DateTime> getBirthDate() {
        return birthDate;
    }

    public StringProperty getGender() {
        return gender;
    }

    public StringProperty getStatus() {
        return status;
    }
}
