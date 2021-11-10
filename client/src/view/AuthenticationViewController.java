package view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import viewmodel.AuthenticationViewModel;

public class AuthenticationViewController extends ViewController {

    private ViewHandler viewHandler;
    private AuthenticationViewModel viewModel;

    // FXML instance variables of the view.
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @Override
    protected void init() {
        viewHandler = getViewHandler();
        viewModel = getViewModelFactory().getAuthenticationViewModel();

        // Bindings for the user interface elements.
        Bindings.bindBidirectional(emailField.textProperty(), viewModel.getEmailProperty());
        Bindings.bindBidirectional(passwordField.textProperty(), viewModel.getPasswordProperty());
        errorLabel.textProperty().bind(viewModel.getErrorProperty());
    }

    public void reset() {
        viewModel.reset();
    }

    @FXML
    private void authenticate() {
        if (viewModel.authenticate()) try {
            viewHandler.openView(View.CATALOG);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Could not authenticate at this time. Try later.");
        }
    }

    @FXML
    private void openRegistrationView() {
        try {
            viewHandler.openView(View.REGISTRATION);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Could not register at this time. Try later.");
        }
    }
}
