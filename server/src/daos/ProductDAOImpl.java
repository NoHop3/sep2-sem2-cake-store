package daos;

import common.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    private static ProductDAOImpl instance;

    private ProductDAOImpl() throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
    }

    public static synchronized ProductDAOImpl getInstance() throws SQLException {
        if (instance == null) {
            instance = new ProductDAOImpl();
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_URL, CONNECTION_USER, CONNECTION_PASSWORD);
    }

    @Override
    public Product create(int quantity, String name, String description, double price) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO product(quantity, name, description, price) VALUES(?, ?, ?, ?);", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, quantity);
            statement.setString(2, name);
            statement.setString(3, description);
            statement.setDouble(4, price);
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                return new Product(String.valueOf(keys.getInt(1)), quantity, name, description, price);
            } else throw new SQLException("No keys granted");
        }
    }

    @Override
    public void update(Product product) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE product SET quantity = ?, name = ?, description = ?, price = ? WHERE id = ?");
            statement.setInt(1, product.getQuantity());
            statement.setString(2, product.getName());
            statement.setString(3, product.getDescription());
            statement.setDouble(4, product.getPrice());
            statement.setInt(5, Integer.parseInt(product.getId()));
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Product product) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM product WHERE id = ?");
            statement.setInt(1, Integer.parseInt(product.getId()));
            statement.executeUpdate();
        }
    }

    @Override
    public List<Product> read() throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM product");
            ResultSet productsSet = statement.executeQuery();
            ArrayList<Product> productsList = new ArrayList<>();
            while (productsSet.next()) {
                String id = String.valueOf(productsSet.getInt("id"));
                String name = productsSet.getString("name");
                String description = productsSet.getString("description");
                int quantity = productsSet.getInt("quantity");
                double price = productsSet.getDouble("price");
                Product product = new Product(id, quantity, name, description, price);
                productsList.add(product);
            }
            return productsList;
        }
    }

    public List<Product> readByName(String name) throws SQLException {
        try(Connection connection = getConnection()){
            PreparedStatement statement =
                    connection.prepareStatement("SELECT * FROM product WHERE name = ?");
            statement.setString(1, name);
            ResultSet productsSet = statement.executeQuery();
            ArrayList<Product> productsList = new ArrayList<>();
            while (productsSet.next()){
                String id = String.valueOf(productsSet.getInt("id"));
                String description = productsSet.getString("description");
                int quantity = productsSet.getInt("quantity");
                double price = productsSet.getDouble("price");
                Product product = new Product(id, quantity, name,description, price);
                productsList.add(product);
            }
            return productsList;
        }
    }

    @Override
    public Product getById(String id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM \"product\" WHERE id = ?")) {
            statement.setInt(1, Integer.parseInt(id));
            ResultSet productSet = statement.executeQuery();
            return productSet.next() ? new Product(
                    String.valueOf(productSet.getInt("id")),
                    productSet.getInt("quantity"),
                    productSet.getString("name"),
                    productSet.getString("description"),
                    productSet.getDouble("price")
            ) : null;
        }
    }
}
