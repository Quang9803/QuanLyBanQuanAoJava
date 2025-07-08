package main.view;

import main.dao.ProductDAO;
import main.model.Product;
import main.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private User currentUser;

public MainFrame(User user) {
        this.currentUser = user;
        setTitle("Trang quản trị - " + user.getUsername());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        loadTableData();
        setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Chỉ tạo menu nếu là admin
        if ("admin".equalsIgnoreCase(currentUser.getRole())) {
            JMenuBar menuBar = new JMenuBar();
            JMenu accountMenu = new JMenu("Tài khoản");
            JMenuItem logoutItem = new JMenuItem("Đăng xuất");
            logoutItem.addActionListener(e -> {
                dispose();
                new LoginFrame();
            });
            accountMenu.add(logoutItem);
            menuBar.add(accountMenu);
            setJMenuBar(menuBar);
        }

        // Bảng chỉ hiện Tên, Giá, Ảnh
        tableModel = new DefaultTableModel(new Object[]{"Tên", "Giá", "Ảnh"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Icon.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(60);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Nếu là admin thì hiện panel quản lý
        if ("admin".equalsIgnoreCase(currentUser.getRole())) {
            JPanel buttonPanel = new JPanel();
            JButton btnAdd = new JButton("Thêm");
            JButton btnEdit = new JButton("Sửa");
            JButton btnDelete = new JButton("Xóa");

            buttonPanel.add(btnAdd);
            buttonPanel.add(btnEdit);
            buttonPanel.add(btnDelete);
            add(buttonPanel, BorderLayout.SOUTH);

            btnAdd.addActionListener(e -> addProduct());
            btnEdit.addActionListener(e -> editProduct());
            btnDelete.addActionListener(e -> deleteProduct());
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Product> list = ProductDAO.getAll();
        if (list != null) {
            for (Product p : list) {
                ImageIcon icon = null;
                if (p.getImagePath() != null && !p.getImagePath().isEmpty()) {
                    try {
                        Image img = new ImageIcon(p.getImagePath()).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                        icon = new ImageIcon(img);
                    } catch (Exception e) {
                        icon = null;
                    }
                }
                tableModel.addRow(new Object[]{p.getName(), p.getPrice(), icon});
            }
        }
    }

    private void addProduct() {
        ProductForm form = new ProductForm(this, null);
        form.setVisible(true);
        loadTableData();
    }

    private void editProduct() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm để sửa.");
        return;
    }

    List<Product> list = ProductDAO.getAll();
    Product selectedProduct = list.get(selectedRow);

    ProductForm form = new ProductForm(this, selectedProduct);
    form.setVisible(true);
    loadTableData();
}


    private void deleteProduct() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm để xóa.");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        List<Product> list = ProductDAO.getAll();
        Product selectedProduct = list.get(selectedRow);
        ProductDAO.delete(selectedProduct.getId());
        loadTableData();
    }
}

}
