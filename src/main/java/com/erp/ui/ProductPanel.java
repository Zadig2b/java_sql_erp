package com.erp.ui;

import com.erp.dao.ProductDAO;
import com.erp.model.Product;
import com.erp.model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductPanel extends JPanel {
    private JComboBox<Category> categoryComboBox;
    private JTable table;
    private DefaultTableModel model;

    public ProductPanel() {
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
    }

    private void loadCategories() {
        List<Category> categories = ProductDAO.getCategories();
        for (Category c : categories) {
            categoryComboBox.addItem(c);
        }
        categoryComboBox.setSelectedIndex(0);
        loadProducts();
    }

    private void loadProducts() {
        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
        List<Product> products = ProductDAO.getProductsByCategoryId(selectedCategory.getId());

        model.setRowCount(0);
        for (Product p : products) {
            model.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getCategory()});
        }
    }
}
