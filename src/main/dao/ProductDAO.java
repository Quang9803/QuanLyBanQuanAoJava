/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.dao;

import java.util.List;
import main.model.Product;
import main.utils.FileUtils;

/**
 *
 * @author phanvinhquang
 */
public class ProductDAO {
    private static final String FILE_PATH = "data/products.json";
    private static List<Product> products;

static {
    products = FileUtils.readListFromFile(FILE_PATH, Product[].class);
    if (products == null) {
        products = new java.util.ArrayList<>();
    }
}


    public static List<Product> getAll() { return products; }

    public static void add(Product p) {
        products.add(p);
        FileUtils.writeListToFile(FILE_PATH, products);
    }

    public static void delete(String id) {
        products.removeIf(p -> p.getId().equals(id));
        FileUtils.writeListToFile(FILE_PATH, products);
    }

    public static void update(Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(updatedProduct.getId())) {
                products.set(i, updatedProduct);
                break;
            }
        }
        FileUtils.writeListToFile(FILE_PATH, products);
    }
}

