package main.view;

import main.model.Cart;
import main.model.Product;
import main.model.CartItem;
import main.model.FavoriteList;

import javax.swing.*;
import java.awt.*;

public class ProductDetailPanel extends JPanel {
    public ProductDetailPanel(Product product, FavoriteList favoriteList, Runnable onBack) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel imageLabel;
        try {
            ImageIcon icon = new ImageIcon(product.getImagePath());
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel = new JLabel("No Image");
        }
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(imageLabel);

        add(Box.createVerticalStrut(15));
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(nameLabel);

        add(Box.createVerticalStrut(10));
        JLabel priceLabel = new JLabel(String.format("%,.0f đ", product.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        priceLabel.setForeground(Color.RED);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(priceLabel);

        add(Box.createVerticalStrut(10));
        JLabel sizeLabel = new JLabel("Chọn size:");
        sizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(sizeLabel);

        JComboBox<String> sizeCombo = new JComboBox<>(new String[]{"S", "M", "L", "XL"});
        sizeCombo.setMaximumSize(new Dimension(100, 25));
        sizeCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(sizeCombo);

        add(Box.createVerticalStrut(15));
        JTextArea descArea = new JTextArea(product.getDescription());
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(descArea);
        scrollPane.setPreferredSize(new Dimension(350, 100));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(scrollPane);

        add(Box.createVerticalStrut(15));
        JButton addBtn = new JButton("Thêm vào giỏ hàng");
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBtn.addActionListener(e -> {
            String selectedSize = sizeCombo.getSelectedItem().toString();
            Cart.getInstance().addProduct(product, selectedSize);
            JOptionPane.showMessageDialog(this, "Đã thêm vào giỏ hàng (Size " + selectedSize + ")!");
        });
        add(addBtn);

        add(Box.createVerticalStrut(10));
        JButton favoriteBtn = new JButton("❤️ Thêm vào yêu thích");
        favoriteBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        favoriteBtn.addActionListener(e -> {
            String selectedSize = sizeCombo.getSelectedItem().toString();
            CartItem item = new CartItem(product, 1, selectedSize);
            favoriteList.addFavorite(item);
            JOptionPane.showMessageDialog(this, "Đã thêm vào danh sách yêu thích!");
        });
        add(favoriteBtn);

        add(Box.createVerticalStrut(10));
        JButton backBtn = new JButton("← Quay lại");
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(e -> onBack.run());
        add(backBtn);
    }
}