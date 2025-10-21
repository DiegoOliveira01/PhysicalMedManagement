package com.physicalmed.physicalmedmanagement;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * Classe responsável por realizar operações de acesso ao banco de dados PostgreSQL.
 * Contém métodos para manipulação de usuários, produtos e formas de pagamento.
 *
 * <p>Essa classe atua como camada DAO (Data Access Object), centralizando
 * a lógica de persistência e consulta de dados no banco.</p>
 *
 */

public class DbFunctions {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/physical_med_DB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "8870";

    /**
     * Obtém uma conexão com o banco de dados.
     *
     * @return objeto {@link Connection} ativo
     * @throws SQLException caso ocorra falha na conexão
     */

    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    /**
     * Valida as credenciais de login de um usuário.
     *
     * @param login    login do usuário
     * @param password senha do usuário
     * @return {@code true} se o usuário existir e as credenciais forem válidas, caso contrário {@code false}
     */

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

    /**
     * Recupera os dados do usuário (login, username e role) e inicia a sessão.
     *
     * @param login login do usuário
     */

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

    /**
     * Busca um produto pelo seu ID.
     *
     * @param productId identificador do produto
     * @return instância de {@link Product} preenchida ou {@code null} se não encontrado
     */

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

    /**
     * Lista todos os produtos cadastrados no banco.
     *
     * @return lista de {@link Product}
     */

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

    /**
     * Lista todas as formas de pagamento do tipo "single" (sem parcelamento).
     *
     * @return lista de {@link PaymentSingle}
     */

