package viewmodel.object;

import common.model.DateTime;
import common.model.Order;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.stream.Collectors;

public class OrderViewModel {

    private StringProperty id;
    private ObjectProperty<DateTime> date;
    private StringProperty email;
    private StringProperty comment;
    private StringProperty status;
    private ObservableList<ProductViewModel> productList;

    public OrderViewModel(Order order) {
        id = new SimpleStringProperty(order.getId());
        date = new SimpleObjectProperty<>(order.getDate());
        email = new SimpleStringProperty(order.getCustomer().getEmail());
        comment = new SimpleStringProperty(order.getComment());
        status = new SimpleStringProperty(order.getStatus());
        productList = FXCollections.observableArrayList(order.getProducts().keySet().stream().map(ProductViewModel::new).collect(Collectors.toList()));
    }

    public StringProperty getId() {
        return id;
    }

    public ObjectProperty<DateTime> getDate() {
        return date;
    }

    public StringProperty getEmail() {
        return email;
    }

    public StringProperty getComment() {
        return comment;
    }

    public StringProperty getStatus() {
        return status;
    }

    public ObservableList<ProductViewModel> getProductList() {
        return productList;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OrderViewModel)) return false;
        OrderViewModel tmp = (OrderViewModel) obj;
        return id.getValue().equals(tmp.id.getValue());
    }
}
