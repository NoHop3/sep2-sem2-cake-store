package common.model;

import java.io.Serializable;

public class Product implements Serializable {

    private String id;
    private int quantity;
    private String name;
    private String description;
    private double price;

    public Product(String id, int quantity, String name, String description, double price) throws IllegalArgumentException {
        setId(id);
        setQuantity(quantity);
        setName(name);
        setDescription(description);
        setPrice(price);
    }

    public void setId(String id) throws IllegalArgumentException {
        if (id == null || id.isEmpty() || id.trim().isEmpty()) throw new IllegalArgumentException("Product id can't be empty.");
        try {
            if (Integer.parseInt(id) < 1) throw new IllegalArgumentException("Product id can't be less than 1.");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Product id must be a whole number greater then 1.");
        }
        this.id = id;
    }

    public void setQuantity(int quantity) throws IllegalArgumentException {
        if (quantity < 1) throw new IllegalArgumentException("Product quantity can't be less then 1.");
        this.quantity = quantity;
    }

    public void setName(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty() || name.trim().isEmpty()) throw new IllegalArgumentException("Product name can't be empty.");
        if (name.length() > 100) throw new IllegalArgumentException("Product name can't be longer then 100 chars.");
        this.name = name;
    }

    public void setDescription(String description) throws IllegalArgumentException {
        if (description == null) description = "";
        if (description.length() > 10000) throw new IllegalArgumentException("Product description can't be longer then 10 000 chars.");
        this.description = description;
    }

    public void setPrice(double price) throws IllegalArgumentException {
        if (price <= 0) throw new IllegalArgumentException("Product should be greater then 0.");
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "id='" + id + '\'' +
                ", quantity=" + quantity +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Product)) return false;
        Product tmp = (Product) obj;
        return id.equals(tmp.id);
    }
}
