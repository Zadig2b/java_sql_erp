package com.erp.ui;

import com.erp.dao.CustomerDAO;
import com.erp.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public CustomerPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[] {"ID", "Prénom", "Nom", "Ville", "Email"});
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton loadButton = new JButton("Charger les clients");
        loadButton.addActionListener(e -> loadCustomers());
        add(loadButton, BorderLayout.SOUTH);
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
}
