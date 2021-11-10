package common.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {
    Employee user;

    @BeforeEach
    void setUp() {
        user = new Employee("bob@gmail.com", "Aaa123456", "Bob", "Bobo", new DateTime(2,3,2005), 'M') ;
    }

    @AfterEach
    void tearDown() {
    }

    /**Testing setters*/
    @Test
    void setNull(){
        /**Email*/
        assertThrows(IllegalArgumentException.class, () -> user.setEmail(""));
        assertThrows(IllegalArgumentException.class, () -> user.setEmail(" "));
        assertThrows(IllegalArgumentException.class, () -> user.setEmail(null));

        /**Password*/
        assertThrows(IllegalArgumentException.class, () -> user.setPassword(""));
        assertThrows(IllegalArgumentException.class, () -> user.setPassword(" "));
        assertThrows(IllegalArgumentException.class, () -> user.setPassword(null));

        /**First Name*/
        assertThrows(IllegalArgumentException.class, () -> user.setFirstName(""));
        assertThrows(IllegalArgumentException.class, () -> user.setFirstName(" "));
        assertThrows(IllegalArgumentException.class, () -> user.setFirstName(null));

        /**Last Name*/
        assertThrows(IllegalArgumentException.class, () -> user.setLastName(""));
        assertThrows(IllegalArgumentException.class, () -> user.setLastName(" "));
        assertThrows(IllegalArgumentException.class, () -> user.setLastName(null));

        /**Birthday
         * all other cases for birthday are tested in dateTime class*/
        assertThrows(IllegalArgumentException.class, () -> user.setBirthday(null));

        /**Gender*/
        assertThrows(IllegalArgumentException.class, () -> user.setGender(' '));
        assertThrows(IllegalArgumentException.class, () -> user.setLastName(null));
    }

    @Test
    void setOne(){
        /**Email*/
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("b"));
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("bob"));

        /**Password*/
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("M"));
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("Mi"));

        /**First Name*/
        assertThrows(IllegalArgumentException.class, () -> user.setFirstName("M"));
        user.setFirstName("Mi");
        assertEquals("Mi", user.getFirstName());

        /**Last Name*/
        assertThrows(IllegalArgumentException.class, () -> user.setLastName("M"));
        user.setLastName("Mi");
        assertEquals("Mi", user.getLastName());

        /**Birthday
         * all other cases for birthday are tested in dateTime class*/

        /**Gender*/
        assertThrows(IllegalArgumentException.class, () -> user.setGender('K'));
        user.setGender('M');
        assertEquals('M', user.getGender());
        user.setGender('F');
        assertEquals('F', user.getGender());
        user.setGender('m');
        assertEquals('m', user.getGender());
        /**so it appears we do no allow lgbtq to buy stuff...our clients just dropped
         * Throws exception: User can either be male or female.
         */
        assertThrows(IllegalArgumentException.class, () -> user.setGender('O'));
    }

    @Test
    void setMany(){
        /**Email*/
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("bobgmail.com"));
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("bob@gmail"));
        user.setEmail("bob@gmail.com");
        assertEquals("bob@gmail.com", user.getEmail());
        user.setEmail("bob@abv.com");
        assertEquals("bob@abv.com", user.getEmail());
        user.setEmail("bob@abv.bg");
        assertEquals("bob@abv.bg", user.getEmail());
        user.setEmail("bob@via.dk");
        assertEquals("bob@via.dk", user.getEmail());

        /**Password*/
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("Mdgkjgje"));
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("Asd123"));
        user.setPassword("Asd123456");
        assertEquals("Asd123456", user.getPassword());

        /**First Name*/
        user.setFirstName("Misho");
        assertEquals("Misho", user.getFirstName());

        /**Last Name*/
        user.setLastName("Misho");
        assertEquals("Misho", user.getLastName());

        /**Birthday
         * all other cases for birthday are tested in dateTime class*/

        /**Gender
         * no more casees*/

    }

    @Test
    void setBoundary(){
        /**No test cases*/
    }

    @Test
    void setExceptions() {
        /**Email*/
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("bobgmail.com"));
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("bob@gmail"));
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("bob.gmail@com"));
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("bob@gmail."));
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("@@.."));
        //assertThrows(IllegalArgumentException.class, () -> user.setEmail("bob@gmai.l"));

        /**Password*/
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("Mdgkjgje"));
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("Asd123"));
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("12343543515435436"));
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("sdfghjkdurydtzsrertyjy213"));

        /**First Name
         * no more cases*/


        /**Last Name
         * no more cases*/

        /**Birthday
         * all other cases for birthday are tested in dateTime class*/

        /**Gender
         * no more casees*/
    }

    @Test
    void getFullName(){
        assertEquals("Bob Bobo", user.getFullName());
    }

    @Test
    void isEmployee() {
        assertTrue(user instanceof Employee);
    }
}