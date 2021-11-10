package mediator;

import common.model.Order;
import common.model.Product;
import common.model.User;
import common.model.UserList;
import common.utility.observer.subject.LocalSubject;
import java.time.LocalDate;
import java.util.ArrayList;

public interface ClientTarget extends LocalSubject<String, Object> {
    void stop();
    void authenticate(String email, String password) throws Exception;
    boolean deauthenticate();
    void register(String email, String password, String firstName, String lastName, LocalDate birthday, char gender) throws Exception;
    User getAuthenticatedUser() throws Exception;
    UserList getAllRegisteredUsers() throws Exception;
    void updateUser(String email, User user) throws Exception;
    void removeUser(String email) throws Exception;
    ArrayList<Product> getCatalogOfProducts() throws Exception;
    Product getProductById(String productId) throws Exception;
    void addProduct(int quantity, String name, String description, double price) throws Exception;
    void updateProduct(Product product) throws Exception;
    void removeProduct(Product product) throws Exception;
    void placeOrder(Order order) throws Exception;
    void updateOrderStatus(String orderId, String status) throws Exception;
    ArrayList<Order> getAllOrders() throws Exception;
    void sendEventNotification(String eventText) throws Exception;
}
