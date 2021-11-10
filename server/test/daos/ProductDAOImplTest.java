package daos;

import common.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ProductDAOImplTest {
    ProductDAO productDAO;

    @BeforeEach
    void setUp() {
        try {
            productDAO = ProductDAOImpl.getInstance();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        try {
            assertEquals("id='1', quantity=3, name='Baklava', description='Baklava is very tasty', price=2.5", productDAO.create(3, "Baklava", "Baklava is very tasty", 2.5).toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test void getById() throws SQLException {
        assertEquals("id='1', quantity=3, name='Baklava', description='Baklava is very tasty', price=2.5", productDAO.getById("1").toString());
    }

    @Test
    void update() {
        try {
            productDAO.update(new Product("1",30, "Baklava", "Baklava is very tasty", 3));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void delete() {
    }

    @Test
    void read() {
        try {
            assertEquals("[id='1', quantity=3, name='Baklava', description='Baklava is very tasty', price=2.5, id='2', quantity=4, name='Pain au Chocolate', description='nice', price=5.0, id='3', quantity=1, name='Golden Apple', description='extra nice', price=7.41, id='4', quantity=3, name='Sugar Bombs', description='niche', price=3.22, id='5', quantity=7, name='2 kg of Sweets', description='niche extra', price=1.0]", productDAO.read().toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void readByName() {
        try {
            assertEquals("[id='1', quantity=3, name='Baklava', description='Baklava is very tasty', price=2.5]", productDAO.readByName("Baklava").toString());
            assertEquals("[]", productDAO.readByName("Baklavaa").toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}