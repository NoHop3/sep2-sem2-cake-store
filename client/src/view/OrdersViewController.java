package view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXToggleButton;
import common.model.DateTime;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import viewmodel.object.OrderViewModel;
import viewmodel.OrdersViewModel;
import viewmodel.object.ProductViewModel;

public class OrdersViewController extends ViewController {

    private ViewHandler viewHandler;
    private OrdersViewModel viewModel;

    // FXML instance variables of order table.
    @FXML private TableView<OrderViewModel> orderTable;
    @FXML private TableColumn<OrderViewModel, String> orderIDColumn;
    @FXML private TableColumn<OrderViewModel, DateTime> orderDateColumn;
    @FXML private TableColumn<OrderViewModel, String> orderEmailColumn;
    @FXML private TableColumn<OrderViewModel, String> orderCommentColumn;
    @FXML private TableColumn<OrderViewModel, String> orderStatusColumn;

    // FXML instance variables of order detailed table.
    @FXML private TableView<ProductViewModel> orderDetailedTable;
    @FXML private TableColumn<ProductViewModel, Integer> orderDetailedQuantityColumn;
    @FXML private TableColumn<ProductViewModel, String> orderDetailedNameColumn;

    // The rest FXML instance variables of the view.
    @FXML private Label usernameLabel;
    @FXML private Button basketButton;
    @FXML private Button manageProductsButton;
    @FXML private Button manageUsersButton;
    @FXML private Button markAsCompletedButton;
    @FXML private JFXToggleButton toggleOrderButton;
    @FXML private Label errorLabel;

    @Override
    protected void init() {
        viewHandler = getViewHandler();
        viewModel = getViewModelFactory().getOrdersViewModel();

        // Bindings for the order product list table.
        orderDetailedQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantityProperty());
        orderDetailedNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());

        // Bindings for the order table.
        orderIDColumn.setCellValueFactory(cellData -> cellData.getValue().getId());
        orderDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDate());
        orderEmailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmail());
        orderCommentColumn.setCellValueFactory(cellData -> cellData.getValue().getComment());
        orderStatusColumn.setCellValueFactory(cellData -> cellData.getValue().getStatus());
        orderTable.setItems(viewModel.getAllOrderList());
        orderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            viewModel.setSelectedOrderProperty(newVal);
            if (newVal != null)  orderDetailedTable.setItems(newVal.getProductList());

        });

        // Bindings for the rest of the user interface elements.
        usernameLabel.textProperty().bind(viewModel.getUsernameProperty());
        basketButton.textProperty().bind(viewModel.getBasketButtonTitleProperty());
        manageProductsButton.visibleProperty().bind(viewModel.getShowProductManagementButtonProperty());
        manageUsersButton.visibleProperty().bind(viewModel.getShowUserManagementButtonProperty());
        markAsCompletedButton.visibleProperty().bind(viewModel.getShowMarkAsCompletedButtonProperty());
        toggleOrderButton.selectedProperty().bindBidirectional(viewModel.getToggleButtonProperty());
        toggleOrderButton.setText("PENDING");
        errorLabel.textProperty().bind(viewModel.getErrorProperty());
    }

    @Override
    protected void reset() {
        // Deselect any items upon reopening the window.
        orderTable.getSelectionModel().clearSelection();
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
            viewModel.getErrorProperty().set("Can not manage the basket at this time. Try later.");
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

    @FXML
    public void markAsCompleted() {
        viewModel.markAsCompleted();
    }


    @FXML
    public void changeOrderTable() {
        if (toggleOrderButton.isSelected()) {
            toggleOrderButton.contentDisplayProperty().set(ContentDisplay.RIGHT);
            toggleOrderButton.setText("ALL");
            orderTable.setItems(viewModel.getPendingOrderList());
        } else {
            toggleOrderButton.setText("PENDING");
            toggleOrderButton.contentDisplayProperty().set(ContentDisplay.LEFT);
            orderTable.setItems(viewModel.getAllOrderList());
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
}
