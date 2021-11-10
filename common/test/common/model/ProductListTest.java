package common.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProductListTest {

    ProductList list = new ProductList();
    Product product1;
    Product product2;
    Product product3;
    Product product4;
    
    @BeforeEach
    void setUp() {
        System.out.println("--> Set up");
        product1 = new Product("1", 5, "Baklava", "Baklava is Balkan", 20);
        product2 = new Product("2", 15, "Baklava2", "Baklava is Muslim", 220);
        product3 = new Product("3", 25, "Baklava3", "Baklava is Azerbaijani", 2220);
        product4 = new Product("4", 23, "Your mom's cake", "The name says it all", 69.96);
        list.addProduct(product1);
        list.addProduct(product2);
        list.addProduct(product3);
    }

    @AfterEach
    void tearDown() {
        System.out.println("<-- Tear down");
    }

    @Test
    void getAllProducts() {
        System.out.println("Get all products test");
        ProductList list2 = new ProductList();
        list2.addProduct(product1);
        ArrayList<Product> products = new ArrayList<>();products.add(product1);
        assertEquals(products, list2.getAllProducts());
        list2.addProduct(product2);
        products.add(product2);
        assertEquals(products, list2.getAllProducts());
        list2.addProduct(product3);
        products.add(product3);
        assertEquals(products, list2.getAllProducts());
    }
    @Test
    void getSize(){
        System.out.println("Get size test");
        assertEquals(3, list.getSize());
    }

    @Test
    void clear() {
        System.out.println("Clear list test");
        assertEquals(3, list.getSize());
        list.clear();
        assertEquals(0, list.getSize());
    }

    @Test
    void addProduct() {
        //Check to add existing product
        assertThrows(IllegalStateException.class, () -> list.addProduct(product1));
        list.addProduct(product4);
        //Checks for product are already made in class product.
    }

    @Test
    void replaceProduct() {
        assertThrows(IllegalStateException.class, () -> list.replaceProduct(product4));
        list.replaceProduct(product3, product4);
        assertEquals(product4, list.getProduct(product4));
    }

    @Test
    void removeProduct() {
        System.out.println("Remove product test");
        assertThrows(IllegalStateException.class, () -> list.removeProduct(product4));
        list.removeProduct(product3.getId());
        list.removeProduct(product2);
        assertEquals(1, list.getSize());
    }
    @Test
    void getProduct() {
        System.out.println("Get product test");
        assertThrows(IllegalStateException.class, () -> list.getProduct(product4));
        assertEquals(product2, list.getProduct(product2));
        assertEquals(product2, list.getProduct(product2.getId()));
    }
}