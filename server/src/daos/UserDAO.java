package daos;

import common.model.*;
import java.sql.SQLException;

public interface UserDAO extends DAO {
    void create(User user) throws SQLException;
    void update(User user) throws SQLException;
    void updateAge(User user) throws SQLException;
    void updatePassword(User user) throws SQLException;
    void delete(String email) throws SQLException;
    UserList allEmployees() throws SQLException;
    UserList allCustomers() throws SQLException;
    UserList allUsers() throws SQLException;
    User readByEmail(String email) throws SQLException;
}
