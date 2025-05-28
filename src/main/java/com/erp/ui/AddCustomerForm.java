package com.erp.ui;

import com.erp.db.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.sql.CallableStatement;
import java.sql.Connection;

public class AddCustomerForm extends JFrame {
    private JTextField firstNameField, lastNameField, cityField, emailField;

    public AddCustomerForm() {
        setTitle("Ajouter un client");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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

        setVisible(true);
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
                        "{ ? = call new_customer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }")) {

            // OUT paramètre
            stmt.registerOutParameter(1, java.sql.Types.INTEGER);

            // IN paramètres requis par la fonction
            stmt.setString(2, firstName); // firstname_in
            stmt.setString(3, lastName); // lastname_in
            stmt.setString(4, "Adresse 1"); // address1_in
            stmt.setString(5, "Adresse 2"); // address2_in
            stmt.setString(6, city); // city_in
            stmt.setString(7, "Région"); // state_in
            stmt.setInt(8, 75000); // zip_in
            stmt.setString(9, "France"); // country_in
            stmt.setInt(10, 1); // region_in
            stmt.setString(11, email); // email_in
            stmt.setString(12, "0102030405"); // phone_in
            stmt.setInt(13, 1); // creditcardtype_in
            stmt.setString(14, "0000-0000-0000-0000"); // creditcard_in
            stmt.setString(15, "12/30"); // creditcardexpiration_in
            stmt.setString(16, "user_" + System.currentTimeMillis()); // username_in
            stmt.setString(17, "password123"); // password_in
            stmt.setInt(18, 30); // age_in
            stmt.setInt(19, 30000); // income_in
            stmt.setString(20, "M"); // gender_in

            stmt.execute();

            int newId = stmt.getInt(1);
            if (newId == 0) {
                JOptionPane.showMessageDialog(this, "Nom d'utilisateur déjà existant", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Client ajouté avec succès (ID = " + newId + ")");
                dispose();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du client.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddCustomerForm::new);
    }
}
