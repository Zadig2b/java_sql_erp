package com.erp;

import com.erp.db.DatabaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestConnexion {
    public static void main(String[] args) {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customers LIMIT 5")) {

            while (rs.next()) {
                System.out.println(rs.getString("firstname") + " " + rs.getString("lastname"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
