package main.view;

import main.dao.ProductDAO;
import main.model.Cart;
import main.model.Product;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserHomeFrame extends JFrame {
    private JPanel contentPanel;

    public UserHomeFrame() {
        setTitle("Trang chủ người dùng");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Navbar
        add(createNavbar(), BorderLayout.NORTH);

        // Content
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(25);
        add(scrollPane, BorderLayout.CENTER);

        showAllCategoriesWithProducts();

        setVisible(true);
    }

    private JPanel createNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setPreferredSize(new Dimension(1000, 50));
        navbar.setBackground(Color.WHITE);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel logo = new JLabel("CL Store");
        logo.setFont(new Font("Arial", Font.BOLD, 20));
        logo.setForeground(Color.RED);
        leftPanel.add(logo);

        String[] categories = {"NỮ", "NAM", "TRẺ EM"};
        for (String cat : categories) {
        JButton btn = new JButton(cat);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(Color.WHITE);

        final String selected = cat; // Cố định biến cho mỗi button
        btn.addActionListener(e -> showProductsByCategory(selected));
        leftPanel.add(btn);
    }



        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        String[] labels = {"Tìm kiếm", "Tài khoản", "Yêu thích", "Giỏ hàng"};
        for (String label : labels) {
            JButton btn = new JButton(label);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.PLAIN, 13));
            if (label.equals("Giỏ hàng")) {
            btn.addActionListener(e -> showCartPanel());  
            }

            rightPanel.add(btn);
        }

        navbar.add(leftPanel, BorderLayout.WEST);
        navbar.add(rightPanel, BorderLayout.EAST);
        return navbar;
    }

    private void showAllCategoriesWithProducts() {
        contentPanel.removeAll();
        contentPanel.add(new JLabel("Đang tải danh mục..."));
        contentPanel.revalidate();
        contentPanel.repaint();

        new SwingWorker<List<JComponent>, Void>() {
        @Override
        protected List<JComponent> doInBackground() {
        List<JComponent> components = new ArrayList<>();
        String[] categories = {"NAM", "NỮ", "TRẺ EM"};

        for (String category : categories) {
            JPanel sectionPanel = new JPanel();
            sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
            sectionPanel.setBackground(Color.WHITE);
            sectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            sectionPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // 👈 căn giữa cả block

            // Tiêu đề
            JButton titleBtn = new JButton(category);
            titleBtn.setFont(new Font("Arial", Font.BOLD, 16));
            titleBtn.setFocusPainted(false);
            titleBtn.setContentAreaFilled(false);
            titleBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            titleBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // 👈 căn giữa tiêu đề
            titleBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            String selectedCategory = category;
            titleBtn.addActionListener(e -> showProductsByCategory(selectedCategory));
            sectionPanel.add(titleBtn);

            // Sản phẩm
            List<Product> filtered = ProductDAO.getAll().stream()
                .filter(p -> selectedCategory.equalsIgnoreCase(p.getCategory()))
                .collect(Collectors.toList());

            Collections.shuffle(filtered);
            List<Product> limited = filtered.stream().limit(4).collect(Collectors.toList());

            JPanel gridPanel = new JPanel(new GridLayout(1, 4, 10, 10));
            gridPanel.setBackground(Color.WHITE);
            gridPanel.setMaximumSize(new Dimension(800, 200)); // 👈 giới hạn chiều ngang
            gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // 👈 căn giữa khung sản phẩm

            for (Product p : limited) {
                gridPanel.add(createProductCard(p));
            }

            // Bù cho đủ 4 sản phẩm
            while (gridPanel.getComponentCount() < 4) {
                gridPanel.add(Box.createHorizontalStrut(150));
            }

            sectionPanel.add(gridPanel);
            components.add(sectionPanel);
        }

        return components;
    }



        @Override
        protected void done() {
            try {
                contentPanel.removeAll();
                for (JComponent comp : get()) {
                    contentPanel.add(comp);
                }
                contentPanel.revalidate();
                contentPanel.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }.execute();
}


    private void showProductsByCategory(String category) {
        contentPanel.removeAll();
        contentPanel.add(new JLabel("Đang tải sản phẩm..."));
        contentPanel.revalidate();
        contentPanel.repaint();

        new SwingWorker<List<Product>, Void>() {
            @Override
            protected List<Product> doInBackground() {
                return ProductDAO.getAll().stream()
                        .filter(p -> category.equalsIgnoreCase(p.getCategory()))
                        .collect(Collectors.toList());
            }

            @Override
            protected void done() {
                try {
                    List<Product> products = get();
                    contentPanel.removeAll();

                    JPanel gridPanel = new JPanel(new GridLayout(0, 5, 10, 10));
                    gridPanel.setBackground(Color.WHITE);

                    for (Product p : products) {
                        gridPanel.add(createProductCard(p));
                    }

                    JButton backButton = new JButton("← Quay lại");
                    backButton.addActionListener(e -> showAllCategoriesWithProducts());

                    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    topPanel.add(backButton);
                    contentPanel.add(topPanel);

                    contentPanel.add(Box.createVerticalStrut(10));
                    contentPanel.add(gridPanel);
                    contentPanel.revalidate();
                    contentPanel.repaint();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.execute();
    }


    public JPanel createProductCard(Product p) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(150, 220));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel imageLabel;
        try {
            ImageIcon icon = new ImageIcon(p.getImagePath());
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel = new JLabel("No Image");
        }
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(p.getName());
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel priceLabel = new JLabel(String.format("%,.0f đ", p.getPrice()));
        priceLabel.setForeground(Color.BLACK);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 13));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton cartBtn = new JButton("+");
        cartBtn.setFocusPainted(false);
        cartBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        cartBtn.addActionListener(e -> {
            Cart.getInstance().addProduct(p, "M"); 
            JOptionPane.showMessageDialog(card, "Đã thêm vào giỏ hàng!");
        });

        // 👉 Mở cửa sổ chi tiết khi click vào toàn bộ card (trừ nút giỏ hàng)
        card.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            contentPanel.removeAll();
            contentPanel.add(new ProductDetailPanel(p, UserHomeFrame.this::showAllCategoriesWithProducts));
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    });


        card.add(Box.createVerticalGlue());
        card.add(imageLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(cartBtn);
        card.add(Box.createVerticalGlue());

        return card;
    }
    private void showCartPanel() {
        contentPanel.removeAll();
        contentPanel.add(new CartPanel());  // Panel hiển thị giỏ hàng
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    public void setContentPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }




    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        SwingUtilities.invokeLater(UserHomeFrame::new);
    }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

