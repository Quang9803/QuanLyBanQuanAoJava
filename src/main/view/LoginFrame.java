package main.view;

import main.dao.UserDAO;
import main.model.User;

import javax.swing.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginFrame() {
        setTitle("Đăng nhập");
        setSize(300, 180);
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

        btnLogin.addActionListener(e -> doLogin());

        add(panel);
    }

    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        User user = UserDAO.login(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
            dispose(); // đóng form login

            // Mở giao diện theo vai trò
            if ("admin".equalsIgnoreCase(user.getRole())) {
                new MainFrame(user);
            } else {
                new UserHomeFrame(); // giao diện người dùng thường
            }

        } else {
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu.");
        }
    }
}
