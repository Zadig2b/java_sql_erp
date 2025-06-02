package com.erp.ui;

import com.erp.dao.InventoryDAO;
import com.erp.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public InventoryPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Nom", "Prix", "Catégorie"});
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadLowStock(10); // seuil par défaut
    }

    private void loadLowStock(int seuil) {
        List<Product> produits = InventoryDAO.getLowStockProducts(seuil);
        model.setRowCount(0);
        for (Product p : produits) {
            model.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getCategory()});
        }
    }
}