    public List<PaymentSingle> getAllSinglePayments(){
        List<PaymentSingle> singlePayments = new ArrayList<>();
        String query = "SELECT payment_id, payment_method, taxes FROM payment WHERE installments = 0";

        try (Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery()){

            while (rs.next()) {
                PaymentSingle payment = new PaymentSingle(
                    rs.getInt("payment_id"),
                    rs.getString("payment_method"),
                    rs.getBigDecimal("taxes")
                );
                singlePayments.add(payment);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return singlePayments;
    }

    /**
     * Lista todas as formas de pagamento do tipo "multi" (com parcelamento).
     * Os resultados são agrupados por método de pagamento.
     *
     * @return lista de {@link PaymentMulti}
     */

    public List<PaymentMulti> getAllMultiPayments() {
        Map<String, PaymentMulti> paymentMap = new HashMap<>(); // key: payment_method (trimmed, kept original case)
        String query = "SELECT payment_id, payment_method, installments, taxes " +
                "FROM payment WHERE installments > 0 " +
                "ORDER BY payment_method, installments";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int paymentId = rs.getInt("payment_id");
                String paymentName = rs.getString("payment_method").trim();
                int installment = rs.getInt("installments");
                BigDecimal tax = rs.getBigDecimal("taxes");

                // Use payment_method como chave para agrupar todas as parcelas do mesmo metodo
                PaymentMulti payment = paymentMap.get(paymentName);
                if (payment == null) {
                    payment = new PaymentMulti(paymentId, paymentName);
                    paymentMap.put(paymentName, payment);
                }

                payment.addInstallmentTax(installment, tax);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(paymentMap.values());
    }

    /**
     * Busca uma forma de pagamento "single" pelo nome.
     *
     * @param paymentName nome do método de pagamento
     * @return instância de {@link PaymentSingle} ou {@code null} se não encontrada
     */

    public PaymentSingle getSinglePaymentByName(String paymentName){
        String query = "SELECT payment_id, payment_method, taxes FROM payment WHERE payment_method = ? AND installments = 0";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, paymentName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new PaymentSingle(
                        rs.getInt("payment_id"),
                        rs.getString("payment_method"),
                        rs.getBigDecimal("taxes")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Busca uma forma de pagamento "multi" pelo nome.
     *
     * @param paymentName nome do método de pagamento
     * @return instância de {@link PaymentMulti} ou {@code null} se não encontrada
     */

    public PaymentMulti getMultiPaymentByName(String paymentName){
        String query = "SELECT payment_id, payment_method, installments, taxes FROM payment " +
                "WHERE payment_method = ? AND installments > 0 ORDER BY installments";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, paymentName);
            ResultSet rs = pstmt.executeQuery();

            PaymentMulti paymentMulti = null;

            while (rs.next()){
                if (paymentMulti == null){
                    paymentMulti = new PaymentMulti(
                            rs.getInt("payment_id"),
                            rs.getString("payment_method")
                    );
                }

                int installment = rs.getInt("installments");
                BigDecimal tax = rs.getBigDecimal("taxes");

                paymentMulti.addInstallmentTax(installment, tax);

            }

            return paymentMulti;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtém todos os nomes distintos de métodos de pagamento cadastrados.
     *
     * @return lista de nomes de formas de pagamento
     */

    public List<String> getAllPaymentMethods(){ // Pega todos os payment_method e coloca em uma lista, para testar se o nome do payment_method já existe
        List<String> methods = new ArrayList<>();
        String query = "SELECT DISTINCT payment_method FROM payment";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                methods.add(rs.getString("payment_method"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return methods;
    }

    public List<ChoiceItem> getAllUsers(){
        List<ChoiceItem> sellers = new ArrayList<>();
        String query = "SELECT userid, username FROM users";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                sellers.add(new ChoiceItem(rs.getInt("userid"), rs.getString("username")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sellers;
    }

    public List<ChoiceItem> getAllProductItems() {
        List<ChoiceItem> products = new ArrayList<>();
        String query = "SELECT product_id, product_name FROM product";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                products.add(new ChoiceItem(rs.getInt("product_id"), rs.getString("product_name")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Sale> getAllSales(){
        List<Sale> sales = new ArrayList<>();
        String query = "SELECT sale_id, seller_id, product_id, status, sale_date, " +
                "payment_method, subtotal, total FROM sale";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {


            while (rs.next()){
                Sale s = new Sale();
                s.setSaleId(rs.getInt("sale_id"));
                s.setSellerId(rs.getInt("seller_id"));
                s.setProductId(rs.getInt("product_id"));
                System.out.println(s.getProductId());
                int saleProductId = s.getProductId();
                int saleSellerId = s.getSellerId();
                s.setProductName(getProductNameById(saleProductId));
                s.setSellerName(getSellerNameById(saleSellerId));
                s.setStatus(rs.getString("status"));
                s.setSaleDate(rs.getString("sale_date"));
                s.setPaymentMethod(rs.getString("payment_method"));
                s.setSubTotal(rs.getBigDecimal("subtotal"));
                s.setTotal(rs.getBigDecimal("total"));

                sales.add(s);
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return sales;
    }

    public String getProductNameById(int productId) {
        String query = "SELECT product_name FROM product WHERE product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, productId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("product_name");
                } else {
                    System.out.println("Produto não encontrado para ID: " + productId);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar nome do produto ID " + productId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getSellerNameById(int sellerId) {
        String query = "SELECT username FROM users WHERE userid = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, sellerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                } else {
                    System.out.println("Usuario não encontrado para ID: " + sellerId);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar nome do usuario ID " + sellerId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Salva um novo produto no banco de dados.
     *
     * @param name         nome do produto
     * @param cost         custo do produto
     * @param pixPrice     preço à vista (Pix)
     * @param cardPrice    preço no cartão de crédito
     * @param minPixPrice  preço mínimo no Pix
     * @param minCardPrice preço mínimo no cartão
     * @param stock        quantidade em estoque
     * @param imageBytes   imagem do produto em bytes
     * @throws SQLException se ocorrer falha na operação
     */

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

    /**
     * Salva uma nova forma de pagamento do tipo "single".
     *
     * @param paymentName nome do método de pagamento
     * @param installments número de parcelas (deve ser 0)
     * @param tax taxa aplicada
     */

    public void savePaymentSingleTax(String paymentName, int installments, BigDecimal tax){
        String query = "INSERT INTO payment (payment_method, installments, taxes) VALUES (?, ?, ?)";

        DbFunctions dbFunctions = new DbFunctions();
        List<String> existingMethods = dbFunctions.getAllPaymentMethods();

        if (existingMethods.contains(paymentName)){
            System.out.println("O nome dessa forma de pagamento já existe!");
            return;
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)){

            pstmt.setString(1, paymentName);
            pstmt.setInt(2, installments);
            pstmt.setBigDecimal(3, tax);

            pstmt.executeUpdate();
            System.out.println("Produto Salvo no banco de dados com sucesso!");
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    /**
     * Salva uma nova forma de pagamento do tipo "multi" (parcelado).
     * Suporta até 12 parcelas.
     *
     * @param paymentName nome do método de pagamento
     * @param tax1  taxa da 1ª parcela
     * @param tax2  taxa da 2ª parcela
     * @param tax3  taxa da 3ª parcela
     * @param tax4  taxa da 4ª parcela
     * @param tax5  taxa da 5ª parcela
     * @param tax6  taxa da 6ª parcela
     * @param tax7  taxa da 7ª parcela
     * @param tax8  taxa da 8ª parcela
     * @param tax9  taxa da 9ª parcela
     * @param tax10 taxa da 10ª parcela
     * @param tax11 taxa da 11ª parcela
     * @param tax12 taxa da 12ª parcela
     */

    public void savePaymentMultiTax(String paymentName, BigDecimal tax1, BigDecimal tax2, BigDecimal tax3,
                                    BigDecimal tax4, BigDecimal tax5, BigDecimal tax6, BigDecimal tax7, BigDecimal tax8,
                                    BigDecimal tax9, BigDecimal tax10, BigDecimal tax11, BigDecimal tax12){
        String query = "INSERT INTO payment (payment_method, installments, taxes) VALUES (?, ?, ?)";

        DbFunctions dbFunctions = new DbFunctions();
        List<String> existingMethods = dbFunctions.getAllPaymentMethods();

        if (existingMethods.contains(paymentName)){
            System.out.println("O nome dessa forma de pagamento já existe!");
            return;
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)){

            BigDecimal[] taxes = {tax1, tax2, tax3, tax4, tax5, tax6, tax7, tax8, tax9, tax10, tax11, tax12};

            for (int i = 0; i < taxes.length; i++){
                if (taxes[i] != null){ // Só salva se a taxa não for nula
                    pstmt.setString(1, paymentName);
                    pstmt.setInt(2, i + 1); // installments: 1, 2, 3, ..., 12
                    pstmt.setBigDecimal(3, taxes[i]); // Adiciona ao batch para execução em lote
                    pstmt.addBatch();
                }
            }

            pstmt.executeBatch();
            System.out.println("Produto Salvo no banco de dados com sucesso!");
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void saveSale(int sellerId, int productId, String status, String saleDate,
                         String paymentMethod, BigDecimal subtotal, BigDecimal total) {
        String query = "INSERT INTO sale (seller_id, product_id, status, sale_date, payment_method, subtotal, total) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)){

            pstmt.setInt(1, sellerId);
            pstmt.setInt(2, productId);
            pstmt.setString(3, status);
            pstmt.setString(4, saleDate);
            pstmt.setString(5, paymentMethod);
            pstmt.setBigDecimal(6, subtotal);
            pstmt.setBigDecimal(7, total);

            pstmt.executeUpdate();

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Atualiza os dados de um produto existente.
     *
     * @param productId    ID do produto
     * @param name         novo nome
     * @param cost         novo custo
     * @param pixPrice     novo preço Pix
     * @param cardPrice    novo preço no cartão
     * @param minPixPrice  novo preço mínimo Pix
     * @param minCardPrice novo preço mínimo cartão
     * @param stock        nova quantidade em estoque
     * @param imageBytes   nova imagem do produto
     * @throws SQLException se ocorrer erro na atualização
     */

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

    /**
     * Atualiza uma forma de pagamento "single".
     *
     * @param paymentName   novo nome
     * @param tax           nova taxa
     * @param oldPaymentName nome antigo do método de pagamento
     */

    public void updateSinglePayment(String paymentName, BigDecimal tax, String oldPaymentName){
        String query = "UPDATE payment SET payment_method = ?, taxes = ? WHERE payment_method = ? AND installments = 0";

        DbFunctions dbFunctions = new DbFunctions();
        List<String> existingMethods = dbFunctions.getAllPaymentMethods();

        if (existingMethods.contains(paymentName) && !Objects.equals(oldPaymentName, paymentName)){
            System.out.println("O nome dessa forma de pagamento já existe!");
            return;
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)){

            pstmt.setString(1, paymentName);
            pstmt.setBigDecimal(2, tax);
            pstmt.setString(3, oldPaymentName);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("Forma de pagamento atualizada com sucesso!");
                SessionManager.getInstance().setPaymentName(paymentName);
            }
            else{
                System.out.println("Ocorrey algum erro ao atualizar o método de pagamento");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Atualiza uma forma de pagamento "multi".
     *
     * @param paymentName   novo nome
     * @param tax1   taxas de 1x a 12x
     * @param oldPaymentName nome antigo do método
     */

    public void updateMultiPayment(String paymentName, BigDecimal tax1, BigDecimal tax2, BigDecimal tax3,
                                   BigDecimal tax4, BigDecimal tax5, BigDecimal tax6, BigDecimal tax7,
                                   BigDecimal tax8, BigDecimal tax9, BigDecimal tax10, BigDecimal tax11,
                                   BigDecimal tax12, String oldPaymentName){
        String query = "UPDATE payment SET payment_method = ?, taxes = ? WHERE payment_method = ? AND installments = ?";

        DbFunctions dbFunctions = new DbFunctions();
        List<String> existingMethods = dbFunctions.getAllPaymentMethods();

        if (existingMethods.contains(paymentName) && !Objects.equals(oldPaymentName, paymentName)){
            System.out.println("O nome dessa forma de pagamento já existe!");
            return;
        }

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)){

            BigDecimal[] taxes = {tax1, tax2, tax3, tax4, tax5, tax6, tax7, tax8, tax9, tax10, tax11, tax12};

            for (int i = 0; i < taxes.length; i++){
                pstmt.setString(1, paymentName);
                pstmt.setBigDecimal(2, taxes[i]);
                pstmt.setString(3, oldPaymentName);
                pstmt.setInt(4, i + 1);

                pstmt.addBatch();
            }

            SessionManager.getInstance().setPaymentName(paymentName);
            pstmt.executeBatch();
            System.out.println("Produto atualizado no banco de dados com sucesso!");
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    /**
     * Exclui um produto pelo ID.
     *
     * @param productId ID do produto a ser excluído
     */

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

    /**
     * Exclui uma forma de pagamento pelo nome.
     *
     * @param paymentName nome da forma de pagamento
     */

    public void deletePayment(String paymentName){
        String query = "DELETE FROM payment WHERE payment_method = ?";

        try (Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement(query)){

            pstmt.setString(1, paymentName);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0){
                System.out.println("Forma de pagamento Excluido com sucesso");
            }
            else {
                System.out.println("Nenhuma Forma de pagamento foi excluido!");
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


}
