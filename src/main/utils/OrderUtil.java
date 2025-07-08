package main.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import main.model.Order;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderUtil {
    private static final String FILE_PATH = "data/orders.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void saveOrder(Order newOrder) {
        List<Order> orders = loadOrders();
        orders.add(newOrder);

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), orders);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Order> loadOrders() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            CollectionType listType = mapper.getTypeFactory()
                                             .constructCollectionType(ArrayList.class, Order.class);
            return mapper.readValue(file, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
