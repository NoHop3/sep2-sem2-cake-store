package common.model;

import java.io.Serializable;
import java.util.HashMap;

public class Order implements Serializable {

    private String id;
    private HashMap<Product, Integer> products = new HashMap<>();
    private DateTime date;
    private Customer customer;
    private String status;
    private String comment;

    /**Constructors*/
    public Order(String id, HashMap<Product, Integer> products, DateTime date, Customer customer, String status, String comment) {
        this.id = id;
        this.products = products;
        this.customer = customer;
        this.date = date;
        this.status = status;
        this.comment = comment;
    }
    public Order(String id, HashMap<Product, Integer> products, Customer customer, String status, String comment) {
        this.id = id;
        this.products = products;
        this.customer = customer;
        this.date = new DateTime();
        this.status = status;
        this.comment = comment;
    }
    public Order(String id, HashMap<Product, Integer> products, Customer customer) {
        this(id, products, customer, "pending", "");
    }
    public Order(HashMap<Product, Integer> products, Customer customer) {
        this("", products, customer, "pending", "");
    }
    public Order(HashMap<Product, Integer> products, Customer customer, String comment) {
        this("", products, customer, "pending", comment);
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public String getStatus() {
        return status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public DateTime getDate() {
        return date;
    }

    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    /**Edit order
     * adding new product*/
    public void addNewProduct(Product product, int quantity) {
        products.put(product, quantity);
    }

    /**Edit order
     * editing product's quantity*/
    public void editProductQuantity(Product product, int quantity) {
        products.put(product, quantity);
    }

    /**Remove product from order*/
    public void removeProduct(Product product) {
        products.remove(product);
    }

    @Override
    public String toString() {
        String output = "Order " + id + " by " + customer.getEmail() + " made on " + date.toString() + " is " + status +"\n";
        for (HashMap.Entry<Product,Integer> entry : products.entrySet()) {
            output += entry.getKey().getName() + " -> " + entry.getValue() + "\n";
        }
        output += "comment: " + comment;
        return output;
    }
}
