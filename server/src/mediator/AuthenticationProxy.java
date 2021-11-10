package mediator;

import common.model.Order;
import common.model.Product;
import common.model.User;
import common.model.UserList;
import common.network.RemoteClientInterface;
import common.network.RemoteServerInterface;
import common.utility.collection.BidirectionalHashMap;
import common.utility.observer.event.ObserverEvent;
import common.utility.observer.listener.GeneralListener;
import common.utility.observer.listener.LocalListener;
import model.Model;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;

public class AuthenticationProxy implements RemoteServerInterface, LocalListener<String, Object> {

    private Model model;
    private BidirectionalHashMap<RemoteClientInterface, GenericAccessType> authenticatedInstances;
    private UserList cache;

    public AuthenticationProxy(Model model) throws InstantiationException {
        this.model = model;
        authenticatedInstances = new BidirectionalHashMap<>();
        try {
            UnicastRemoteObject.exportObject(this, 0);
            Naming.rebind("access", this);
        } catch (Exception e) {
            throw new InstantiationException("Authentication proxy could not be started.");
        }
        cache = model.getAllRegisteredUsers();
        model.addListener(this, "newUser", "deletedUser");
    }

    @Override
    public boolean addListener(GeneralListener<String, Object> listener, String... propertyNames) throws RemoteException {
        throw new IllegalStateException("Authenticate in order to perform this request.");
    }

    @Override
    public boolean removeListener(GeneralListener<String, Object> listener, String... propertyNames) throws RemoteException {
        throw new IllegalStateException("Authenticate in order to perform this request.");
    }

    @Override
    public RemoteServerInterface authenticate(RemoteClientInterface client, String email, String password) throws RemoteException {
        // Validate the provided arguments.
        if (email == null || email.isEmpty()) throw new IllegalArgumentException("Email value can't be null or empty.");
        if (password == null || password.isEmpty()) throw new IllegalArgumentException("Password value can't be null or empty.");
        // Check if such a user is registered in the system.
        User tmp = cache.getUser(email);
        if (tmp == null || !tmp.getPassword().equals(password)) throw new IllegalArgumentException("Invalid email or password.");
        GenericAccessType toReturn;
        try {
            toReturn = tmp.isEmployee() ? new EmployeeAuthenticated(model, email, password) : new CustomerAuthenticated(model, email, password);
        } catch (Exception e) {
            throw new IllegalStateException("Could not authenticate at this moment, try later.");
        }
        // Checks if the given user is already logged in.
        if (authenticatedInstances.getKey(toReturn) != null) throw new IllegalStateException("This user is already authenticated.");
        // Return the real subject to be replaced instead of the proxy on the client side.
        authenticatedInstances.put(client, toReturn);

        // Upon authentication automatically add the client to be a listener for any changes on the server.
        toReturn.addListener(client);
        return toReturn;
    }

    @Override
    public void deauthenticate(RemoteClientInterface client) {
        if (authenticatedInstances.getValue(client) != null) authenticatedInstances.remove(client);
    }

    @Override
    public void register(String email, String password, String firstName, String lastName, LocalDate birthday, char gender) throws RemoteException {
        model.register(email, password, firstName, lastName, birthday, gender);
    }

    @Override
    public User getAuthenticatedUser() throws RemoteException {
        throw new IllegalStateException("Authenticate in order to perform this request.");
    }

    @Override
    public UserList getAllRegisteredUsers() throws RemoteException {
        throw new IllegalStateException("Authenticate in order to perform this request.");
    }

    @Override
    public void updateUser(String email, User user) throws RemoteException {
        throw new IllegalStateException("Authenticate in order to perform this request.");
    }

    @Override
    public void removeUser(String email) throws RemoteException {
        throw new IllegalStateException("Authenticate in order to perform this request.");
    }

    @Override
    public ArrayList<Product> getCatalogOfProducts() throws RemoteException {
        throw new IllegalStateException("Authenticate in order to perform this request.");
    }

    @Override
    public Product getProductById(String productId) throws RemoteException {
        throw new IllegalStateException("Authenticate in order to perform this request.");
    }

    @Override
    public void addProduct(int quantity, String name, String description, double price) throws RemoteException {
        throw new IllegalStateException("Authenticate in order to perform this request.");
    }

    @Override
    public void updateProduct(Product product) throws RemoteException {
        throw new IllegalStateException("Authenticate in order to perform this request.");
    }

    @Override
    public void removeProduct(Product product) throws RemoteException {
        throw new IllegalStateException("Authenticate in order to perform this request.");
    }

    @Override
    public void placeOrder(Order order) throws RemoteException {
        throw new IllegalStateException("Authenticate in order to perform this request.");
    }

    @Override
    public void updateOrderStatus(String orderId, String status) throws RemoteException {
        throw new IllegalStateException("Authenticate in order to perform this request.");
    }

    @Override
    public ArrayList<Order> getAllOrders() throws RemoteException {
        throw new IllegalStateException("Authenticate in order to perform this request.");
    }

    @Override
    public void sendEventNotification(String eventText) throws RemoteException {
        throw new IllegalStateException("Authenticate in order to perform this request.");
    }

    @Override
    public void propertyChange(ObserverEvent<String, Object> event) {
        try {
            switch (event.getPropertyName()) {
                case "newUser" : {
                    cache.addUser((User) event.getValue2());
                    break;
                }
                case "deletedUser" : {
                    cache.removeUser(event.getValue1());
                    break;
                }
            }
        } catch (Exception e) {
            // Do nothing.
        }
    }
}
