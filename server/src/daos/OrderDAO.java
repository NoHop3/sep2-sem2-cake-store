package daos;

import common.model.Customer;
import common.model.DateTime;
import common.model.Order;
import common.model.Product;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface OrderDAO extends DAO {
    Order create(HashMap<Product, Integer> products, DateTime date, Customer customer, String status, String comment) throws SQLException;
    void update(Order order) throws SQLException;
    void updateProductOrder(Order order) throws SQLException;
    void delete(Order order) throws SQLException;
    void addToProductOrder(HashMap<Product, Integer> products, String orderId) throws SQLException;
    ArrayList<Order> getPendingOrders() throws SQLException;
    ArrayList<Order> getAllOrders() throws SQLException;
    void updateOrderStatus(String orderId, String status) throws SQLException;
    ArrayList<Order> getAllCustomerOrderByEmail(String email) throws SQLException;
    ArrayList<Order> getOrderById(String id) throws SQLException;
}
