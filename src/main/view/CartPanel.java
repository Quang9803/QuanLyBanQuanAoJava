package main.view;

import main.model.Cart;
import main.model.CartItem;
import main.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CartPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;

    public CartPanel() {
        setLayout(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new Object[]{"Tên", "Size", "Giá", "Số lượng"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel dưới cùng
        JPanel bottomPanel = new JPanel(new BorderLayout());

        totalLabel = new JLabel("Tổng: 0 đ");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(totalLabel, BorderLayout.WEST);

        // Nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteBtn = new JButton("Xóa sản phẩm");
        JButton checkoutBtn = new JButton("Thanh toán");

        deleteBtn.addActionListener(e -> removeSelected());
        checkoutBtn.addActionListener(e -> handleCheckout());

        buttonPanel.add(deleteBtn);
        buttonPanel.add(checkoutBtn);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        loadData();
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<CartItem> items = Cart.getInstance().getItems();
        double total = 0;

        for (CartItem item : items) {
            Product p = item.getProduct();
            tableModel.addRow(new Object[]{
                p.getName(),
                item.getSize(),
                String.format("%,.0f đ", p.getPrice()),
                item.getQuantity()
            });
            total += p.getPrice() * item.getQuantity();
        }

        totalLabel.setText("Tổng: " + String.format("%,.0f đ", total));
    }

    private void removeSelected() {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            String name = (String) tableModel.getValueAt(selected, 0);
            String size = (String) tableModel.getValueAt(selected, 1);
            Cart.getInstance().removeByNameAndSize(name, size);
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xóa.");
        }
    }

    private void handleCheckout() {
        if (Cart.getInstance().getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận thanh toán?", "Thanh toán", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Cart.getInstance().clear();
            loadData();
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
        }
    }
}
