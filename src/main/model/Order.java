package main.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private String id;
    private String username;
    private List<CartItem> items;
    private double total;
    private LocalDateTime date;

    public Order() {
    }

    public Order(String id, String username, List<CartItem> items, double total, LocalDateTime date) {
        this.id = id;
        this.username = username;
        this.items = items;
        this.total = total;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public LocalDateTime getDate() {
        return date;
    }

    
}
