package com.erp.ui;

import com.erp.dao.ProductDAO;
import com.erp.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductPanel extends JFrame {
    private JComboBox<String> categoryComboBox;
    private JTable table;
    private DefaultTableModel model;

    public ProductPanel() {
        setTitle("Liste des Produits");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // --- TOP : combo catégories ---
        JPanel topPanel = new JPanel();
        categoryComboBox = new JComboBox<>();
        categoryComboBox.addActionListener(e -> loadProducts());
        topPanel.add(new JLabel("Catégorie :"));
        topPanel.add(categoryComboBox);
        add(topPanel, BorderLayout.NORTH);

        // --- CENTER : tableau produits ---
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Nom", "Prix", "Catégorie"});
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Load initial ---
        loadCategories();

        setVisible(true);
    }

    private void loadCategories() {
        List<String> categories = ProductDAO.getCategories();
        for (String c : categories) {
            categoryComboBox.addItem(c);
        }
        categoryComboBox.setSelectedIndex(0);
        loadProducts();
    }

    private void loadProducts() {
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        List<Product> products = ProductDAO.getProductsByCategory(selectedCategory);
        model.setRowCount(0);
        for (Product p : products) {
            model.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getCategory()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProductPanel::new);
    }
}
