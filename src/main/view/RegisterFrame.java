package main.view;

import main.dao.UserDAO;
import main.model.User;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtEmail;
    private JTextField txtPhone;

    public RegisterFrame() {
        setTitle("Đăng ký tài khoản");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Username
        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setBounds(20, 20, 120, 25);
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(150, 20, 200, 25);
        panel.add(txtUsername);

        // Password
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setBounds(20, 50, 120, 25);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 50, 200, 25);
        panel.add(txtPassword);

        // Confirm Password
        JLabel lblConfirmPassword = new JLabel("Nhập lại mật khẩu:");
        lblConfirmPassword.setBounds(20, 80, 120, 25);
        panel.add(lblConfirmPassword);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(150, 80, 200, 25);
        panel.add(txtConfirmPassword);

        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(20, 110, 120, 25);
        panel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(150, 110, 200, 25);
        panel.add(txtEmail);

        // Phone
        JLabel lblPhone = new JLabel("Số điện thoại:");
        lblPhone.setBounds(20, 140, 120, 25);
        panel.add(lblPhone);

        txtPhone = new JTextField();
        txtPhone.setBounds(150, 140, 200, 25);
        panel.add(txtPhone);

        // Register Button
        JButton btnRegister = new JButton("Đăng ký");
        btnRegister.setBounds(150, 180, 100, 30);
        btnRegister.addActionListener(e -> doRegister());
        panel.add(btnRegister);

        // Login Link
        JLabel loginLink = new JLabel("Đã có tài khoản? Đăng nhập");
        loginLink.setBounds(120, 220, 160, 25);
        loginLink.setForeground(Color.BLUE);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                new LoginFrame();
            }
        });
        panel.add(loginLink);

        add(panel);
    }

    private void doRegister() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || 
            email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 6 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!phone.matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create new user
        User newUser = new User(username, password, email, phone, "user");
        
        if (UserDAO.register(newUser)) {
            JOptionPane.showMessageDialog(this, "Đăng ký thành công! Vui lòng đăng nhập.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginFrame();
        } else {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc email đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}