package com.physicalmed.physicalmedmanagement;

import org.postgresql.Driver;

import java.sql.*;

public class DbFunctions {

    //teste

    public Connection connect_to_db(String dbname, String user, String password){
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname, user, password);
            if (conn!=null){
                System.out.println("Conexão estabelecida com sucesso!");
            }
            else {
                System.out.println("Erro na conexão!");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return conn;
    }

    public boolean validateLogin(String username, String password){
        String querry = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = connect_to_db("physical_med_DB", "postgres", "8870");
        PreparedStatement pstmt = conn.prepareStatement(querry)){

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
