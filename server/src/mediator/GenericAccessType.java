package mediator;

import common.model.Order;
import common.model.Product;
import common.model.User;
import common.network.RemoteClientInterface;
import common.network.RemoteServerInterface;
import common.utility.observer.event.ObserverEvent;
import common.utility.observer.listener.GeneralListener;
import common.utility.observer.listener.LocalListener;
import common.utility.observer.subject.PropertyChangeHandler;
import model.Model;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;

public abstract class GenericAccessType implements RemoteServerInterface, LocalListener<String, Object> {

    private PropertyChangeHandler<String, Object> property;
    private Model model;
    private String email;
    private String password;

    public GenericAccessType(Model model, String email, String password) throws RemoteException {
        this.model = model;
        this.email = email;
        this.password = password;
        property = new PropertyChangeHandler<>(this);
        model.addListener(this, getChangesToListenFor());
        UnicastRemoteObject.exportObject(this, 0);
    }

    protected abstract String[] getChangesToListenFor();

    public Model getModel() {
        return model;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GenericAccessType)) return false;
        GenericAccessType tmp = (GenericAccessType) obj;
        return email.equals(tmp.email) && password.equals(tmp.password);
    }

    // Overriding the hashCode method is necessary in order for the reverse entry lookup of the bidirectional map to work properly.
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + email.hashCode();
        hash = 31 * hash + password.hashCode();
        return hash;
    }

    @Override
    public boolean addListener(GeneralListener<String, Object> listener, String... propertyNames) throws RemoteException {
        return property.addListener(listener, propertyNames);
    }

    @Override
    public boolean removeListener(GeneralListener<String, Object> listener, String... propertyNames) throws RemoteException {
        return property.removeListener(listener, propertyNames);
    }

    @Override
    public RemoteServerInterface authenticate(RemoteClientInterface client, String email, String password) throws RemoteException {
        throw new IllegalStateException("Can't perform request, already authenticated.");
    }

    @Override
    public void deauthenticate(RemoteClientInterface client) throws RemoteException {
        try {
            getModel().removeListener(this, getChangesToListenFor());
            removeListener(client);
            UnicastRemoteObject.unexportObject(this, true);
            ((RemoteServerInterface) Naming.lookup("rmi://127.0.0.1:1099/access")).deauthenticate(client);
        } catch (Exception e) {
            throw new IllegalStateException("Could not deauthenticate.");
        }
    }

    @Override
    public void register(String email, String password, String firstName, String lastName, LocalDate birthday, char gender) throws RemoteException {
        throw new IllegalStateException("Can't perform register request, already authenticated.");
    }

    @Override
    public User getAuthenticatedUser() throws RemoteException {
        return model.getUser(email);
    }

    @Override
    public ArrayList<Product> getCatalogOfProducts() throws RemoteException {
        return model.getCatalogOfProducts();
    }

    @Override
    public Product getProductById(String productId) throws RemoteException {
        return model.getProductById(productId);
    }

    @Override
    public void placeOrder(Order order) {
        model.placeOrder(order);
    }

    @Override
    public void propertyChange(ObserverEvent<String, Object> event) {
        switch (event.getPropertyName()) {
            case "newProduct" : {
                String whoAdded = event.getValue1();
                property.firePropertyChange(event.getPropertyName(), email.equals(whoAdded) ? whoAdded : "", event.getValue2());
                break;
            }
            case "completedOrder" :
            case "newOrder" : {
                if (this instanceof CustomerAuthenticated && !email.equals(event.getValue1())) return;
                property.firePropertyChange(event.getPropertyName(), event.getValue1(), event.getValue2());
                break;
            }
            default : {
                property.firePropertyChange(event.getPropertyName(), event.getValue1(), event.getValue2());
            }
        }
    }
}
