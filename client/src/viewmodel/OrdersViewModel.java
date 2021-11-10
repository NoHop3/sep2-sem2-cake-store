package viewmodel;

import common.model.Order;
import common.model.User;
import common.utility.observer.event.ObserverEvent;
import common.utility.observer.listener.LocalListener;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import model.Model;
import viewmodel.object.OrderViewModel;

public class OrdersViewModel implements LocalListener<String, Object> {

    private Model model;

    // Instance variables for storing the orders of the order table.
    private ObservableList<OrderViewModel> allOrderList;
    private FilteredList<OrderViewModel> pendingOrderList;
    private ObjectProperty<OrderViewModel> selectedOrderProperty;

    // Instance variables for linking and storing the other elements of the user interface.
    private StringProperty usernameProperty;
    private StringProperty basketButtonTitleProperty;
    private ObjectProperty<Boolean> showProductManagementButtonProperty;
    private ObjectProperty<Boolean> showUserManagementButtonProperty;
    private ObjectProperty<Boolean> showMarkAsCompletedButtonProperty;
    private ObjectProperty<Boolean> toggleButtonProperty;
    private StringProperty errorProperty;

    public OrdersViewModel(Model model) {
        this.model = model;

        // Initialize the view model instance variables responsible for storing the data of the tables.
        allOrderList = FXCollections.observableArrayList();
        pendingOrderList = new FilteredList<>(allOrderList, orderViewModel -> orderViewModel.getStatus().getValue().equalsIgnoreCase("pending"));
        selectedOrderProperty = new SimpleObjectProperty<>();

        // Initialize the instance variables responsible for storing data of the other ui elements.
        usernameProperty = new SimpleStringProperty("");
        basketButtonTitleProperty = new SimpleStringProperty("Basket");
        showProductManagementButtonProperty = new SimpleObjectProperty<>(false);
        showUserManagementButtonProperty = new SimpleObjectProperty<>(false);
        showMarkAsCompletedButtonProperty = new SimpleObjectProperty<>(false);
        toggleButtonProperty = new SimpleObjectProperty<>(false);
        errorProperty = new SimpleStringProperty("");

        model.addListener(this);
    }

    public void reset() {
        errorProperty.set("");

        // Deselect any selected items if window reopens.
        selectedOrderProperty.set(null);

        if (!model.wasDataQueriedFor("Orders")) {
            allOrderList.clear();
            try {
                model.getAllOrders().forEach(order -> allOrderList.add(new OrderViewModel(order)));
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
            showMarkAsCompletedButtonProperty.set(isEmployee);
        }

        // Updates the number of products indicator in the basket button title.
        int tmp = model.getAllProductsInBasket().size();
        basketButtonTitleProperty.set(tmp == 0 ? "Basket" : "Basket (" + tmp + ")");
    }


    public ObservableList<OrderViewModel> getAllOrderList() {
        return allOrderList;
    }

    public FilteredList<OrderViewModel> getPendingOrderList() {
        return pendingOrderList;
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

    public ObjectProperty<Boolean> getShowMarkAsCompletedButtonProperty() {
        return showMarkAsCompletedButtonProperty;
    }

    public ObjectProperty<Boolean> getToggleButtonProperty() {
        return toggleButtonProperty;
    }

    public StringProperty getErrorProperty() {
        return errorProperty;
    }

    public void setSelectedOrderProperty(OrderViewModel orderProperty) {
        selectedOrderProperty.set(orderProperty);
    }

    public void markAsCompleted() {
        try {
            OrderViewModel selectedOrder = selectedOrderProperty.getValue();
            if (selectedOrder == null) throw new Exception("Select an order to mark as completed.");
            model.updateOrderStatus(selectedOrder.getId().getValue(), "completed");
        } catch (Exception e) {
            errorProperty.set(e.getMessage());
        }
    }

    public boolean deauthenticate() {
        return model.deauthenticate();
    }

    @Override
    public void propertyChange(ObserverEvent<String, Object> event) {
        Platform.runLater(() -> {
            switch (event.getPropertyName()) {
                case "newOrder" : {
                    allOrderList.add(new OrderViewModel((Order) event.getValue2()));
                    break;
                }
                case "completedOrder" : {
                    OrderViewModel completedOrder = new OrderViewModel((Order) event.getValue2());
                    OrderViewModel toRemove = null;
                    for (OrderViewModel order : allOrderList) if (completedOrder.equals(order)) {
                        toRemove = order;
                        break;
                    }
                    if (toRemove != null) {
                        toRemove.getStatus().set("completed");
                        allOrderList.remove(toRemove);
                    }
                    allOrderList.add(toRemove);
                    break;
                }
            }
        });
    }
}
