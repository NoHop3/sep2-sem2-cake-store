package model;

import common.model.Order;
import common.model.Product;
import common.model.User;
import common.model.UserList;
import common.utility.observer.subject.LocalSubject;
import java.time.LocalDate;
import java.util.ArrayList;

public interface Model extends LocalSubject<String, Object> {
    UserList getAllRegisteredUsers() throws IllegalStateException;
    void register(String email, String password, String firstName, String lastName, LocalDate birthday, char gender) throws IllegalArgumentException, IllegalStateException;
    User getUser(String email) throws IllegalArgumentException, IllegalStateException;
    void updateUser(String email, User user) throws IllegalStateException, IllegalArgumentException;
    void removeUser(String email) throws IllegalArgumentException, IllegalStateException;
    ArrayList<Product> getCatalogOfProducts() throws IllegalStateException;
    Product getProductById(String productId) throws IllegalArgumentException, IllegalStateException;
    void addProduct(String emailOfWhoAdded, int quantity, String name, String description, double price) throws IllegalStateException;
    void updateProduct(Product product) throws IllegalStateException;
    void removeProduct(Product product) throws IllegalStateException;
    void placeOrder(Order order) throws IllegalStateException;
    void updateOrderStatus(String orderId, String status) throws IllegalArgumentException;
    ArrayList<Order> getAllOrders() throws IllegalStateException;
    void sendEventNotification(String eventText);
}
