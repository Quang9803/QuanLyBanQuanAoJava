package main.view;

import main.model.Cart;
import main.model.Product;

import javax.swing.*;
import java.awt.*;

public class ProductDetailFrame extends JFrame {

    public ProductDetailFrame(Product product) {
        setTitle("Chi tiết sản phẩm");
        setSize(400, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel loadingLabel = new JLabel("Đang tải sản phẩm...", SwingConstants.CENTER);
        add(loadingLabel, BorderLayout.CENTER);
        setVisible(true);

        new SwingWorker<JPanel, Void>() {
            @Override
            protected JPanel doInBackground() {
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
                mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                mainPanel.setBackground(Color.WHITE);

                // Hình ảnh
                JLabel imageLabel;
                try {
                    ImageIcon icon = new ImageIcon(product.getImagePath());
                    Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_FAST);
                    imageLabel = new JLabel(new ImageIcon(img));
                } catch (Exception e) {
                    imageLabel = new JLabel("No Image");
                }
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                mainPanel.add(imageLabel);

                // Tên sản phẩm
                JLabel nameLabel = new JLabel(product.getName());
                nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
                nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                mainPanel.add(Box.createVerticalStrut(15));
                mainPanel.add(nameLabel);

                // Giá
                JLabel priceLabel = new JLabel(String.format("%,.0f đ", product.getPrice()));
                priceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                priceLabel.setForeground(Color.RED);
                mainPanel.add(Box.createVerticalStrut(10));
                mainPanel.add(priceLabel);

                // Size chọn
                JLabel sizeLabel = new JLabel("Chọn size:");
                sizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                String[] sizes = {"S", "M", "L", "XL"};
                JComboBox<String> sizeCombo = new JComboBox<>(sizes);
                sizeCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
                mainPanel.add(Box.createVerticalStrut(10));
                mainPanel.add(sizeLabel);
                mainPanel.add(Box.createVerticalStrut(5));
                mainPanel.add(sizeCombo);

                // Mô tả
                JTextArea descArea = new JTextArea(product.getDescription() != null ? product.getDescription() : "Không có mô tả.");
                descArea.setWrapStyleWord(true);
                descArea.setLineWrap(true);
                descArea.setEditable(false);
                descArea.setBackground(Color.WHITE);
                JScrollPane scrollPane = new JScrollPane(descArea);
                scrollPane.setPreferredSize(new Dimension(350, 150));
                mainPanel.add(Box.createVerticalStrut(15));
                mainPanel.add(scrollPane);

                // Nút thêm giỏ hàng
                JButton addToCart = new JButton("Thêm vào giỏ hàng");
                addToCart.setAlignmentX(Component.CENTER_ALIGNMENT);
                addToCart.addActionListener(e -> {
                    String selectedSize = sizeCombo.getSelectedItem().toString(); // ✅ lấy size đã chọn
                    Cart.getInstance().addProduct(product, selectedSize);

                    JOptionPane.showMessageDialog(ProductDetailFrame.this, "Đã thêm vào giỏ hàng (Size " + selectedSize + ")!");
                });

                mainPanel.add(Box.createVerticalStrut(20));
                mainPanel.add(addToCart);

                return mainPanel;
            }

            @Override
            protected void done() {
                try {
                    JPanel loadedPanel = get();
                    getContentPane().removeAll();
                    add(loadedPanel, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.execute();
    }
}
