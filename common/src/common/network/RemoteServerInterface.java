package common.network;

import common.model.Order;
import common.model.Product;
import common.model.User;
import common.model.UserList;
import common.utility.observer.subject.RemoteSubject;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface RemoteServerInterface extends RemoteSubject<String, Object> {
    RemoteServerInterface authenticate(RemoteClientInterface client, String email, String password) throws RemoteException;
    void deauthenticate(RemoteClientInterface client) throws RemoteException;
    void register(String email, String password, String firstName, String lastName, LocalDate birthday, char gender) throws RemoteException;
    User getAuthenticatedUser() throws RemoteException;
    UserList getAllRegisteredUsers() throws RemoteException;
    void updateUser(String email, User user) throws RemoteException;
    void removeUser(String email) throws RemoteException;
    ArrayList<Product> getCatalogOfProducts() throws RemoteException;
    Product getProductById(String productId) throws RemoteException;
    void addProduct(int quantity, String name, String description, double price) throws RemoteException;
    void updateProduct(Product product) throws RemoteException;
    void removeProduct(Product product) throws RemoteException;
    void placeOrder(Order order) throws RemoteException;
    void updateOrderStatus(String orderId, String status) throws RemoteException;
    ArrayList<Order> getAllOrders() throws RemoteException;
    void sendEventNotification(String eventText) throws RemoteException;
}
