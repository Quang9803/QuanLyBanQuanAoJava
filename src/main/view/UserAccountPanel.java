package main.view;

import main.dao.UserDAO;
import main.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UserAccountPanel extends JPanel {
    private User currentUser;
    private JTextField emailField;
    private JTextField phoneField;
    private Runnable onBack;
    private Runnable onLogout;

    public UserAccountPanel(User user, Runnable onBack, Runnable onLogout) {
        this.currentUser = user;
        this.onBack = onBack;
        this.onLogout = onLogout;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initUI();
    }

    private void initUI() {
        // === TOP BAR: nút quay lại bên trái và tiêu đề ===
        JPanel topPanel = new JPanel(new BorderLayout());

        JButton backBtn = new JButton("← Quay lại");
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> onBack.run());
        topPanel.add(backBtn, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("THÔNG TIN TÀI KHOẢN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // === FORM THÔNG TIN ===
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        infoPanel.add(new JLabel("Tên đăng nhập:"));
        JTextField usernameField = new JTextField(currentUser.getUsername());
        usernameField.setEditable(false);
        infoPanel.add(usernameField);

        infoPanel.add(new JLabel("Email:"));
        emailField = new JTextField(currentUser.getEmail());
        infoPanel.add(emailField);

        infoPanel.add(new JLabel("Số điện thoại:"));
        phoneField = new JTextField(currentUser.getPhone());
        infoPanel.add(phoneField);

        infoPanel.add(new JLabel("Vai trò:"));
        JTextField roleField = new JTextField(
            currentUser.getRole().equals("admin") ? "Quản trị viên" : "Người dùng");
        roleField.setEditable(false);
        infoPanel.add(roleField);

        infoPanel.add(new JLabel("Mật khẩu:"));
        JButton changePassBtn = new JButton("Đổi mật khẩu");
        changePassBtn.addActionListener(this::doiMatKhau);
        infoPanel.add(changePassBtn);

        add(infoPanel, BorderLayout.CENTER);

        // === BOTTOM BUTTONS: Lưu, Đăng xuất ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton saveBtn = new JButton("Lưu thay đổi");
        saveBtn.addActionListener(this::luuThayDoi);
        buttonPanel.add(saveBtn);

        JButton logoutBtn = new JButton("Đăng xuất");
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                onLogout.run();
            }
        });
        buttonPanel.add(logoutBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void luuThayDoi(ActionEvent e) {
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!phone.matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải có 10-11 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentUser.setEmail(email);
        currentUser.setPhone(phone);
        UserDAO.update(currentUser);

        JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private void doiMatKhau(ActionEvent e) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JPasswordField currentPassField = new JPasswordField();
        JPasswordField newPassField = new JPasswordField();
        JPasswordField confirmPassField = new JPasswordField();

        panel.add(new JLabel("Mật khẩu hiện tại:"));
        panel.add(currentPassField);
        panel.add(new JLabel("Mật khẩu mới:"));
        panel.add(newPassField);
        panel.add(new JLabel("Xác nhận mật khẩu:"));
        panel.add(confirmPassField);

        int result = JOptionPane.showConfirmDialog(
            this, panel, "Đổi mật khẩu",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String currentPass = new String(currentPassField.getPassword());
            String newPass = new String(newPassField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());

            if (!currentUser.getPassword().equals(currentPass)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu hiện tại không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu mới không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (newPass.length() < 6) {
                JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 6 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            currentUser.setPassword(newPass);
            UserDAO.update(currentUser);
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
