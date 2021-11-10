package view;

import common.utility.converter.IntegerToString;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import viewmodel.BasketViewModel;
import viewmodel.object.ProductViewModel;

public class BasketViewController extends ViewController {

    private ViewHandler viewHandler;
    private BasketViewModel viewModel;

    // FXML instance variables of basket table.
    @FXML private TableView<ProductViewModel> basketTable;
    @FXML private TableColumn<ProductViewModel, Integer> basketQuantityColumn;
    @FXML private TableColumn<ProductViewModel, String> basketNameColumn;
    @FXML private TableColumn<ProductViewModel, Double> basketPriceColumn;
    @FXML private TableColumn<ProductViewModel, Number> basketCostColumn;

    // The rest FXML instance variables of the view.
    @FXML private Label usernameLabel;
    @FXML private Button manageProductsButton;
    @FXML private Button manageUsersButton;
    @FXML private Label errorLabel;
    @FXML private Label priceLabel;
    @FXML private Label discountLabel;
    @FXML private TextField inputCouponField;
    @FXML private Label finalPriceLabel;
    @FXML private TextArea inputCommentField;

    @Override
    protected void init() {
        viewHandler = getViewHandler();
        viewModel = getViewModelFactory().getBasketViewModel();

        // Bindings for the basket table.
        // Responsible for handling the behaviour of the text fields when the user, for editing purposes, double clicks on the quantity of any product in the basket.
        basketQuantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override
            public String toString(Integer integer) {
                return integer == null ? "1" : integer.toString();
            }

            @Override
            public Integer fromString(String s) {
                // The call to the method below handles all the check logic when the user tries to enter a new quantity value in the text field presented to him.
                return viewModel.updateQuantity(s);
            }
        }));
        basketQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantityProperty());
        basketNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        basketPriceColumn.setCellValueFactory(cellData -> cellData.getValue().getPriceProperty());
        // Automatically updates the values in the total cost column for each product based on its current quantity and price.
        basketCostColumn.setCellValueFactory(cellData -> {
            ObjectProperty<Integer> cellProductQuantity = cellData.getValue().getQuantityProperty();
            ObjectProperty<Double> cellProductPrice = cellData.getValue().getPriceProperty();
            return Bindings.createDoubleBinding(() -> Math.floor(cellProductQuantity.getValue() * cellProductPrice.getValue() * 100) / 100, cellProductQuantity, cellProductPrice);
        });
        // Links the basket hash map of the view model to the displaying observable product list of the basket table.
        // On any product additions or removals from the view model hash map the list would behave accordingly by adding or removing the same product.
        viewModel.getBasketMap().addListener((MapChangeListener.Change<? extends String, ? extends ProductViewModel> change) -> {
            // First condition verifies the change was not triggered by a replacement of an already existing product with put() method called on the basket hash map.
            if (change.wasRemoved() ^ change.wasAdded()) if (change.wasRemoved()) {
                basketTable.getItems().remove(change.getValueRemoved());
            } else {
                basketTable.getItems().add(change.getValueAdded());
            }
        });
        basketTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> viewModel.setSelectedBasketProductProperty(newVal));

        // Bindings for the rest of the user interface elements.
        usernameLabel.textProperty().bind(viewModel.getUsernameProperty());
        manageProductsButton.visibleProperty().bind(viewModel.getShowProductManagementButtonProperty());
        manageUsersButton.visibleProperty().bind(viewModel.getShowUserManagementButtonProperty());
        errorLabel.textProperty().bind(viewModel.getErrorProperty());
        priceLabel.textProperty().bind(viewModel.getPriceProperty());
        Bindings.bindBidirectional(discountLabel.textProperty(), viewModel.getDiscountProperty(), new IntegerToString());
        Bindings.bindBidirectional(inputCouponField.textProperty(), viewModel.getInputCouponProperty());
        finalPriceLabel.textProperty().bind(viewModel.getFinalPriceProperty());
        inputCommentField.textProperty().bindBidirectional(viewModel.getInputCommentFieldProperty());
    }

    public void reset() {
        // Deselect any items upon reopening the window.
        basketTable.getSelectionModel().clearSelection();
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
    private void openProductManagementView() {
        try {
            viewHandler.openView(View.MANAGEPRODUCTS);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Can not manage products at this time. Try later.");
        }
    }

    @FXML
    private void openUserManagementView() {
        try {
            viewHandler.openView(View.USERS);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Can not manage users at this time. Try later.");
        }
    }

    @FXML public void openOrdersView() {
        try {
            viewHandler.openView(View.ORDERS);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Could not open orders at this time. Try later.");
        }
    }

    @FXML
    private void clearBasket() {
        basketTable.getSelectionModel().clearSelection();
        viewModel.clearBasket();
    }

    @FXML
    private void dropFromBasket() {
        if (viewModel.dropFromBasket()) basketTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void applyCoupon() {
        viewModel.applyCoupon();
    }

    @FXML
    private void placeOrder() {
        viewModel.placeOrder();
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
}
