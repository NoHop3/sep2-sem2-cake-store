package mediator;

import common.model.Order;
import common.model.Product;
import common.model.User;
import common.model.UserList;
import model.Model;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CustomerAuthenticated extends GenericAccessType {

    public CustomerAuthenticated(Model model, String email, String password) throws Exception {
        super(model, email, password);
    }

    @Override
    protected String[] getChangesToListenFor() {
        return new String[] {
                "newProduct",
                "replacedProduct",
                "deletedProduct",
                "newEvent",
                "newOrder",
                "completedOrder"
        };
    }

    @Override
    public UserList getAllRegisteredUsers() throws RemoteException {
        throw new IllegalStateException("Only an employee is allowed to perform this request.");
    }

    @Override
    public void updateUser(String email, User user) throws RemoteException {
        if (email == null || !email.equals(getEmail())) throw new IllegalArgumentException("A customer can only change his account settings.");
        getModel().updateUser(email, user);
    }

    @Override
    public void removeUser(String email) throws RemoteException {
        throw new IllegalStateException("Only an employee is allowed to perform this request.");
    }

    @Override
    public void addProduct(int quantity, String name, String description, double price) throws RemoteException {
        throw new IllegalStateException("Only an employee is allowed to perform this request.");
    }

    @Override
    public void updateProduct(Product product) throws RemoteException {
        throw new IllegalStateException("Only an employee is allowed to perform this request.");
    }

    @Override
    public void removeProduct(Product product) throws RemoteException {
        throw new IllegalStateException("Only an employee is allowed to perform this request.");
    }

    @Override
    public void updateOrderStatus(String orderId, String status) throws RemoteException {
        throw new IllegalStateException("Only an employee is allowed to perform this request.");
    }

    @Override
    public ArrayList<Order> getAllOrders() throws RemoteException {
        return getModel().getAllOrders().stream().filter(order -> getEmail().equals(order.getCustomer().getEmail())).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void sendEventNotification(String eventText) throws RemoteException {
        throw new IllegalStateException("Only an employee is allowed to perform this request.");
    }
}
