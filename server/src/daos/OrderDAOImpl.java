package daos;

import common.model.Customer;
import common.model.DateTime;
import common.model.Order;
import common.model.Product;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderDAOImpl implements OrderDAO{
    private  static OrderDAOImpl instance;

    private OrderDAOImpl() throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
    }

    public static  synchronized OrderDAOImpl getInstance() throws  SQLException{
        if(instance == null){
            instance = new OrderDAOImpl();
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_URL, CONNECTION_USER, CONNECTION_PASSWORD);
    }

    @Override
    public Order create(HashMap<Product, Integer> products, DateTime date, Customer customer, String status, String comment) throws SQLException{
        try(Connection connection = getConnection()){
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO \"order\"(date, email, status, coment) VALUES(?, ?, ?, ?);", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setObject(1, date.getSortableDate(), Types.DATE);
            statement.setString(2, customer.getEmail());
            statement.setString(3, status);
            statement.setString(4, comment);
            statement.executeUpdate();
            ResultSet keys =  statement.getGeneratedKeys();
            if(keys.next()){
                Order order = new Order(String.valueOf(keys.getInt(1)),products, customer, status, comment);
                order.setDate(date);
                addToProductOrder(products, order.getId());
                return order;
            }
            else throw new SQLException("No keys granted");
        }
    }

    @Override
    public void update(Order order) throws SQLException{
        try(Connection connection = getConnection()){
            PreparedStatement statement =
                    connection.prepareStatement("UPDATE order SET date = ?, email = ?, status =?, coment = ? WHERE id = ?");
            statement.setObject(1, order.getDate(), Types.DATE);
            statement.setString(2, order.getCustomer().getEmail());
            statement.setString(3, order.getStatus());
            statement.setString(4, order.getComment());
            statement.setInt(5, Integer.parseInt(order.getId()));
            statement.executeUpdate();
            updateProductOrder(order);
        }
    }

    @Override
    public void updateProductOrder(Order order) throws SQLException{
        try(Connection connection = getConnection()) {
            for (HashMap.Entry<Product,Integer> entry : order.getProducts().entrySet()) {
                PreparedStatement statement =
                        connection.prepareStatement("UPDATE productorder SET quantity = ?) WHERE orderid = ? AND productid = ?;");
                statement.setInt(1, entry.getValue());
                statement.setInt(2, Integer.parseInt(order.getId()));
                statement.setInt(3, Integer.parseInt(entry.getKey().getId()));
                statement.executeUpdate();
            }
        }
    }

    @Override
    public void delete(Order order) throws SQLException {
        try(Connection connection = getConnection()){
            PreparedStatement statement =
                    connection.prepareStatement("DELETE FROM order WHERE id = ?");
            statement.setInt(1, Integer.parseInt(order.getId()));
            statement.executeUpdate();
        }
    }

    @Override
    public void addToProductOrder(HashMap<Product, Integer> products, String orderId) throws SQLException {
        try(Connection connection = getConnection()){
            for (HashMap.Entry<Product,Integer> entry : products.entrySet()) {
                PreparedStatement statement =
                        connection.prepareStatement("INSERT INTO productorder(orderid, productid, quantity) VALUES(?, ?, ?);");
                statement.setInt(1, Integer.parseInt(orderId));
                statement.setInt(2, Integer.parseInt(entry.getKey().getId()));
                statement.setInt(3, entry.getValue());
                statement.executeUpdate();
            }
        }
    }

    @Override
    public ArrayList<Order> getPendingOrders() throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("SELECT * FROM \"order\" WHERE status = ?");
            statement.setString(1, "pending");
            ResultSet orderSet = statement.executeQuery();
            ArrayList<Order> orderList = new ArrayList<>();
            while (orderSet.next()) {
                String orderid = String.valueOf(orderSet.getInt("id"));
                LocalDate d = orderSet.getDate("date").toLocalDate();
                DateTime date = new DateTime(d.getDayOfMonth(), d.getMonthValue(), d.getYear());
                String email = orderSet.getString("email");
                String comment = orderSet.getString("coment");
                statement =
                        connection.prepareStatement(
                                "SELECT productorder.quantity, product.id, product.name, product.description, product.quantity AS available, product.price " +
                                        "FROM productorder " +
                                        "INNER JOIN product on productorder.productid = product.id " +
                                        "WHERE productorder.orderid = ?;");
                statement.setInt(1, orderSet.getInt("id"));
                ResultSet productsSet = statement.executeQuery();
                HashMap<Product, Integer> products = new HashMap<>();
                while (productsSet.next()) {
                    String productid = String.valueOf(productsSet.getInt("id"));
                    String name = productsSet.getString("name");
                    String description = productsSet.getString("description");
                    int quantity = productsSet.getInt("quantity");
                    double price = productsSet.getDouble("price");
                    int available = productsSet.getInt("available");
                    products.put(new Product(productid, quantity, name, description, price), quantity);
                }
                orderList.add(new Order(orderid, products, date, (Customer) UserDAOImpl.getInstance().readByEmail(email), "pending", comment));
            }
            return orderList;
        }
    }
        @Override
        public ArrayList<Order> getAllOrders() throws SQLException {
            try(Connection connection = getConnection()){
                PreparedStatement statement =
                        connection.prepareStatement("SELECT * FROM \"order\"");
                ResultSet orderSet = statement.executeQuery();
                ArrayList<Order> orderList = new ArrayList<>();
                while (orderSet.next()){
                    String orderid = String.valueOf(orderSet.getInt("id"));
                    LocalDate d = orderSet.getDate("date").toLocalDate();
                    DateTime date = new DateTime(d.getDayOfMonth(), d.getMonthValue(),d.getYear());
                    String email = orderSet.getString("email");
                    String comment = orderSet.getString("coment");
                    String status = orderSet.getString("status");
                    statement =
                            connection.prepareStatement(
                                    "SELECT productorder.quantity, product.id, product.name, product.description, product.quantity AS available, product.price " +
                                            "FROM productorder " +
                                            "INNER JOIN product on productorder.productid = product.id " +
                                            "WHERE productorder.orderid = ?;");
                    statement.setInt(1,orderSet.getInt("id"));
                    ResultSet productsSet = statement.executeQuery();
                    HashMap<Product, Integer> products = new HashMap<>();
                    while(productsSet.next()){
                        String productid = String.valueOf(productsSet.getInt("id"));
                        String name = productsSet.getString("name");
                        String description = productsSet.getString("description");
                        int quantity = productsSet.getInt("quantity");
                        double price = productsSet.getDouble("price");
                        int available = productsSet.getInt("available");
                        products.put(new Product(productid, quantity, name, description, price), quantity);
                    }
                    orderList.add(new Order(orderid, products, date,(Customer) UserDAOImpl.getInstance().readByEmail(email), status, comment));
                }
                return orderList;
            }
    }

    @Override
    public void updateOrderStatus(String orderId, String status) throws SQLException {
        try(Connection connection = getConnection()){
            PreparedStatement statement =
                    connection.prepareStatement("UPDATE \"order\" SET status = ? WHERE id = ?");
            statement.setString(1, status);
            statement.setInt(2, Integer.parseInt(orderId));
            statement.executeUpdate();
        }
    }

    @Override
    public ArrayList<Order> getAllCustomerOrderByEmail(String email) throws SQLException {
        try(Connection connection = getConnection()){
            PreparedStatement statement =
                    connection.prepareStatement("SELECT * FROM \"order\" WHERE email = ?");
            statement.setString(1, email);
            ResultSet orderSet = statement.executeQuery();
            ArrayList<Order> orderList = new ArrayList<>();
            while (orderSet.next()){
                String orderid = String.valueOf(orderSet.getInt("id"));
                LocalDate d = orderSet.getDate("date").toLocalDate();
                DateTime date = new DateTime(d.getDayOfMonth(), d.getMonthValue(),d.getYear());
                String e = orderSet.getString("email");
                String comment = orderSet.getString("coment");
                String status = orderSet.getString("status");
                statement =
                        connection.prepareStatement(
                                "SELECT productorder.quantity, product.id, product.name, product.description, product.quantity AS available, product.price " +
                                        "FROM productorder " +
                                        "INNER JOIN product on productorder.productid = product.id " +
                                        "WHERE productorder.orderid = ?;");
                statement.setInt(1,orderSet.getInt("id"));
                ResultSet productsSet = statement.executeQuery();
                HashMap<Product, Integer> products = new HashMap<>();
                while(productsSet.next()){
                    String productid = String.valueOf(productsSet.getInt("id"));
                    String name = productsSet.getString("name");
                    String description = productsSet.getString("description");
                    int quantity = productsSet.getInt("quantity");
                    double price = productsSet.getDouble("price");
                    int available = productsSet.getInt("available");
                    products.put(new Product(productid, available, name, description, price), quantity);
                }
                orderList.add(new Order(orderid, products, date,(Customer) UserDAOImpl.getInstance().readByEmail(email), status, comment));
            }
            return orderList;
        }
    }

    @Override
    public ArrayList<Order> getOrderById(String id) throws SQLException {
        try(Connection connection = getConnection()){
            PreparedStatement statement =
                    connection.prepareStatement("SELECT * FROM \"order\" WHERE id = ?");
            statement.setInt(1, Integer.parseInt(id));
            ResultSet orderSet = statement.executeQuery();
            ArrayList<Order> orderList = new ArrayList<>();
            while (orderSet.next()){
                LocalDate d = orderSet.getDate("date").toLocalDate();
                DateTime date = new DateTime(d.getDayOfMonth(), d.getMonthValue(),d.getYear());
                String email = orderSet.getString("email");
                String comment = orderSet.getString("coment");
                String status = orderSet.getString("status");
                statement =
                        connection.prepareStatement(
                                "SELECT productorder.quantity, product.id, product.name, product.description, product.quantity AS available, product.price " +
                                        "FROM productorder " +
                                        "INNER JOIN product on productorder.productid = product.id " +
                                        "WHERE productorder.orderid = ?;");
                statement.setInt(1,Integer.parseInt(id));
                ResultSet productsSet = statement.executeQuery();
                HashMap<Product, Integer> products = new HashMap<>();
                while(productsSet.next()){
                    String productid = String.valueOf(productsSet.getInt("id"));
                    String name = productsSet.getString("name");
                    String description = productsSet.getString("description");
                    int quantity = productsSet.getInt("quantity");
                    double price = productsSet.getDouble("price");
                    int available = productsSet.getInt("available");
                    products.put(new Product(productid, available, name, description, price), quantity);
                }
                orderList.add(new Order(id, products, date,(Customer) UserDAOImpl.getInstance().readByEmail(email), status, comment));
            }
            return orderList;
        }
    }
}
