package com.physicalmed.physicalmedmanagement;

import org.postgresql.Driver;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public Product getProductById(int productId){
        String query = "SELECT * FROM product WHERE product_id = ?";

        try (Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)){

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()){
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setProductName(rs.getString("product_name"));
                p.setCost(rs.getBigDecimal("cost"));
                p.setPixPrice(rs.getBigDecimal("pix_price"));
                p.setCreditPrice(rs.getBigDecimal("credit_price"));
                p.setPixPriceDiscount(rs.getBigDecimal("min_pix_price"));
                p.setCreditPriceDiscount(rs.getBigDecimal("min_credit_price"));
                p.setStock(rs.getInt("stock"));
                p.setProductImage(rs.getBytes("product_image"));

                return p;
            }

        }
        catch (SQLException e){

        }
        return null;
    }

    public List<Product> getAllProducts(){
        List<Product> products = new ArrayList<>();
        String query = "SELECT product_id, product_name, cost, pix_price, credit_price, min_pix_price, min_credit_price, stock, product_image FROM product";


        try (Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery()){

            while (rs.next()){
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setProductName(rs.getString("product_name"));
                p.setCost(rs.getBigDecimal("cost"));
                p.setPixPrice(rs.getBigDecimal("pix_price"));
                p.setCreditPrice(rs.getBigDecimal("credit_price"));
                p.setPixPriceDiscount(rs.getBigDecimal("min_pix_price"));
                p.setCreditPriceDiscount(rs.getBigDecimal("min_credit_price"));
                p.setStock(rs.getInt("stock"));
                p.setProductImage(rs.getBytes("product_image"));
                products.add(p);
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return products;
    }

    public void saveProduct(String name, BigDecimal cost, BigDecimal pixPrice, BigDecimal cardPrice, BigDecimal minPixPrice,
                            BigDecimal minCardPrice, int stock, byte[] imageBytes) throws SQLException{
        String query = "INSERT INTO product (product_name, cost, pix_price, credit_price, min_pix_price, min_credit_price, stock, product_image)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)){

            pstmt.setString(1, name);
            pstmt.setBigDecimal(2, cost);
            pstmt.setBigDecimal(3, pixPrice);
            pstmt.setBigDecimal(4, cardPrice);
            pstmt.setBigDecimal(5, minPixPrice);
            pstmt.setBigDecimal(6, minCardPrice);
            pstmt.setInt(7, stock);
            pstmt.setBytes(8, imageBytes);

            pstmt.executeUpdate();
            System.out.println("Produto Salvo no banco de dados com sucesso!");
        }

    }

    public void updateProduct(int productId, String name, BigDecimal cost, BigDecimal pixPrice, BigDecimal cardPrice, BigDecimal minPixPrice,
                              BigDecimal minCardPrice, int stock, byte[] imageBytes) throws SQLException{
        String query = "UPDATE product SET product_name = ?, cost = ?, pix_price = ?, credit_price = ?, min_pix_price = ?, " +
                "min_credit_price = ?, stock = ?, product_image = ? WHERE product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)){

            pstmt.setString(1, name);
            pstmt.setBigDecimal(2, cost);
            pstmt.setBigDecimal(3, pixPrice);
            pstmt.setBigDecimal(4, cardPrice);
            pstmt.setBigDecimal(5, minPixPrice);
            pstmt.setBigDecimal(6, minCardPrice);
            pstmt.setInt(7, stock);
            pstmt.setBytes(8, imageBytes);
            pstmt.setInt(9, productId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("Produto atualizado com sucesso!");
            }
            else{
                System.out.println("Ocorrey algum erro ao atualizar o produto!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteProduct(int productId){
        String query = "DELETE FROM product WHERE product_id = ?";

        try (Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)){

            pstmt.setInt(1, productId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0){
                System.out.println("Produto Excluido com sucesso");
            }
            else {
                System.out.println("Nenhum Produto foi excluido!");
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

}
