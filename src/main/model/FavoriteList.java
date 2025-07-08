
package main.model;

import java.util.ArrayList;
import java.util.List;

public class FavoriteList {
    private List<CartItem> favorites = new ArrayList<>();

    public void addFavorite(CartItem item) {
        if (!favorites.contains(item)) {
            favorites.add(item);
        }
    }

    public List<CartItem> getFavorites() {
        return favorites;
    }

    public void remove(Product product, String size) {
    favorites.removeIf(item -> 
        item.getProduct().getId() == product.getId() &&
        item.getSize().equals(size)
    );
    }
}
