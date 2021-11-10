package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import viewmodel.ProductManagementViewModel;
import viewmodel.object.ProductViewModel;

public class ProductManagementViewController extends ViewController {

    private ViewHandler viewHandler;
    private ProductManagementViewModel viewModel;

    // FXML instance variables of catalog table.
    @FXML private TableView<ProductViewModel> catalogTable;
    @FXML private TableColumn<ProductViewModel, Integer> catalogAvailabilityColumn;
    @FXML private TableColumn<ProductViewModel, String> catalogNameColumn;
    @FXML private TableColumn<ProductViewModel, String> catalogDescriptionColumn;
    @FXML private TableColumn<ProductViewModel, Double> catalogPriceColumn;

    // The rest FXML instance variables of the view.
    @FXML private Label usernameLabel;
    @FXML private Button basketButton;
    @FXML private Button managementButton;
    @FXML private Label errorLabel;

    @Override
    protected void init() {
        viewHandler = getViewHandler();
        viewModel = getViewModelFactory().getProductManagementViewModel();

        // Bindings for the catalog table.
        catalogAvailabilityColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantityProperty());
        catalogDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        catalogNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        catalogPriceColumn.setCellValueFactory(cellData -> cellData.getValue().getPriceProperty());
        catalogTable.setItems(viewModel.getCatalogTable());
        catalogTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            managementButton.setText((newVal == null ? "ADD" : "EDIT") + " PRODUCT");
            viewModel.setSelectedCatalogProductProperty(newVal);
        });

        // Bindings for the rest of the user interface elements.
        usernameLabel.textProperty().bind(viewModel.getUsernameProperty());
        basketButton.textProperty().bind(viewModel.getBasketButtonTitleProperty());
        errorLabel.textProperty().bind(viewModel.getErrorProperty());
    }

    @Override
    protected void reset() {
        catalogTable.getSelectionModel().clearSelection();
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
            viewModel.getErrorProperty().set("Can not manage basket at this time. Try later.");
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
    public void openOrdersView() {
        try {
            viewHandler.openView(View.ORDERS);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Can not view orders at this time. Try later.");
        }
    }

    @FXML
    public void manageProduct() {
        try {
            viewHandler.openView(View.EDITPRODUCTS);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Can not edit products at this time. Try later.");
        }
    }

    @FXML
    public void deleteProduct() {
        if (viewModel.deleteProduct()) catalogTable.getSelectionModel().clearSelection();
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
