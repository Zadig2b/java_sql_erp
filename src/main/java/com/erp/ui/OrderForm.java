package com.erp.ui;

import com.erp.db.DatabaseManager;
import com.erp.model.Customer;
import com.erp.model.Product;
import com.erp.dao.CustomerDAO;
import com.erp.dao.ProductDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class OrderForm extends JFrame {
    private JComboBox<Customer> customerComboBox;
    private JList<Product> productList;
    private JButton submitButton;

    public OrderForm() {
        setTitle("Créer une commande");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sélection du client
        customerComboBox = new JComboBox<>();
        List<Customer> customers = CustomerDAO.getAllCustomers();
        for (Customer c : customers) {
            customerComboBox.addItem(c);
        }

        // Sélection des produits
        List<Product> products = ProductDAO.getProductsByCategoryId(-1);
        productList = new JList<>(products.toArray(new Product[0]));
        productList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Bouton
        submitButton = new JButton("Valider la commande");
        submitButton.addActionListener(e -> submitOrder());

        // Organisation
        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Client :"), BorderLayout.WEST);
        top.add(customerComboBox, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(productList), BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void submitOrder() {
        Customer customer = (Customer) customerComboBox.getSelectedItem();
        List<Product> selectedProducts = productList.getSelectedValuesList();

        if (customer == null || selectedProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client et au moins un produit.");
            return;
        }

        double net = selectedProducts.stream().mapToDouble(Product::getPrice).sum();
        double tax = net * 0.2;
        double total = net + tax;

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Insertion dans orders
            PreparedStatement insertOrder = conn.prepareStatement(
                    "INSERT INTO orders (customerid, orderdate, netamount, tax, totalamount) VALUES (?, CURRENT_DATE, ?, ?, ?) RETURNING orderid");
            insertOrder.setInt(1, customer.getId());
            insertOrder.setDouble(2, net);
            insertOrder.setDouble(3, tax);
            insertOrder.setDouble(4, total);

            ResultSet rs = insertOrder.executeQuery();
            int orderId = -1;
            if (rs.next()) {
                orderId = rs.getInt("orderid");
            } else {
                throw new SQLException("Échec de la création de la commande.");
            }

            // 2. Insertion dans orderlines
            PreparedStatement insertLine = conn.prepareStatement(
                    "INSERT INTO orderlines (orderid, prod_id, quantity, orderdate) VALUES (?, ?, ?, CURRENT_DATE)");

            for (Product p : selectedProducts) {
                insertLine.setInt(1, orderId);
                insertLine.setInt(2, p.getId());
                insertLine.setInt(3, 1); // quantité fixe pour l'instant
                insertLine.addBatch();
            }

            insertLine.executeBatch();

            // 3. Mise à jour du stock
            PreparedStatement updateStock = conn.prepareStatement(
                    "UPDATE inventory SET quan_in_stock = quan_in_stock - ? WHERE prod_id = ?");

            for (Product p : selectedProducts) {
                updateStock.setInt(1, 1); // quantité commandée
                updateStock.setInt(2, p.getId());
                updateStock.addBatch();
            }
            updateStock.executeBatch();

            conn.commit();

            JOptionPane.showMessageDialog(this, "Commande créée avec succès (ID: " + orderId + ")");
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la création de la commande.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OrderForm::new);
    }
}
