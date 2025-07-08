package main.view;

import main.model.*;
import main.utils.OrderUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CartPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private User currentUser;
    private Runnable onBack;

    public CartPanel(User currentUser, Runnable onBack) {
        this.currentUser = currentUser;
        this.onBack = onBack;
        setLayout(new BorderLayout());
        initUI();
        loadData();
    }

    private void initUI() {
        tableModel = new DefaultTableModel(new Object[]{"Tên", "Size", "Giá", "Số lượng"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        totalLabel = new JLabel("Tổng: 0 đ");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(totalLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteBtn = new JButton("Xóa sản phẩm");
        JButton checkoutBtn = new JButton("Thanh toán");

        deleteBtn.addActionListener(e -> removeSelected());
        checkoutBtn.addActionListener(e -> handleCheckout());

        buttonPanel.add(deleteBtn);
        buttonPanel.add(checkoutBtn);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
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
        List<CartItem> items = Cart.getInstance().getItems();
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận thanh toán?", "Thanh toán", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Tính tổng tiền thủ công
            double total = 0;
            for (CartItem item : items) {
                total += item.getProduct().getPrice() * item.getQuantity();
            }

            // Tạo đơn hàng
            String id = UUID.randomUUID().toString();
            LocalDateTime now = LocalDateTime.now();
            Order order = new Order(id, currentUser.getUsername(), items, total, now);

            // Lưu đơn hàng vào file
            OrderUtil.saveOrder(order);

            // Xóa giỏ hàng và cập nhật lại bảng
            Cart.getInstance().clear();
            loadData();

            // Hiển thị hóa đơn
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Hóa đơn");
                frame.setContentPane(new InvoicePanel(order));
                frame.setSize(500, 600);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });
        }
    }
}
