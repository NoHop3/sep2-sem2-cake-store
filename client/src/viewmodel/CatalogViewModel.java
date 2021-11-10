package viewmodel;

import common.model.Order;
import common.model.Product;
import common.model.User;
import common.utility.observer.event.ObserverEvent;
import common.utility.observer.listener.LocalListener;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Model;
import viewmodel.object.OrderViewModel;
import viewmodel.object.ProductViewModel;

public class CatalogViewModel implements LocalListener<String, Object> {

    private Model model;

    // Instance variables for storing the products of the catalog table.
    private ObservableList<ProductViewModel> catalogTable;
    private ObjectProperty<ProductViewModel> selectedCatalogProductProperty;

    // Instance variables for linking and storing the other elements of the user interface.
    private StringProperty usernameProperty;
    private StringProperty basketButtonTitleProperty;
    private ObjectProperty<Boolean> showProductManagementButtonProperty;
    private ObjectProperty<Boolean> showUserManagementButtonProperty;
    private ObjectProperty<Boolean> showEventFieldProperty;
    private ObjectProperty<Boolean> showEventButtonProperty;
    private StringProperty eventTextProperty;
    private StringProperty errorProperty;

    // Other helper instance variables.
    private ProductViewModel newNotificationProduct;
    private ObjectProperty<Boolean> showNotificationProperty;
    private ObjectProperty<Boolean> showEventProperty;
    private ObjectProperty<Boolean> showCompletedOrderProperty;
    private String newEventText;
    private OrderViewModel completedOrder;

    public CatalogViewModel(Model model) {
        this.model = model;

        // Initialize the view model instance variables responsible for storing the data of the tables.
        catalogTable = FXCollections.observableArrayList();
        selectedCatalogProductProperty = new SimpleObjectProperty<>();

        // Initialize the instance variables responsible for storing data of the other ui elements.
        usernameProperty = new SimpleStringProperty("");
        basketButtonTitleProperty = new SimpleStringProperty("Basket");
        showProductManagementButtonProperty = new SimpleObjectProperty<>(false);
        showUserManagementButtonProperty = new SimpleObjectProperty<>(false);
        showEventFieldProperty = new SimpleObjectProperty<>(false);
        showEventButtonProperty = new SimpleObjectProperty<>(false);
        eventTextProperty = new SimpleStringProperty();
        errorProperty = new SimpleStringProperty("");

        // Initialize the rest of the instance variables.
        newNotificationProduct = null;
        showNotificationProperty = new SimpleObjectProperty<>(false);
        showEventProperty = new SimpleObjectProperty<>(false);
        showCompletedOrderProperty = new SimpleObjectProperty<>(false);
        newEventText = "";
        completedOrder = null;

        model.addListener(this);
    }

    public void reset() {
        errorProperty.set("");

        // Deselect any selected items if window reopens.
        selectedCatalogProductProperty.set(null);

        if (!model.wasDataQueriedFor("Catalog")) {
            catalogTable.clear();
            try {
                model.getCatalogOfProducts().forEach(product -> catalogTable.add(new ProductViewModel(product)));
            } catch (Exception e) {
                errorProperty.set(e.getMessage());
            }
            boolean isEmployee = false;
            try {
                User authenticatedUser = model.getAuthenticatedUser();
                isEmployee = authenticatedUser.isEmployee();
                usernameProperty.set((isEmployee ? "Employee" : "Customer") + " ● " + authenticatedUser.getFullName());
            } catch (Exception e) {
                usernameProperty.set("");
                errorProperty.set(e.getMessage());
            }
            showProductManagementButtonProperty.set(isEmployee);
            showUserManagementButtonProperty.set(isEmployee);
            showEventFieldProperty.set(isEmployee);
            showEventButtonProperty.set(isEmployee);
        }

        // Updates the number of products indicator in the basket button title.
        int tmp = model.getAllProductsInBasket().size();
        basketButtonTitleProperty.set(tmp == 0 ? "Basket" : "Basket (" + tmp + ")");
    }

    public ObservableList<ProductViewModel> getCatalogTable() {
        return catalogTable;
    }

    public StringProperty getUsernameProperty() {
        return usernameProperty;
    }

    public StringProperty getBasketButtonTitleProperty() {
        return basketButtonTitleProperty;
    }

    public ObjectProperty<Boolean> getShowProductManagementButtonProperty() {
        return showProductManagementButtonProperty;
    }

    public ObjectProperty<Boolean> getShowUserManagementButtonProperty() {
        return showUserManagementButtonProperty;
    }

    public ObjectProperty<Boolean> getShowEventFieldProperty() {
        return showEventFieldProperty;
    }

    public ObjectProperty<Boolean> getShowEventButtonProperty() {
        return showEventButtonProperty;
    }

    public StringProperty getEventTextProperty() {
        return eventTextProperty;
    }

    public StringProperty getErrorProperty() {
        return errorProperty;
    }

    public ProductViewModel getNewNotificationProduct() {
        return newNotificationProduct;
    }

    public ObjectProperty<Boolean> getShowNotificationProperty() {
        return showNotificationProperty;
    }

    public ObjectProperty<Boolean> getShowEventProperty() {
        return showEventProperty;
    }

    public ObjectProperty<Boolean> getShowCompletedOrderProperty() {
        return showCompletedOrderProperty;
    }

    public String getNewEventText() {
        return newEventText;
    }

    public OrderViewModel getCompletedOrder() {
        return completedOrder;
    }

    public void setSelectedCatalogProductProperty(ProductViewModel productViewModel) {
        selectedCatalogProductProperty.set(productViewModel);
    }

    public void addToBasket() {
        ProductViewModel selectedCatalogProductViewModel = selectedCatalogProductProperty.get();
        if (selectedCatalogProductViewModel == null) {
            errorProperty.set("Please select a product from the catalog to be added to the cart.");
            return;
        }
        try {
            model.addProductToBasket(new Product(
                    selectedCatalogProductViewModel.getIdProperty().getValue(),
                    1,
                    selectedCatalogProductViewModel.getNameProperty().getValue(),
                    selectedCatalogProductViewModel.getDescriptionProperty().getValue(),
                    selectedCatalogProductViewModel.getPriceProperty().getValue()
            ));
            basketButtonTitleProperty.set("Basket (" + model.getAllProductsInBasket().size() + ")");
        } catch (Exception e) {
            errorProperty.set(e.getMessage());
        }
    }

    public void sendNotification() {
        try {
            model.sendEventNotification(eventTextProperty.getValue());
            eventTextProperty.set("");
        } catch (Exception e) {
            errorProperty.set("Could not send notification at this time. Try later.");
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
                    if (event.getValue1().isEmpty()) {
                        newNotificationProduct = new ProductViewModel((Product) event.getValue2());
                        showNotificationProperty.set(true);
                    }
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
                case "newEvent" : {
                    newEventText = event.getValue1();
                    showEventProperty.set(true);
                    break;
                }
                case "completedOrder" : {
                    try {
                        if (event.getValue1().equals(model.getAuthenticatedUser().getEmail())) {
                            completedOrder = new OrderViewModel((Order) event.getValue2());
                            showCompletedOrderProperty.set(true);
                        }
                    } catch (Exception e) {
                        // Do nothing.
                    }
                    break;
                }
            }
        });
    }
}
