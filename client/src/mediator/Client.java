package mediator;

import common.model.Order;
import common.model.Product;
import common.model.User;
import common.model.UserList;
import common.network.RemoteClientInterface;
import common.network.RemoteServerInterface;
import common.utility.observer.event.ObserverEvent;
import common.utility.observer.listener.GeneralListener;
import common.utility.observer.subject.PropertyChangeHandler;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;

public class Client implements ClientTarget, RemoteClientInterface {

    private PropertyChangeHandler<String, Object> property;
    private RemoteServerInterface server;

    public Client() throws Exception {
        server = (RemoteServerInterface) Naming.lookup("rmi://127.0.0.1:1099/access");
        property = new PropertyChangeHandler<>(this);
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public boolean addListener(GeneralListener<String, Object> listener, String... propertyNames) {
        return property.addListener(listener, propertyNames);
    }

    @Override
    public boolean removeListener(GeneralListener<String, Object> listener, String... propertyNames) {
        return property.removeListener(listener, propertyNames);
    }

    @Override
    public void stop() {
        try {
            server.deauthenticate(this);
            UnicastRemoteObject.unexportObject(this, true);
        } catch (Exception e) {
            // Do nothing.
        }
    }

    @Override
    public void authenticate(String email, String password) throws Exception {
        server = server.authenticate(this, email, password);
    }

    @Override
    public boolean deauthenticate() {
        try {
            server.deauthenticate(this);
        } catch (RemoteException e) {
            // Do nothing.
        }
        try {
            server = (RemoteServerInterface) Naming.lookup("rmi://127.0.0.1:1099/access");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void register(String email, String password, String firstName, String lastName, LocalDate birthday, char gender) throws Exception {
        server.register(email, password, firstName, lastName, birthday, gender);
    }

    @Override
    public User getAuthenticatedUser() throws Exception {
        return server.getAuthenticatedUser();
    }

    @Override
    public UserList getAllRegisteredUsers() throws Exception {
        return server.getAllRegisteredUsers();
    }

    @Override
    public void updateUser(String email, User user) throws Exception {
        server.updateUser(email, user);
    }

    @Override
    public void removeUser(String email) throws Exception {
        server.removeUser(email);
    }

    @Override
    public ArrayList<Product> getCatalogOfProducts() throws Exception {
        return server.getCatalogOfProducts();
    }

    @Override
    public Product getProductById(String productId) throws Exception {
        return server.getProductById(productId);
    }

    @Override
    public void addProduct(int quantity, String name, String description, double price) throws Exception {
        server.addProduct(quantity, name, description, price);
    }

    @Override
    public void updateProduct(Product product) throws Exception {
        server.updateProduct(product);
    }

    @Override
    public void removeProduct(Product product) throws Exception {
        server.removeProduct(product);
    }

    @Override
    public void placeOrder(Order order) throws Exception {
        server.placeOrder(order);
    }

    @Override
    public void updateOrderStatus(String orderId, String status) throws Exception {
        server.updateOrderStatus(orderId, status);
    }

    @Override
    public ArrayList<Order> getAllOrders() throws Exception {
        return server.getAllOrders();
    }

    @Override
    public void sendEventNotification(String eventText) throws Exception {
        server.sendEventNotification(eventText);
    }

    @Override
    public void propertyChange(ObserverEvent<String, Object> event) throws RemoteException {
        property.firePropertyChange(event.getPropertyName(), event.getValue1(), event.getValue2());
    }
}
