package main.model;

public class CartItem {
    private Product product;
    private int quantity;
    private String size;

    public CartItem(Product product, int quantity, String size) {
        this.product = product;
        this.quantity = quantity;
        this.size = size;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
        }
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public void decreaseQuantity(int amount) {
        this.quantity = Math.max(1, this.quantity - amount);
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
