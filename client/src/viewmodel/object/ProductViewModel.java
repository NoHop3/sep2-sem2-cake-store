package viewmodel.object;

import common.model.Product;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProductViewModel {

    private StringProperty id;
    private ObjectProperty<Integer> quantity;
    private StringProperty name;
    private StringProperty description;
    private ObjectProperty<Double> price;

    public ProductViewModel(Product product) {
        id = new SimpleStringProperty(product.getId());
        quantity = new SimpleObjectProperty<>(product.getQuantity());
        name = new SimpleStringProperty(product.getName());
        description = new SimpleStringProperty(product.getDescription());
        price = new SimpleObjectProperty<>(product.getPrice());
    }

    public StringProperty getIdProperty() {
        return id;
    }

    public ObjectProperty<Integer> getQuantityProperty() {
        return quantity;
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public ObjectProperty<Double> getPriceProperty() {
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProductViewModel)) return false;
        ProductViewModel tmp = (ProductViewModel) obj;
        return id.getValue().equals(tmp.id.getValue());
    }
}
