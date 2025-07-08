package main.view;

import main.model.CartItem;
import main.model.FavoriteList;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WishlistPanel extends JPanel {
    public WishlistPanel(FavoriteList favoriteList, Runnable onBack) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        List<CartItem> favorites = favoriteList.getFavorites();

        if (favorites.isEmpty()) {
            JLabel emptyLabel = new JLabel("Chưa có sản phẩm yêu thích.");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(Box.createVerticalGlue());
            mainPanel.add(emptyLabel);
            mainPanel.add(Box.createVerticalGlue());
        } else {
            for (CartItem item : favorites) {
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
                itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                itemPanel.setBackground(Color.WHITE);
                itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

                // Hình ảnh
                JLabel imgLabel = new JLabel();
                try {
                    ImageIcon icon = new ImageIcon(item.getProduct().getImagePath());
                    Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    imgLabel.setIcon(new ImageIcon(scaled));
                } catch (Exception e) {
                    imgLabel.setText("No Image");
                }
                imgLabel.setPreferredSize(new Dimension(80, 80));
                itemPanel.add(imgLabel);
                itemPanel.add(Box.createHorizontalStrut(20));

                // Thông tin
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setBackground(Color.WHITE);
                infoPanel.add(new JLabel("Tên: " + item.getProduct().getName()));
                infoPanel.add(new JLabel("Size: " + item.getSize()));
                infoPanel.add(new JLabel("Giá: " + String.format("%,.0f đ", item.getProduct().getPrice())));
                itemPanel.add(infoPanel);
                itemPanel.add(Box.createHorizontalGlue());

                // Nút xóa
                JButton removeBtn = new JButton("Xóa");
                removeBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
                removeBtn.addActionListener(e -> {
                    favoriteList.remove(item.getProduct(), item.getSize());
                    SwingUtilities.invokeLater(() -> onBack.run()); // Gọi lại callback để làm mới danh sách
                });
                itemPanel.add(removeBtn);

                mainPanel.add(itemPanel);
                mainPanel.add(Box.createVerticalStrut(10));
            }
        }

        // Nút quay lại
        JButton backButton = new JButton("← Quay lại");
        backButton.addActionListener(e -> onBack.run());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
    }
}
