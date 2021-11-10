package common.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserListTest {

    UserList list;
    Employee employee1;
    Employee employee2;
    Employee employee3;
    Customer customer1;
    Customer customer2;
    Customer customer3;

    @BeforeEach
    void setUp() {
        System.out.println("--> Set up ");
        employee1 = new Employee("bob@gmail.com", "Aaa123456", "Bob", "Bobo", new DateTime(2,3,2005), 'M') ;
        employee2 = new Employee("steve@gmail.com", "Aaaa123456", "Steve", "Jobs", new DateTime(2,12,2000), 'M') ;
        employee3 = new Employee("katerina_kat@gmail.com", "Aaaa1234526", "Katerina", "Kat", new DateTime(12,2,1999), 'F') ;
        customer1 = new Customer("Giorgio@ammaroni.hispanico", "Qwerty123", "Giorgio", "Hispanico", new DateTime(13, 11, 2000), 'M');
        customer2 = new Customer("whatisthatemail@gmail.com", "Qwerty123", "Customer2", "FamilyName", new DateTime(13, 11, 2000), 'M');
        customer3 = new Customer("idkthatemailisstrangefr@gmail.com", "Qwerty123", "Customer3", "NoFamilyRip", new DateTime(13, 11, 2000), 'M');
        list = new UserList();
        list.addUser(employee1);
        list.addUser(employee2);
        list.addUser(employee3);
        list.addUser(customer1);
        list.addUser(customer2);
        list.addUser(customer3);
    }

    @AfterEach
    void tearDown() {
        System.out.println("<-- Tear down");
    }

    @Test
    void getSize() {
        System.out.println("Get size test");
        UserList list2 = new UserList();
        assertEquals(0, list2.getSize());
        list2.addUser(customer1);
        assertEquals(1, list2.getSize());
    }

    @Test
    void getAllUsers() {
        System.out.println("Get all users test");
        UserList list2 = new UserList();
        list2.addUser(customer1);
        ArrayList<User> users = new ArrayList<>();users.add(customer1);
        assertEquals(users, list2.getAllUsers());
        list2.addUser(employee1);
        users.add(0,employee1);
        assertEquals(users, list2.getAllUsers());
        list2.addUser(employee3);
        users.add(0,employee3);
        assertEquals(users, list2.getAllUsers());
    }

    @Test
    void addUser() {
        System.out.println("Add user test");
        assertEquals(employee1, list.getUser(employee1));
        assertThrows(IllegalArgumentException.class, () -> list.addUser(null));
    }

    @Test
    void removeUser() {
        System.out.println("Remove user test");
        // List is empty
        String email = null;
        assertThrows(IllegalArgumentException.class, () -> list.removeUser(email));
        User user = null;
        assertThrows(IllegalArgumentException.class, () -> list.removeUser(user));
        assertEquals(customer3, list.removeUser(customer3));
        list.addUser(customer3);
        assertEquals(customer3, list.removeUser(customer3.getEmail()));
    }


    @Test
    void getUser() {
        System.out.println("Get user test");
        // List is empty
        String email = null;
        assertThrows(IllegalArgumentException.class, () -> list.getUser(email));
        User user = null;
        assertThrows(IllegalArgumentException.class, () -> list.getUser(user));
        assertEquals(customer3, list.getUser(customer3));
        assertEquals(customer3, list.getUser(customer3.getEmail()));
    }

}