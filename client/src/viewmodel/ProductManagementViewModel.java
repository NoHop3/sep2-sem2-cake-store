package viewmodel;

import common.model.Product;
import common.utility.observer.event.ObserverEvent;
import common.utility.observer.listener.LocalListener;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Model;
import viewmodel.object.ProductViewModel;
import viewmodel.viewstate.ProductManagementViewState;

public class ProductManagementViewModel implements LocalListener<String, Object> {

    private Model model;
    private ProductManagementViewState viewState;

    // Instance variables for linking and storing the other elements of the user interface.
    private StringProperty usernameProperty;
    private StringProperty basketButtonTitleProperty;
    private ObservableList<ProductViewModel> catalogTable;
    private StringProperty errorProperty;

    public ProductManagementViewModel(Model model, ProductManagementViewState viewState) {
        this.model = model;
        this.viewState = viewState;

        // Initialize the instance variables responsible for storing data of the other ui elements.
        usernameProperty = new SimpleStringProperty("");
        basketButtonTitleProperty = new SimpleStringProperty("Basket");
        catalogTable = FXCollections.observableArrayList();
        errorProperty = new SimpleStringProperty("");

        model.addListener(this);
    }

    public void reset() {
        errorProperty.set("");

        // Deselect any selected items if window reopens.
        viewState.setSelectedProduct(null);

        if (!model.wasDataQueriedFor("ProductManagement")) {
            catalogTable.clear();
            try {
                model.getCatalogOfProducts().forEach(product -> catalogTable.add(new ProductViewModel(product)));
            } catch (Exception e) {
                errorProperty.set(e.getMessage());
            }
            try {
                usernameProperty.set("Employee ● " + model.getAuthenticatedUser().getFullName());
            } catch (Exception e) {
                usernameProperty.set("");
                errorProperty.set(e.getMessage());
            }
        }

        // Updates the number of products indicator in the basket button title.
        int tmp = model.getAllProductsInBasket().size();
        basketButtonTitleProperty.set(tmp == 0 ? "Basket" : "Basket (" + tmp + ")");
    }

    public StringProperty getUsernameProperty() {
        return usernameProperty;
    }

    public StringProperty getBasketButtonTitleProperty() {
        return basketButtonTitleProperty;
    }

    public ObservableList<ProductViewModel> getCatalogTable() {
        return catalogTable;
    }

    public StringProperty getErrorProperty() {
        return errorProperty;
    }

    public void setSelectedCatalogProductProperty(ProductViewModel productViewModel) {
        viewState.setSelectedProduct(productViewModel);
    }

    public boolean deleteProduct() {
        ProductViewModel selectedProductViewModel = viewState.getSelectedProduct();
        if (selectedProductViewModel == null) {
            errorProperty.set("Please select a product from the catalog to be removed.");
            return false;
        }
        viewState.setSelectedProduct(null);
        try {
            model.removeProduct(new Product(
                    selectedProductViewModel.getIdProperty().getValue(),
                    selectedProductViewModel.getQuantityProperty().getValue(),
                    selectedProductViewModel.getNameProperty().getValue(),
                    selectedProductViewModel.getDescriptionProperty().getValue(),
                    selectedProductViewModel.getPriceProperty().getValue()
            ));
            return true;
        } catch (Exception e) {
            errorProperty.set(e.getMessage());
            return false;
        }
    }

    public boolean deauthenticate() {
        return model.deauthenticate();
    }

    @Override
    public void propertyChange(ObserverEvent<String, Object> event) {
        Platform.runLater(() -> {
            switch (event.getPropertyName()) {
                case "newProduct" : {
                    catalogTable.add(new ProductViewModel((Product) event.getValue2()));
                    break;
                }
                case "replacedProduct" : {
                    ProductViewModel replacedProduct = new ProductViewModel((Product) event.getValue2());
                    for (ProductViewModel product : catalogTable) if (replacedProduct.equals(product)) {
                        product.getQuantityProperty().set(replacedProduct.getQuantityProperty().getValue());
                        product.getNameProperty().set(replacedProduct.getNameProperty().getValue());
                        product.getDescriptionProperty().set(replacedProduct.getDescriptionProperty().getValue());
                        product.getPriceProperty().set(replacedProduct.getPriceProperty().getValue());
                        return;
                    }
                    break;
                }
                case "deletedProduct" : {
                    catalogTable.remove(new ProductViewModel((Product) event.getValue2()));
                    break;
                }
            }
        });
    }
}
