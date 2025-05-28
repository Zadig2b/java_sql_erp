package com.erp.dao;

import com.erp.db.DatabaseManager;
import com.erp.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public static List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM products ORDER BY category";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            categories.add("Toutes");
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }

    public static List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        String sql = category.equals("Toutes")
                ? "SELECT prod_id, title, price, category FROM products"
                : "SELECT prod_id, title, price, category FROM products WHERE category = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (!category.equals("Toutes")) {
                stmt.setString(1, category);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new Product(
                        rs.getInt("prod_id"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("category")
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }
}
