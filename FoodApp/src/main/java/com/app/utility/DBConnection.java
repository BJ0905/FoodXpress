package com.app.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

public class DBConnection {

    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    static {
        try {
            Properties prop = new Properties();

            InputStream input = DBConnection.class
                    .getClassLoader()
                    .getResourceAsStream("app.properties");

            if (input == null) {
                throw new RuntimeException("❌ app.properties NOT found in classpath");
            }

            prop.load(input);

            URL = prop.getProperty("db.url");
            USERNAME = prop.getProperty("db.username");
            PASSWORD = prop.getProperty("db.password");

            if (URL == null || USERNAME == null || PASSWORD == null) {
                throw new RuntimeException("❌ Database properties missing in app.properties");
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver Loaded");
            System.out.println("✅ DB Properties Loaded");

        } catch (Exception e) {
            System.err.println("❌ Failed to initialize DBConnection");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✅ Database Connected");
            return connection;
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed");
            e.printStackTrace();
            return null;
        }
    }
}