package common.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeTest {
    private DateTime dateTime;
    private DateTime date;

    @BeforeEach
    void setUp() {
        dateTime = new DateTime(9,2,2001);
        date = new DateTime(9,10,2001);
    }

    /**Test Constructor DateTime(int day, int month, int year)*/
    @Test
    void createNull(){
        assertThrows(IllegalStateException.class, () -> new DateTime(0, 0,0));
        assertThrows(IllegalStateException.class, () -> new DateTime(1, 0,0));
        assertThrows(IllegalStateException.class, () -> new DateTime(1, 1,0));
        assertThrows(IllegalStateException.class, () -> new DateTime(0, 1,0));
        assertThrows(IllegalStateException.class, () -> new DateTime(0, 0,1));
        assertThrows(IllegalStateException.class, () -> new DateTime(0, 1,1));
    }
    @Test
    void createOne(){
        assertThrows(IllegalStateException.class, () -> new DateTime(1, 1,1));
        assertThrows(IllegalStateException.class, () -> new DateTime(4, 3,1));
        assertEquals("01/01/2001", new DateTime(1, 1,2001).toString());
    }
    @Test
    void createMany(){
        assertThrows(IllegalStateException.class, () -> new DateTime(1, 1,1));
        assertThrows(IllegalStateException.class, () -> new DateTime(4, 3,1));
        assertEquals("01/01/2001", new DateTime(1, 1,2001).toString());
        assertEquals("02/02/2021", new DateTime(2, 2,2021).toString());
        assertEquals("29/02/2020", new DateTime(29, 2,2020).toString());
    }
    @Test
    void createBoundary(){
        assertThrows(IllegalStateException.class, () -> new DateTime(-1, -1,-1));
        assertThrows(IllegalStateException.class, () -> new DateTime(-11, -11,-1));
        assertThrows(IllegalStateException.class, () -> new DateTime(29, 2,2001));
        assertEquals("31/01/2001", new DateTime(31, 1,2001).toString());
        assertEquals("30/04/2001", new DateTime(30, 4,2001).toString());
        assertEquals("28/02/2021", new DateTime(28, 2,2021).toString());
        assertEquals("29/02/2020", new DateTime(29, 2,2020).toString());
    }

    @Test
    void createExceptions(){
        assertThrows(IllegalStateException.class, () -> new DateTime(-1, -1,-1));
        assertThrows(IllegalStateException.class, () -> new DateTime(-11, -11,-1));
        assertThrows(IllegalStateException.class, () -> new DateTime(23, 3,1899));
    }

    @Test
    void yearsBetween(){
        int yb = DateTime.yearsBetween(dateTime);
        assertEquals(20, yb);
        yb = DateTime.yearsBetween(date);
        assertEquals(19, yb);
    }
}