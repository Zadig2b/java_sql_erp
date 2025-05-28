package com.erp.dao;

import com.erp.db.DatabaseManager;
import com.erp.model.Product;
import com.erp.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

public static List<Category> getCategories() {
    List<Category> categories = new ArrayList<>();
    String sql = "SELECT category, categoryname FROM categories ORDER BY categoryname";

    try (Connection conn = DatabaseManager.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        categories.add(new Category(-1, "Toutes"));
        while (rs.next()) {
            categories.add(new Category(
                rs.getInt("category"),           // id
                rs.getString("categoryname")     // nom lisible
            ));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return categories;
}


    public static List<Product> getProductsByCategoryId(int categoryId) {
        List<Product> products = new ArrayList<>();

        String sql = (categoryId == -1)
                ? "SELECT p.prod_id, p.title, p.price, c.categoryname AS category " +
                        "FROM products p JOIN categories c ON p.category = c.category"
                : "SELECT p.prod_id, p.title, p.price, c.categoryname AS category " +
                        "FROM products p JOIN categories c ON p.category = c.category WHERE c.category = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (categoryId != -1) {
                stmt.setInt(1, categoryId);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new Product(
                            rs.getInt("prod_id"),
                            rs.getString("title"),
                            rs.getDouble("price"),
                            rs.getString("category") // alias de categoryname
                    ));

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }
}
