package viewmodel;

import common.model.Product;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Model;
import viewmodel.object.ProductViewModel;
import viewmodel.viewstate.ProductManagementViewState;

public class ProductEditingViewModel {

    private Model model;
    private ProductManagementViewState viewState;

    private StringProperty errorProperty;
    private ObjectProperty<Integer> quantityProperty;
    private StringProperty nameProperty;
    private StringProperty descriptionProperty;
    private ObjectProperty<Double> priceProperty;
    private StringProperty manageButtonTitleProperty;

    public ProductEditingViewModel(Model model, ProductManagementViewState viewState) {
        this.model = model;
        this.viewState = viewState;

        errorProperty = new SimpleStringProperty("Edit Product");
        quantityProperty = new SimpleObjectProperty<>();
        nameProperty = new SimpleStringProperty();
        descriptionProperty = new SimpleStringProperty();
        priceProperty = new SimpleObjectProperty<>();
        manageButtonTitleProperty = new SimpleStringProperty();
    }

    public StringProperty getErrorProperty() {
        return errorProperty;
    }

    public ObjectProperty<Integer> getQuantityProperty() {
        return quantityProperty;
    }

    public StringProperty getNameProperty() {
        return nameProperty;
    }

    public StringProperty getDescriptionProperty() {
        return descriptionProperty;
    }

    public ObjectProperty<Double> getPriceProperty() {
        return priceProperty;
    }

    public StringProperty getManageButtonTitleProperty() {
        return manageButtonTitleProperty;
    }

    public void reset() {
        errorProperty.set("Edit Product");
        ProductViewModel dataToFill = viewState.getSelectedProduct();
        if (dataToFill == null) {
            errorProperty.set("Add Product");
            quantityProperty.set(null);
            nameProperty.set("");
            descriptionProperty.set("");
            priceProperty.set(null);
            manageButtonTitleProperty.set("ADD");
        } else {
            errorProperty.set("Edit Product");
            quantityProperty.set(dataToFill.getQuantityProperty().getValue());
            nameProperty.set(dataToFill.getNameProperty().getValue());
            descriptionProperty.set(dataToFill.getDescriptionProperty().getValue());
            priceProperty.set(dataToFill.getPriceProperty().getValue());
            manageButtonTitleProperty.set("EDIT");
        }
    }

    public boolean editProduct() {
        try {
            if (quantityProperty.getValue() == -1) throw new IllegalArgumentException("Input a valid quantity.");
            if (priceProperty.getValue() == -1.00) throw new IllegalArgumentException("Input a valid price.");
            if (viewState.getSelectedProduct() == null) {
                model.addProduct(quantityProperty.getValue(), nameProperty.getValue(), descriptionProperty.getValue(), priceProperty.getValue());
            } else {
                model.updateProduct(new Product(
                        viewState.getSelectedProduct().getIdProperty().getValue(),
                        quantityProperty.getValue(),
                        nameProperty.getValue(),
                        descriptionProperty.getValue(),
                        priceProperty.getValue()
                ));
            }
            return true;
        } catch (Exception e) {
            errorProperty.set(e.getMessage());
            return false;
        }
    }

    public Integer quantityChecker(String input) {
        try {
            int toReturn = Integer.parseInt(input);
            if (toReturn < 1) throw new IllegalArgumentException("Quantity must be higher then 1.");
            return toReturn;
        } catch (NumberFormatException e) {
            errorProperty.set("Input a valid positive integer for quantity.");
        } catch (IllegalArgumentException e) {
            errorProperty.set(e.getMessage());
        }
        return -1;
    }

    public Double priceChecker(String input) {
        try {
            double toReturn = Double.parseDouble(input);
            if (toReturn <= 0) throw new IllegalArgumentException("Price must be higher then 0.");
            return toReturn;
        } catch (NumberFormatException e) {
            errorProperty.set("Input a valid positive number for price.");
        } catch (IllegalArgumentException e) {
            errorProperty.set(e.getMessage());
        }
        return -1.00;
    }
}
