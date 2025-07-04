package main.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static Cart instance;
    private final List<CartItem> items = new ArrayList<>();

    private Cart() {}

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void addProduct(Product product, String size) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId()) &&
                item.getSize().equalsIgnoreCase(size)) {
                item.increaseQuantity(1);
                return;
            }
        }
        items.add(new CartItem(product, 1, size)); // 

    }

    public List<CartItem> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }

    public double getTotal() {
        return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    // ❌ Không còn dùng được khi có size
    public void removeByName(String name) {
        items.removeIf(item -> item.getProduct().getName().equalsIgnoreCase(name));
    }

    public void removeById(String id) {
        items.removeIf(item -> item.getProduct().getId().equals(id));
    }

    // ✅ Hàm xóa theo tên + size
    public void removeByNameAndSize(String name, String size) {
        items.removeIf(item ->
                item.getProduct().getName().equalsIgnoreCase(name) &&
                item.getSize().equalsIgnoreCase(size));
    }

}
