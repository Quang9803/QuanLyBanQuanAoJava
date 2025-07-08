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

    public static boolean register(User newUser) {
        // Kiểm tra username và email đã tồn tại chưa
        if (isUsernameExists(newUser.getUsername())) {
            return false;
        }
        if (isEmailExists(newUser.getEmail())) {
            return false;
        }
        
        // Mặc định role là user khi đăng ký
        newUser.setRole("user");
        
        users.add(newUser);
        return FileUtils.writeListToFile(FILE_PATH, users);
    }

    public static boolean isUsernameExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }

    public static boolean isEmailExists(String email) {
        return users.stream().anyMatch(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(email));
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