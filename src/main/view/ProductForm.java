package main.view;

import main.dao.ProductDAO;
import main.model.Product;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ProductForm extends JDialog {
    private JTextField txtId, txtName, txtImagePath, txtPrice, txtQuantity;
    private JComboBox<String> cmbCategory;
    private JTextArea descriptionArea;
    private JButton btnSave, btnCancel, btnBrowse;
    private Product product;

    public ProductForm(Frame owner, Product product) {
        super(owner, true);
        this.product = product;

        setTitle(product == null ? "Thêm sản phẩm" : "Sửa sản phẩm");
        setSize(450, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField();
        panel.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Tên sản phẩm:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField();
        panel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Giá:"), gbc);
        gbc.gridx = 1;
        txtPrice = new JTextField();
        panel.add(txtPrice, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Số lượng:"), gbc);
        gbc.gridx = 1;
        txtQuantity = new JTextField();
        panel.add(txtQuantity, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Ảnh:"), gbc);
        gbc.gridx = 1;
        JPanel imagePanel = new JPanel(new BorderLayout());
        txtImagePath = new JTextField();
        btnBrowse = new JButton("Chọn...");
        imagePanel.add(txtImagePath, BorderLayout.CENTER);
        imagePanel.add(btnBrowse, BorderLayout.EAST);
        panel.add(imagePanel, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Danh mục:"), gbc);
        gbc.gridx = 1;
        cmbCategory = new JComboBox<>(new String[]{"NỮ", "NAM", "TRẺ EM"});
        panel.add(cmbCategory, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1;
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        panel.add(scrollPane, gbc);

        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        // Gán dữ liệu nếu đang sửa
        if (product != null) {
            txtId.setText(product.getId());
            txtId.setEnabled(false);
            txtName.setText(product.getName());
            txtPrice.setText(String.valueOf(product.getPrice()));
            txtQuantity.setText(String.valueOf(product.getQuantity()));
            txtImagePath.setText(product.getImagePath());
            cmbCategory.setSelectedItem(product.getCategory());
            descriptionArea.setText(product.getDescription());
        }

        // Chọn ảnh
        btnBrowse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                txtImagePath.setText(selectedFile.getAbsolutePath());
            }
        });

        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> dispose());
    }

    private void onSave() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String imagePath = txtImagePath.getText().trim();
        String category = cmbCategory.getSelectedItem().toString();
        String description = descriptionArea.getText().trim();
        double price;
        int quantity;

        try {
            price = Double.parseDouble(txtPrice.getText().trim());
            quantity = Integer.parseInt(txtQuantity.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá hoặc số lượng không hợp lệ.");
            return;
        }

        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        if (product == null) {
            if (ProductDAO.getAll().stream().anyMatch(p -> p.getId().equals(id))) {
                JOptionPane.showMessageDialog(this, "ID đã tồn tại.");
                return;
            }
            Product newProduct = new Product(id, name, price, quantity, imagePath, category, description);
            ProductDAO.add(newProduct);
        } else {
            product.setName(name);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setImagePath(imagePath);
            product.setCategory(category);
            product.setDescription(description);
            ProductDAO.update(product);
        }

        dispose();
    }
}
