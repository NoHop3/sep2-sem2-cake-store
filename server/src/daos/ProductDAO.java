package daos;

import common.model.Product;
import java.sql.SQLException;
import java.util.List;

public interface ProductDAO extends DAO {
    Product create(int quantity, String name, String description, double price) throws SQLException;
    void update(Product product) throws SQLException;
    void delete(Product product) throws SQLException;
    List<Product> read() throws SQLException;
    List<Product> readByName(String name) throws SQLException;
    Product getById(String id) throws SQLException;
    //void createDummyData(int quantity, String name, String description, double price) throws SQLException;
}
