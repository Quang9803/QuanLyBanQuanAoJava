package main.dao;

import java.util.List;
import main.model.User;
import main.utils.FileUtils;

public class UserDAO {
    private static final String FILE_PATH = "data/users.json";
    private static List<User> users = FileUtils.readListFromFile(FILE_PATH, User[].class);

    public static User login(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst().orElse(null);
    }

    public static List<User> getAll() {
        return users;
    }

    public static void add(User u) {
        users.add(u);
        FileUtils.writeListToFile(FILE_PATH, users);
    }

    public static void delete(String username) {
        users.removeIf(u -> u.getUsername().equals(username));
        FileUtils.writeListToFile(FILE_PATH, users);
    }

    public static void update(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                break;
            }
        }
        FileUtils.writeListToFile(FILE_PATH, users);
    }
}
