package com.erp.dao;

import com.erp.db.DatabaseManager;
import com.erp.model.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT customerid, firstname, lastname, city, email FROM customers")) {

            while (rs.next()) {
                Customer c = new Customer(
                        rs.getInt("customerid"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("city"),
                        rs.getString("email")
                );
                customers.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }
}
