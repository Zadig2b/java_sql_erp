package com.erp.ui;

import com.erp.dao.CustomerDAO;
import com.erp.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerPanel extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public CustomerPanel() {
        setTitle("Liste des Clients");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[] {"ID", "Prénom", "Nom", "Ville", "Email"});
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton loadButton = new JButton("Charger les clients");
        loadButton.addActionListener(e -> loadCustomers());
        add(loadButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadCustomers() {
        List<Customer> customers = CustomerDAO.getAllCustomers();
        model.setRowCount(0); // Vide l'ancien contenu
        for (Customer c : customers) {
            model.addRow(new Object[] {
                c.getId(), c.getFirstName(), c.getLastName(), c.getCity(), c.getEmail()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustomerPanel::new);
    }
}
