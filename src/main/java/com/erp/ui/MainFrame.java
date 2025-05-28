package com.erp.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Mini ERP - Tableau de bord");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Onglets
        JTabbedPane tabs = new JTabbedPane();

        // Vue Clients
        tabs.addTab("👥 Clients", createInternalFrame(new CustomerPanel()));
        tabs.addTab("➕ Ajouter Client", createInternalFrame(new AddCustomerForm()));

        // Vue Produits
        tabs.addTab("📦 Produits", createInternalFrame(new ProductPanel()));

        // Commandes
        tabs.addTab("🛒 Créer Commande", createInternalFrame(new OrderForm()));
        tabs.addTab("📜 Historique", createInternalFrame(new OrderHistoryPanel()));

        // Stock
        tabs.addTab("⚠️ Stock Faible", createInternalFrame(new InventoryPanel()));

        add(tabs);

        setVisible(true);
    }

    // Permet d'intégrer n'importe quelle JFrame dans un onglet
    private JPanel createInternalFrame(JFrame frame) {
        JPanel panel = new JPanel(new BorderLayout());
        for (Component c : frame.getContentPane().getComponents()) {
            panel.add(c);
        }
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
