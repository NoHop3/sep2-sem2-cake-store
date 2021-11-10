package daos;

import common.model.*;
import java.sql.*;

public class UserDAOImpl implements UserDAO {

    private static UserDAOImpl instance;

    private UserDAOImpl() throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
    }

    public static synchronized UserDAOImpl getInstance() throws SQLException {
        if (instance == null) {
            instance = new UserDAOImpl();
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_URL, CONNECTION_USER, CONNECTION_PASSWORD);
    }

    @Override
    public void create(User user) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO \"user\"(email, password, f_name, l_name, birthday, age, gender, isemployee) VALUES(?, ?, ?, ?, ?, ?, ?, ?);");
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setObject(5, user.getBirthday().getSortableDate(), Types.DATE);
            statement.setInt(6, DateTime.yearsBetween(user.getBirthday()));
            statement.setObject(7, user.getGender(), Types.CHAR);
            statement.setBoolean(8, user.isEmployee());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(User user) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE \"user\" SET  password = ?, f_name = ?, l_name = ?, birthday = ?, age = ?, gender = ?, isemployee = ? WHERE email = ?");
            statement.setString(1, user.getPassword());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setObject(4, user.getBirthday().getSortableDate(), Types.DATE);
            statement.setInt(5, user.getAge());
            statement.setObject(6, user.getGender(), Types.CHAR);
            statement.setBoolean(7, user instanceof Employee);
            statement.setString(8, user.getEmail());
            statement.executeUpdate();
        }
    }

    @Override
    public void updateAge(User user) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE \"user\" SET  age = ? WHERE email = ?");
            statement.setInt(1, DateTime.yearsBetween(user.getBirthday()));
            statement.setString(2, user.getEmail());
            statement.executeUpdate();
        }
    }

    @Override
    public void updatePassword(User user) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE \"user\" SET  password = ? WHERE email = ?");
            statement.setString(1, user.getPassword());
            statement.setString(2, user.getEmail());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(String email) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM \"user\" WHERE email = ?");
            statement.setString(1, email);
            statement.executeUpdate();
        }
    }

    @Override
    public UserList allEmployees() throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"user\" WHERE isemployee = true");
            ResultSet employeeSet = statement.executeQuery();
            UserList employees = new UserList();
            while (employeeSet.next()) {
                String email = employeeSet.getString("email");
                String pass = employeeSet.getString("password");
                String fname = employeeSet.getString("f_name");
                String lname = employeeSet.getString("l_name");
                Date date = (Date) employeeSet.getObject("birthday");
                DateTime bday = new DateTime(date.toLocalDate().getDayOfMonth(), date.toLocalDate().getMonthValue(), date.toLocalDate().getYear());
                int age = employeeSet.getInt("age");
                char gender = employeeSet.getString("gender").charAt(0);
                Employee employee = new Employee(email, pass, fname, lname, bday, gender);
                employees.addUser(employee);
            }
            return employees;
        }
    }

    @Override
    public UserList allCustomers() throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"user\" WHERE isemployee = false");
            ResultSet customerSet = statement.executeQuery();
            UserList customers = new UserList();
            while (customerSet.next()) {
                String email = customerSet.getString("email");
                String pass = customerSet.getString("password");
                String fname = customerSet.getString("f_name");
                String lname = customerSet.getString("l_name");
                Date date = (Date) customerSet.getObject("birthday");
                DateTime bday = new DateTime(date.toLocalDate().getDayOfMonth(), date.toLocalDate().getMonthValue(), date.toLocalDate().getYear());
                int age = customerSet.getInt("age");
                char gender = customerSet.getString("gender").charAt(0);
                Customer customer = new Customer(email, pass, fname, lname, bday, gender);
                customers.addUser(customer);
            }
            return customers;
        }
    }

    @Override
    public UserList allUsers() throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"user\"");
            ResultSet userSet = statement.executeQuery();
            UserList users = new UserList();
            while (userSet.next()) {
                String email = userSet.getString("email");
                String pass = userSet.getString("password");
                String fname = userSet.getString("f_name");
                String lname = userSet.getString("l_name");
                Date date = (Date) userSet.getObject("birthday");
                DateTime bday = new DateTime(date.toLocalDate().getDayOfMonth(), date.toLocalDate().getMonthValue(), date.toLocalDate().getYear());
                int age = userSet.getInt("age");
                char gender = userSet.getString("gender").charAt(0);
                boolean isEmployee = userSet.getBoolean("isemployee");
                if (isEmployee) {
                    Employee employee = new Employee(email, pass, fname, lname, bday, gender);
                    users.addUser(employee);
                } else {
                    Customer customer = new Customer(email, pass, fname, lname, bday, gender);
                    users.addUser(customer);
                }
            }
            return users;
        }
    }

    @Override
    public User readByEmail(String email) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"user\" WHERE email = ?");
            statement.setString(1, email);
            ResultSet userSet = statement.executeQuery();
            if (userSet.next()) {
                String pass = userSet.getString("password");
                String fname = userSet.getString("f_name");
                String lname = userSet.getString("l_name");
                Date date = (Date) userSet.getObject("birthday");
                DateTime bday = new DateTime(date.toLocalDate().getDayOfMonth(), date.toLocalDate().getMonthValue(), date.toLocalDate().getYear());
                int age = userSet.getInt("age");
                char gender = userSet.getString("gender").charAt(0);
                return userSet.getBoolean("isemployee") ? new Employee(email, pass, fname, lname, bday, gender) : new Customer(email, pass, fname, lname, bday, gender);
            }
            return null;
        }
    }
}
