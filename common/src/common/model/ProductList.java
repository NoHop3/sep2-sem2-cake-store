package common.model;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductList {

    private HashMap<String, Product> productList;

    public ProductList() {
        productList = new HashMap<>();
    }

    public ArrayList<Product> getAllProducts() {
        return new ArrayList<>(productList.values());
    }

    public void clear() {
        productList.clear();
    }

    public int getSize() {
        return new ArrayList<>(productList.values()).size();
    }

    public void addProduct(Product product) throws IllegalStateException, IllegalArgumentException {
        if (productList.get(product.getId()) != null) throw new IllegalStateException("Product is already in the basket.");
        productList.put(product.getId(), product);
    }

    public void replaceProduct(Product product) throws IllegalStateException {
        if (productList.get(product.getId()) == null) throw new IllegalStateException("No such product could be found in the basket.");
        productList.remove(product.getId());
        productList.put(product.getId(), product);
    }
    public void replaceProduct(Product product, Product product2) throws IllegalStateException {
        if (productList.get(product.getId()) == null) throw new IllegalStateException("No such product could be found in the basket.");
        productList.remove(product.getId());
        productList.put(product2.getId(), product2);
    }

    public void removeProduct(String productId) throws IllegalStateException {
        if (productList.get(productId) == null) throw new IllegalStateException("No such product could be found in the basket.");
        productList.remove(productId);
    }
    public Product removeProduct(Product product) throws IllegalStateException{
        if(productList.get(product.getId()) == null) throw new IllegalStateException("No such product could be found ");
        return productList.remove(product.getId());
    }
    public Product getProduct(String id) throws IllegalStateException {
        if(id!=null)
            if(productList.get(id)!=null)return productList.get(id);
        throw new IllegalStateException("Id cannot be null");
    }

    public Product getProduct(Product product) throws IllegalStateException{
        if(productList.get(product.getId())!=null)
            return productList.get(product.getId());
        throw new IllegalStateException("Product argument cannot be found");
    }
}
