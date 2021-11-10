package view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import viewmodel.UserEditingViewModel;

public class UserEditingViewController extends ViewController {

    private ViewHandler viewHandler;
    private UserEditingViewModel viewModel;

    // FXML instance variables of the view.
    @FXML private Label addEditLabel;
    @FXML private Label usernameLabel;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker birthdaySelector;
    @FXML private RadioButton maleRadioButton;
    @FXML private RadioButton femaleRadioButton;
    @FXML private Label errorLabel;
    @FXML private Button saveAddButton;

    @Override
    protected void init() {
        viewHandler = getViewHandler();
        viewModel = getViewModelFactory().getUserEditingViewModel();

        // Bindings for the user interface elements.
        usernameLabel.textProperty().bind(Bindings.createStringBinding(() -> firstNameField.textProperty().getValue() + " " + lastNameField.textProperty().getValue(), firstNameField.textProperty(), lastNameField.textProperty()));
        errorLabel.textProperty().bind(viewModel.getErrorProperty());
        addEditLabel.textProperty().bind(viewModel.getAddEditProperty());
        saveAddButton.textProperty().bind(viewModel.getSaveAddButtonProperty());
        Bindings.bindBidirectional(emailField.textProperty(), viewModel.getEmailProperty());
        Bindings.bindBidirectional(passwordField.textProperty(), viewModel.getPasswordProperty());
        Bindings.bindBidirectional(firstNameField.textProperty(), viewModel.getFirstNameProperty());
        Bindings.bindBidirectional(lastNameField.textProperty(), viewModel.getLastNameProperty());
        Bindings.bindBidirectional(birthdaySelector.valueProperty(), viewModel.getBirthdayPickerProperty());
        maleRadioButton.selectedProperty().bindBidirectional(viewModel.maleGenderButtonProperty());
        femaleRadioButton.selectedProperty().bindBidirectional(viewModel.femaleGenderButtonProperty());
    }

    public void reset() {
        viewModel.reset();
    }

    @FXML
    private void modify() {
        try {
            if (viewModel.modify()) viewHandler.openView(View.USERS);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Could not modify at this time. Try later.");
        }
    }

    @FXML
    private void cancelManaging() {
        try {
            viewHandler.openView(View.USERS);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Could not cancel the modification at this time. Try later.");
        }
    }
}
