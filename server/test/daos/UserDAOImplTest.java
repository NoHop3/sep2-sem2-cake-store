package daos;

import common.model.DateTime;
import common.model.Employee;
import common.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOImplTest {
    UserDAO userDAO;

    @BeforeEach
    void setUp() {
        try {
            userDAO = UserDAOImpl.getInstance();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
//        try {
//            //assertEquals("Bob Bob - 02/01/2001 - M", userDAO.create("bob@gmail.com", "123456", "Bob", "Bob", new DateTime(2,1,2001),'M', false).toString());
//            //assertEquals("Rob Rob - 03/05/2001 - M", userDAO.create("rob@gmail.com", "123456", "Rob", "Rob", new DateTime(3,5,2001),'M', true).toString());
//            //assertEquals("Katy Katy - 06/01/2001 - F", userDAO.create("katy@gmail.com", "123456", "Katy", "Katy", new DateTime(6,1,2001),'F', true).toString());
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
    }

    @Test
    void update() throws SQLException {
        userDAO.update(new Employee("bob@gmail.com", "123456", "Bob", "Bob", new DateTime(2,1,2001),'M'));
    }

    @Test
    void updateAge() {
    }

    @Test
    void readByEmail() throws SQLException {
        User user = userDAO.readByEmail("bob@gmail.com");
        System.out.println(user instanceof Employee);
    }

    @Test
    void updatePassword() {
    }

    @Test
    void delete() {
    }

    @Test
    void allEmployees() {
        try {
            assertEquals("[Rob Rob - 03/05/2001 - M, Katy Katy - 06/01/2001 - F]", userDAO.allEmployees().toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}