/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.makram.utlookup.database;

import java.sql.*;

/**
 *
 * @author admin
 */
public class Main {
    public static void main(String[] args) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = c.createStatement();
            String sql = "create table company " + 
                    "(_id integer primary key, " + 
                    "name text not null," +
                    "age integer not null," + 
                    "address text," +
                    "salary real);";
            int update = stmt.executeUpdate(sql);
            System.out.println("Update value: " + update);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(1);
        }
        System.out.println("Created database successfully");
    }
}
