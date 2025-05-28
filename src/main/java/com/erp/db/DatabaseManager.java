package com.erp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseManager {
    private static Connection connection;

    /**
     * Charge la configuration JDBC depuis le fichier db.properties.
     */
    private static Properties loadProperties() throws Exception {
        Properties props = new Properties();
        try (InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new Exception("Fichier db.properties introuvable");
            }
            props.load(input);
        }
        return props;
    }

    /**
     * Établit une connexion JDBC vers PostgreSQL.
     */
    public static Connection getConnection() throws Exception {
        if (connection == null || connection.isClosed()) {
            Properties props = loadProperties();
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion établie avec succès !");
        }
        return connection;
    }
}
