package model;

import common.model.*;
import common.utility.observer.listener.GeneralListener;
import common.utility.observer.subject.PropertyChangeHandler;
import daos.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ModelManager implements Model {

    private PropertyChangeHandler<String, Object> property;
    private ProductDAO productDAO;
    private UserDAO userDAO;
    private OrderDAO orderDAO;

    public ModelManager() throws SQLException {
        productDAO = ProductDAOImpl.getInstance();
        userDAO = UserDAOImpl.getInstance();
        orderDAO = OrderDAOImpl.getInstance();
        property = new PropertyChangeHandler<>(this);
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
    public UserList getAllRegisteredUsers() throws IllegalStateException {
        try {
            return userDAO.allUsers();
        } catch (SQLException e) {
            throw new IllegalStateException("Server is unavailable at the moment. Try Later.");
        }
    }

    @Override
    public void register(String email, String password, String firstName, String lastName, LocalDate birthday, char gender) throws IllegalArgumentException, IllegalStateException {
        try {
            // Validate first the arguments through creating an object of type customer.
            User toCreate = new Customer(email, password, firstName, lastName, birthday, gender);
            // Checks if an user with this email is already registered.
            if (userDAO.readByEmail(email) != null) throw new IllegalStateException("An user with this email is already registered.");
            // Store the newly registered user in the database.
            userDAO.create(toCreate);
            property.firePropertyChange("newUser", toCreate.getEmail(), toCreate);
        } catch (SQLException e) {
            throw new IllegalStateException("Server is unavailable at the moment. Try Later.");
        }
    }

    @Override
    public User getUser(String email) throws IllegalArgumentException, IllegalStateException {
        if (email == null || email.isEmpty()) throw new IllegalArgumentException("Email can not be empty.");
        try {
            User toReturn = userDAO.readByEmail(email);
            if (toReturn == null) throw new IllegalStateException("No registered user with such email could be found.");
            return toReturn;
        } catch (SQLException e) {
            throw new IllegalStateException("Server is unavailable at the moment. Try Later.");
        }
    }

    @Override
    public void updateUser(String email, User user) throws IllegalStateException, IllegalArgumentException {
        if (email == null) throw new IllegalArgumentException("Old email of a user can't not be null.");
        try {
            if (!email.isEmpty()) {
                // Checks if an user with this old email exists.
                if (userDAO.readByEmail(email) == null) throw new IllegalStateException("No registered user with this old email could be found.");
                // Check if the new email is not already taken when modifying an existing user.
                if (!email.equals(user.getEmail()) && userDAO.readByEmail(user.getEmail()) != null) throw new IllegalStateException("The given new email is already taken.");
                userDAO.delete(email);
                property.firePropertyChange("deletedUser", email, null);
            } else {
                // Check if the new email is not already taken when creating a new user.
                if (userDAO.readByEmail(user.getEmail()) != null) throw new IllegalStateException("The provided email is already taken.");
            }
            userDAO.create(user);
            property.firePropertyChange("newUser", user.getEmail(), user);
        } catch (SQLException e) {
            throw new IllegalStateException("Server is unavailable at the moment. Try Later.");
        }
    }

    @Override
    public void removeUser(String email) throws IllegalArgumentException, IllegalStateException {
        if (email == null || email.isEmpty()) throw new IllegalArgumentException("Email can not be empty.");
        try {
            if (userDAO.readByEmail(email) == null) throw new IllegalStateException("No registered user with such email could be found.");
            userDAO.delete(email);
            property.firePropertyChange("deletedUser", email, null);
        } catch (SQLException e) {
            throw new IllegalStateException("Server is unavailable at the moment. Try Later.");
        }
    }

    @Override
    public ArrayList<Product> getCatalogOfProducts() throws IllegalStateException {
        try {
            return new ArrayList<>(ProductDAOImpl.getInstance().read());
        } catch (SQLException e) {
            throw new IllegalStateException("Server is unavailable at the moment. Try Later.");
        }
    }

    @Override
    public Product getProductById(String productId) throws IllegalArgumentException, IllegalStateException {
        if (productId == null || productId.isEmpty()) throw new IllegalArgumentException("Product id can not be empty.");
        try {
            Product toReturn = productDAO.getById(productId);
            if (toReturn == null) throw new IllegalStateException("No such product could be found.");
            return toReturn;
        } catch (SQLException e) {
            throw new IllegalStateException("Server is unavailable at the moment. Try Later.");
        }
    }

    @Override
    public void addProduct(String emailOfWhoAdded, int quantity, String name, String description, double price) throws IllegalStateException {
        if (quantity < 1) throw new IllegalArgumentException("Product quantity can't be less then 1.");
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Product name can't be empty.");
        if (name.length() > 100) throw new IllegalArgumentException("Product name can't be longer then 100 chars.");
        if (description == null) description = "";
        if (description.length() > 10000) throw new IllegalArgumentException("Product description can't be longer then 10 000 chars.");
        if (price < 0) throw new IllegalArgumentException("Product price can't be negative.");
        try {
            if (!productDAO.readByName(name).isEmpty()) throw new IllegalStateException("A product with this name already exists.");
            property.firePropertyChange("newProduct", emailOfWhoAdded, productDAO.create(quantity, name, description, price));
        } catch (SQLException e) {
            throw new IllegalStateException("Server is unavailable at the moment. Try Later.");
        }
    }

    @Override
    public void updateProduct(Product product) throws IllegalStateException {
        try {
            if (productDAO.getById(product.getId()) == null) throw new IllegalStateException("No such product could be found.");
            // Check if the new name of the product conflicts with another product's name.
            Product tmp = productDAO.readByName(product.getName()).isEmpty() ? null : productDAO.readByName(product.getName()).get(0);
            if (tmp != null && !tmp.getId().equals(product.getId())) throw new IllegalStateException("This product name is already taken.");
            productDAO.update(product);
            property.firePropertyChange("replacedProduct", product.getId(), product);
        } catch (SQLException e) {
            throw new IllegalStateException("Server is unavailable at the moment. Try Later.");
        }
    }

    @Override
    public void removeProduct(Product product) throws IllegalStateException {
        try {
            if (productDAO.getById(product.getId()) == null) throw new IllegalStateException("No such product could be found.");
            productDAO.delete(product);
            property.firePropertyChange("deletedProduct", product.getId(), product);
        } catch (SQLException e) {
            throw new IllegalStateException("Server is unavailable at the moment. Try Later.");
        }
    }

    @Override
    public void placeOrder(Order order) throws IllegalStateException {
        ArrayList<Product> tmpCache = getCatalogOfProducts();
        System.out.println("Debug point 1");
        for (Product toCompare : order.getProducts().keySet()) {
            Product match = tmpCache.stream().filter(product -> product.equals(toCompare)).findFirst().orElse(null);
            if (match == null) throw new IllegalStateException("Could not place the order at the moment. Try later.");
            if (toCompare.getQuantity() > match.getQuantity()) throw new IllegalStateException(toCompare.getName() + " has only " + match.getQuantity() + " units in stock left.");
        }
        try {
            for (Product toCompare : order.getProducts().keySet()) {
                Product old = tmpCache.stream().filter(product -> product.equals(toCompare)).findFirst().orElse(null);
                if (old == null) continue;
                Product toUpdate = new Product(
                        old.getId(),
                        old.getQuantity() - toCompare.getQuantity(),
                        old.getName(),
                        old.getDescription(),
                        old.getPrice()
                );
                System.out.println("Debug point 2");
                productDAO.update(toUpdate);
                System.out.println("Debug point 3");
                property.firePropertyChange("replacedProduct", toUpdate.getId(), toUpdate);
            }
            property.firePropertyChange("newOrder", order.getCustomer().getEmail(), orderDAO.create(order.getProducts(), order.getDate(), order.getCustomer(), order.getStatus(), order.getComment()));
        } catch (SQLException e) {
            throw new IllegalStateException("Server is unavailable at the moment. Try Later.");
        }
    }

    @Override
    public void updateOrderStatus(String orderId, String status) throws IllegalArgumentException {
        if (orderId == null || orderId.isEmpty()) throw new IllegalArgumentException("Order id can not be empty.");
        if (status == null || status.isEmpty()) throw new IllegalArgumentException("Status of order can not be empty.");
        if (!(status.equals("completed") || status.equals("pending"))) throw new IllegalArgumentException("Status can only be set to completed or pending.");
        try {
            orderDAO.updateOrderStatus(orderId, status);
            Order toSend = orderDAO.getOrderById(orderId).get(0);
            property.firePropertyChange("completedOrder", toSend.getCustomer().getEmail(), toSend);
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public ArrayList<Order> getAllOrders() throws IllegalStateException {
        try {
            return orderDAO.getAllOrders();
        } catch (SQLException e) {
            throw new IllegalStateException("Server is unavailable at the moment. Try Later.");
        }
    }

    @Override
    public void sendEventNotification(String eventText) {
        property.firePropertyChange("newEvent", eventText, null);
    }
}
