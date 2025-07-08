package main.view;

import main.model.Cart;
import main.model.CartItem;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import main.model.Order;

public class InvoicePanel extends JPanel {
    public InvoicePanel(Order order) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("HÓA ĐƠN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.setBackground(Color.WHITE);

        infoPanel.add(new JLabel("Mã đơn hàng: " + order.getId()));
        infoPanel.add(new JLabel("Người đặt: " + order.getUsername()));
        infoPanel.add(new JLabel("Ngày đặt: " + order.getDate().toString()));
        infoPanel.add(new JLabel("Tổng tiền: " + String.format("%,.0f đ", order.getTotal())));
        infoPanel.add(new JLabel(" "));

        for (CartItem item : order.getItems()) {
            String line = String.format("- %s | Size: %s | SL: %d | Giá: %,.0f đ",
                    item.getProduct().getName(),
                    item.getSize(),
                    item.getQuantity(),
                    item.getProduct().getPrice());
            infoPanel.add(new JLabel(line));
        }

        add(infoPanel, BorderLayout.CENTER);
    }
}
