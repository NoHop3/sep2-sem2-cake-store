package view;

public enum View {

    AUTHENTICATION("fxml/AuthenticationView.fxml"),
    REGISTRATION("fxml/RegistrationView.fxml"),
    CATALOG("fxml/CatalogView.fxml"),
    BASKET("fxml/BasketView.fxml"),
    MANAGEPRODUCTS("fxml/ProductManagementView.fxml"),
    EDITPRODUCTS("fxml/ProductEditingView.fxml"),
    USERS("fxml/UserManagementView.fxml"),
    MANAGEUSERS("fxml/UserEditingView.fxml"),
    ORDERS("fxml/OrdersView.fxml");


    private String fxmlFile;
    private ViewController viewController;

    View(String fxmlFile) {
        this.fxmlFile = fxmlFile;
    }

    public String getFxmlFile() {
        return fxmlFile;
    }
}
