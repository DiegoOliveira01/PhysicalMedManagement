package com.physicalmed.physicalmedmanagement;

import org.postgresql.Driver;

import java.sql.*;

public class DbFunctions {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/physical_med_DB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "8870";

    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    public boolean validateLogin(String login, String password){
        System.out.println("Validando Login, login: " + login + " Password: " + password);
        String query = "SELECT * FROM users WHERE login = ? AND password = ?";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)){

            pstmt.setString(1, login);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()){
                return rs.next();
            }

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void getUserData(String login){
        String query = "SELECT login, username, role FROM users WHERE login = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)){

            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                UserSession.getInstance().setUser(rs.getString("login"), rs.getString("username"), rs.getString("role"));
                System.out.println("Sessão iniciada para: " + login);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
