package musicline.cmov.org.feup.musicline.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class Order {
    String id;
    String customerId;
    HashMap<String, Integer> products;
    ArrayList<String> vouchers;
    double totalPrice;
    boolean validated;

    public Order(String id, String customerId, HashMap<String, Integer> products, ArrayList<String> vouchers, double totalPrice, boolean validated) {
        this.id = id;
        this.customerId = customerId;
        this.products = products;
        this.vouchers = vouchers;
        this.totalPrice = totalPrice;
        this.validated = validated;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", products=" + products +
                ", vouchers=" + vouchers +
                ", totalPrice=" + totalPrice +
                ", validated=" + validated +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public HashMap<String, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<String, Integer> products) {
        this.products = products;
    }

    public ArrayList<String> getVouchers() {
        return vouchers;
    }

    public void setVouchers(ArrayList<String> vouchers) {
        this.vouchers = vouchers;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }
}
