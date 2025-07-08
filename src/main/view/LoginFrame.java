package main.view;

import main.dao.UserDAO;
import main.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginFrame() {
        setTitle("Đăng nhập");
        setSize(300, 220); // Tăng chiều cao để thêm nút đăng ký
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setBounds(20, 20, 100, 25);
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(130, 20, 130, 25);
        panel.add(txtUsername);

        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setBounds(20, 55, 100, 25);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(130, 55, 130, 25);
        panel.add(txtPassword);

        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setBounds(90, 90, 120, 30);
        panel.add(btnLogin);

        // Thêm nút đăng ký
        JButton btnRegister = new JButton("Đăng ký");
        btnRegister.setBounds(90, 130, 120, 30);
        btnRegister.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });
        panel.add(btnRegister);

        btnLogin.addActionListener(e -> doLogin());

        add(panel);
    }

// Trong phương thức doLogin()
private void doLogin() {
    String username = txtUsername.getText().trim();
    String password = new String(txtPassword.getPassword()).trim();

    User user = UserDAO.login(username, password);
    if (user != null) {
        JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
        dispose();

        if ("admin".equalsIgnoreCase(user.getRole())) {
            new MainFrame(user); // Truyền user cho trang admin
        } else {
            new UserHomeFrame(user); // Truyền user cho trang người dùng
        }
    } else {
        JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!");
    }
}
}