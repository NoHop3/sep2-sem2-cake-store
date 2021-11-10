package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import viewmodel.ProductEditingViewModel;

public class ProductEditingViewController extends ViewController {

    private ViewHandler viewHandler;
    private ProductEditingViewModel viewModel;

    @FXML public Label errorLabel;
    @FXML public TextField quantityField;
    @FXML public TextField nameField;
    @FXML public TextArea descriptionField;
    @FXML public TextField priceField;
    @FXML private Button manageButton;

    @Override
    protected void init() {
        this.viewHandler = getViewHandler();
        this.viewModel = getViewModelFactory().getProductEditingViewModel();

        errorLabel.textProperty().bind(viewModel.getErrorProperty());
        quantityField.textProperty().bindBidirectional(viewModel.getQuantityProperty(), new StringConverter<>() {
            @Override
            public String toString(Integer integer) {
                return integer == null ? "" : integer.toString();
            }

            @Override
            public Integer fromString(String s) {
                return viewModel.quantityChecker(s);
            }
        });
        nameField.textProperty().bindBidirectional(viewModel.getNameProperty());
        descriptionField.textProperty().bindBidirectional(viewModel.getDescriptionProperty());
        priceField.textProperty().bindBidirectional(viewModel.getPriceProperty(), new StringConverter<>() {
            @Override
            public String toString(Double aDouble) {
                return aDouble == null ? "" : aDouble.toString();
            }

            @Override
            public Double fromString(String s) {
                return viewModel.priceChecker(s);
            }
        });
        manageButton.textProperty().bind(viewModel.getManageButtonTitleProperty());
    }

    @Override
    protected void reset() {
        viewModel.reset();
    }

    public void manageProduct() {
        try {
            if (viewModel.editProduct()) viewHandler.openView(View.MANAGEPRODUCTS);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Could not manage the product at this time. Try later.");
        }
    }

    public void cancel() {
        try {
            viewHandler.openView(View.MANAGEPRODUCTS);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Could not cancel at this time. Try later.");
        }
    }
}
