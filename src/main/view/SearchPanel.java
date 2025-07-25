package main.view;

import main.dao.ProductDAO;
import main.model.Product;
import main.model.Cart;
import main.model.FavoriteList;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SearchPanel extends JPanel {
    private JTextField searchField;
    private JPanel resultsPanel;
    private Runnable onBack;
    private FavoriteList favoriteList;

    public SearchPanel(Runnable onBack, FavoriteList favoriteList) {
        this.onBack = onBack;
        this.favoriteList = favoriteList;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Search bar
        JPanel searchBarPanel = new JPanel(new BorderLayout());
        searchBarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchBarPanel.setBackground(Color.WHITE);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 30));
        searchField.addActionListener(e -> performSearch());

        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.addActionListener(e -> performSearch());

        JButton backButton = new JButton("← Quay lại");
        backButton.addActionListener(e -> onBack.run());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(backButton);
        leftPanel.add(Box.createHorizontalStrut(10));
        leftPanel.add(searchField);
        leftPanel.add(Box.createHorizontalStrut(5));
        leftPanel.add(searchButton);

        searchBarPanel.add(leftPanel, BorderLayout.WEST);
        add(searchBarPanel, BorderLayout.NORTH);

        // Results area
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(25);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setSearchQuery(String query) {
        searchField.setText(query);
    }

    void performSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Hiển thị trạng thái loading
        resultsPanel.removeAll();
        resultsPanel.add(new JLabel("Đang tìm kiếm...", SwingConstants.CENTER));
        resultsPanel.revalidate();
        resultsPanel.repaint();

        new SwingWorker<List<Product>, Void>() {
            @Override
            protected List<Product> doInBackground() throws Exception {
                try {
                    List<Product> allProducts = ProductDAO.getAll();
                    if (allProducts == null) {
                        return Collections.emptyList();
                    }

                    return allProducts.stream()
                            .filter(p -> p != null && p.getName() != null)
                            .filter(p -> p.getName().toLowerCase().contains(query.toLowerCase()))
                            .collect(Collectors.toList());
                } catch (Exception e) {
                    e.printStackTrace();
                    return Collections.emptyList();
                }
            }

            @Override
            protected void done() {
                try {
                    List<Product> results = get();
                    displaySearchResults(results);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    displayErrorMessage();
                }
            }
        }.execute();
    }

    private void displaySearchResults(List<Product> results) {
        resultsPanel.removeAll();

        if (results == null || results.isEmpty()) {
            resultsPanel.add(new JLabel("Không tìm thấy sản phẩm nào phù hợp", SwingConstants.CENTER));
        } else {
            JPanel gridPanel = new JPanel(new GridLayout(0, 4, 10, 10));
            gridPanel.setBackground(Color.WHITE);
            gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            for (Product p : results) {
                if (p != null) {
                    gridPanel.add(createProductCard(p));
                }
            }

            // Thêm padding nếu cần
            while (gridPanel.getComponentCount() % 4 != 0) {
                gridPanel.add(Box.createHorizontalStrut(150));
            }

            resultsPanel.add(new JScrollPane(gridPanel));
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private void displayErrorMessage() {
        resultsPanel.removeAll();
        resultsPanel.add(new JLabel("Đã xảy ra lỗi khi tìm kiếm. Vui lòng thử lại!", SwingConstants.CENTER));
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private JPanel createProductCard(Product p) {
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

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resultsPanel.removeAll();
                resultsPanel.add(new ProductDetailPanel(p, favoriteList, () -> {
                    resultsPanel.removeAll();
                    resultsPanel.add(new SearchPanel(onBack, favoriteList));
                    resultsPanel.revalidate();
                    resultsPanel.repaint();
                }));
                resultsPanel.revalidate();
                resultsPanel.repaint();
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
}
