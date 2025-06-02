package com.erp.ui;

import com.erp.dao.CustomerDAO;
import com.erp.db.DatabaseManager;
import com.erp.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class OrderHistoryPanel extends JPanel {
    private JComboBox<Customer> customerComboBox;
    private JTable table;
    private DefaultTableModel model;

    public OrderHistoryPanel() {
        setLayout(new BorderLayout());

        // Combo clients
        customerComboBox = new JComboBox<>();
        List<Customer> customers = CustomerDAO.getAllCustomers();
        for (Customer c : customers) {
            customerComboBox.addItem(c);
        }
        customerComboBox.addActionListener(e -> loadOrders());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Client :"));
        topPanel.add(customerComboBox);
        add(topPanel, BorderLayout.NORTH);

        // Tableau commandes
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Commande", "Date", "Produit", "Quantité", "Prix", "Montant total"});
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadOrders(); // initial
    }

    private void loadOrders() {
        model.setRowCount(0);
        Customer customer = (Customer) customerComboBox.getSelectedItem();
        if (customer == null) return;

        String sql = """
            SELECT o.orderid, o.orderdate, p.title AS product, ol.quantity, p.price, (ol.quantity * p.price) AS total
            FROM orders o
            JOIN orderlines ol ON o.orderid = ol.orderid
            JOIN products p ON ol.prod_id = p.prod_id
            WHERE o.customerid = ?
            ORDER BY o.orderdate DESC
        """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customer.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("orderid"),
                    rs.getDate("orderdate"),
                    rs.getString("product"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getDouble("total")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
