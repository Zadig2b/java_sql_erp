package com.erp.ui;

import com.erp.db.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.sql.CallableStatement;
import java.sql.Connection;

public class AddCustomerForm extends JPanel {
    private JTextField firstNameField, lastNameField, cityField, emailField;

    public AddCustomerForm() {
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Prénom:"));
        firstNameField = new JTextField();
        add(firstNameField);

        add(new JLabel("Nom:"));
        lastNameField = new JTextField();
        add(lastNameField);

        add(new JLabel("Ville:"));
        cityField = new JTextField();
        add(cityField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(e -> addCustomer());
        add(new JLabel()); // Espace vide
        add(addButton);
    }

    private void addCustomer() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String city = cityField.getText().trim();
        String email = emailField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || city.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseManager.getConnection();
             CallableStatement stmt = conn.prepareCall(
                     "{ ? = call new_customer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }")
        ) {
            stmt.registerOutParameter(1, java.sql.Types.INTEGER);

            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, "Adresse 1");
            stmt.setString(5, "Adresse 2");
            stmt.setString(6, city);
            stmt.setString(7, "Région");
            stmt.setInt(8, 75000);
            stmt.setString(9, "France");
            stmt.setInt(10, 1);
            stmt.setString(11, email);
            stmt.setString(12, "0102030405");
            stmt.setInt(13, 1);
            stmt.setString(14, "0000-0000-0000-0000");
            stmt.setString(15, "12/30");
            stmt.setString(16, "user_" + System.currentTimeMillis());
            stmt.setString(17, "password123");
            stmt.setInt(18, 30);
            stmt.setInt(19, 30000);
            stmt.setString(20, "M");

            stmt.execute();
            int newId = stmt.getInt(1);

            if (newId == 0) {
                JOptionPane.showMessageDialog(this, "Nom d'utilisateur déjà existant", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Client ajouté avec succès (ID = " + newId + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du client.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
