package com.erp.ui;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Mini ERP - Tableau de bord");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Onglets
        JTabbedPane tabs = new JTabbedPane();

        // Vue Clients
        tabs.addTab("👥 Clients", new CustomerPanel());
        tabs.addTab("➕ Ajouter Client", new AddCustomerForm());

        // Vue Produits
        tabs.addTab("📦 Produits", new ProductPanel());

        // Commandes
        tabs.addTab("🛒 Créer Commande", new OrderForm());
        tabs.addTab("📜 Historique", new OrderHistoryPanel());

        // Stock
        tabs.addTab("⚠️ Stock Faible", new InventoryPanel());

        add(tabs);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
