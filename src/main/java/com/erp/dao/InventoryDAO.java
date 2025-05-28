package com.erp.dao;

import com.erp.db.DatabaseManager;
import com.erp.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {
    public static List<Product> getLowStockProducts(int seuil) {
        List<Product> lowStock = new ArrayList<>();

        String sql = """
            SELECT p.prod_id, p.title, p.price, c.categoryname AS category
            FROM products p
            JOIN categories c ON p.category = c.category
            JOIN inventory i ON p.prod_id = i.prod_id
            WHERE i.quan_in_stock < ?
        """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seuil);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lowStock.add(new Product(
                    rs.getInt("prod_id"),
                    rs.getString("title"),
                    rs.getDouble("price"),
                    rs.getString("category")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lowStock;
    }
}
