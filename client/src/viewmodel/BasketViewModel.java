package viewmodel;

import common.model.Customer;
import common.model.Order;
import common.model.Product;
import common.model.User;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.Model;
import viewmodel.object.ProductViewModel;
import java.util.HashMap;

public class BasketViewModel {

    private Model model;

    // Instance variables for storing the products of the basket table.
    private ObservableMap<String, ProductViewModel> basketMap;
    private ObjectProperty<ProductViewModel> selectedBasketProductProperty;

    // Instance variables for linking and storing the other elements of the user interface.
    private StringProperty usernameProperty;
    private ObjectProperty<Boolean> showProductManagementButtonProperty;
    private ObjectProperty<Boolean> showUserManagementButtonProperty;
    private StringProperty errorProperty;
    private StringProperty priceProperty;
    private IntegerProperty discountProperty;
    private StringProperty inputCouponProperty;
    private StringProperty finalPriceProperty;
    private StringProperty inputCommentField;

    // Other helper instance variables.
    private boolean wasAuthenticatedUserQueried;

    public BasketViewModel(Model model) {
        this.model = model;

        // Initialize the view model instance variables responsible for storing the data of the table.
        basketMap = FXCollections.observableHashMap();
        selectedBasketProductProperty = new SimpleObjectProperty<>();

        // Initialize the instance variables responsible for storing data of the other ui elements.
        usernameProperty = new SimpleStringProperty("");
        showProductManagementButtonProperty = new SimpleObjectProperty<>(false);
        showUserManagementButtonProperty = new SimpleObjectProperty<>(false);
        errorProperty = new SimpleStringProperty("");
        priceProperty = new SimpleStringProperty("0.00");
        discountProperty = new SimpleIntegerProperty(0);
        inputCouponProperty = new SimpleStringProperty("");
        finalPriceProperty = new SimpleStringProperty("0.00");
        inputCommentField = new SimpleStringProperty();

        // Initialize the rest of the instance variables.
        wasAuthenticatedUserQueried = false;
    }

    public void reset() {
        errorProperty.set("");

        // Deselect any selected items if window reopens.
        selectedBasketProductProperty.set(null);

        // Refresh, every time the window reopens, the basket table with all the products the user added to the basket in his current logged session.
        basketMap.clear();
        model.getAllProductsInBasket().forEach((product) -> basketMap.put(product.getId(), new ProductViewModel(product)));

        // Configure properly the product and user management and the username label based if the user is an employee or not.
        if (!wasAuthenticatedUserQueried) {
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
            wasAuthenticatedUserQueried = true;
        }

        // Update all the price labels in the footer.
        updatePrices();
    }

    public ObservableMap<String, ProductViewModel> getBasketMap() {
        return basketMap;
    }

    public StringProperty getUsernameProperty() {
        return usernameProperty;
    }

    public ObjectProperty<Boolean> getShowProductManagementButtonProperty() {
        return showProductManagementButtonProperty;
    }

    public ObjectProperty<Boolean> getShowUserManagementButtonProperty() {
        return showUserManagementButtonProperty;
    }

    public StringProperty getErrorProperty() {
        return errorProperty;
    }

    public StringProperty getPriceProperty() {
        return priceProperty;
    }

    public IntegerProperty getDiscountProperty() {
        return discountProperty;
    }

    public StringProperty getInputCouponProperty() {
        return inputCouponProperty;
    }

    public StringProperty getFinalPriceProperty() {
        return finalPriceProperty;
    }

    public StringProperty getInputCommentFieldProperty() {
        return inputCommentField;
    }

    public void setSelectedBasketProductProperty(ProductViewModel productViewModel) {
        selectedBasketProductProperty.set(productViewModel);
    }

    public void clearBasket() {
        selectedBasketProductProperty.set(null);
        model.clearBasket();
        basketMap.clear();
        inputCommentField.setValue("");
        updatePrices();
    }

    public boolean dropFromBasket() {
        ProductViewModel selectedBasketProductViewModel = selectedBasketProductProperty.get();
        if (selectedBasketProductViewModel == null) {
            errorProperty.set("Please select a product from the basket to be removed.");
            return false;
        }
        selectedBasketProductProperty.set(null);
        String tmp = selectedBasketProductViewModel.getIdProperty().getValue();
        basketMap.remove(tmp);
        model.removeProductFromBasket(tmp);
        updatePrices();
        return true;
    }

    public void applyCoupon() {
        String couponCode = inputCouponProperty.get();
        if (couponCode == null || couponCode.isEmpty()) {
            errorProperty.set("Please enter a valid coupon code.");
            return;
        }
        // TODO: Needs further development of the system.
        // Some method to get the discount value based on the coupon code.
        // discountProperty.set(someMethod);
        updatePrices();
    }

    public void placeOrder() {
        HashMap<Product, Integer> productList = new HashMap<>();
        model.getAllProductsInBasket().forEach(product -> productList.put(product, product.getQuantity()));
        try {
            model.placeOrder(new Order(productList, (Customer) model.getAuthenticatedUser(), inputCommentField.get()));
            clearBasket();
        } catch (Exception e) {
            errorProperty.set(e.getMessage());
        }
    }

    public boolean deauthenticate() {
        wasAuthenticatedUserQueried = false;
        return model.deauthenticate();
    }

    public Integer updateQuantity(String newQuantity) {
        ProductViewModel selectedBasketProduct = selectedBasketProductProperty.getValue();
        try {
            int toReturn = Integer.parseInt(newQuantity);
            if (toReturn < 1) throw new NumberFormatException();
            int availableQuantity;
            try {
                availableQuantity = model.getProductById(selectedBasketProduct.getIdProperty().getValue()).getQuantity();
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
           if (toReturn > availableQuantity) throw new IllegalArgumentException("Unavailable stock. The quantity must be within " + availableQuantity + " units.");
           model.replaceProductInBasket(new Product(
                   selectedBasketProduct.getIdProperty().getValue(),
                   toReturn,
                   selectedBasketProduct.getNameProperty().getValue(),
                   selectedBasketProduct.getDescriptionProperty().getValue(),
                   selectedBasketProduct.getPriceProperty().getValue()
           ));
            basketMap.get(selectedBasketProduct.getIdProperty().getValue()).setQuantity(toReturn);
            updatePrices();
            return toReturn;
        } catch (NumberFormatException e) {
            errorProperty.set("Input a valid positive number.");
        } catch (IllegalArgumentException e) {
            errorProperty.set(e.getMessage());
        }
        return selectedBasketProduct.getQuantityProperty().getValue();
    }

    private void updatePrices() {
        final double[] preliminaryPrice = {0.0};
        model.getAllProductsInBasket().forEach(product -> preliminaryPrice[0] += product.getPrice() * product.getQuantity());
        priceProperty.set(String.format("%.2f", preliminaryPrice[0]));
        int discount = discountProperty.getValue();
        finalPriceProperty.set(String.format("%.2f", discount == 0 ? preliminaryPrice[0] : preliminaryPrice[0] * discount / 100));
    }
}
