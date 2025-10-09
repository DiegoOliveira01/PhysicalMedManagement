package com.physicalmed.physicalmedmanagement;

import com.physicalmed.physicalmedmanagement.utils.ButtonEffects;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SaleAddController implements Initializable {

    @FXML
    private ChoiceBox<ChoiceItem> choiceBoxSeller;
    @FXML
    private ChoiceBox<ChoiceItem> choiceBoxProduct;
    @FXML
    private ChoiceBox<ChoiceItemPayment> choiceBoxPaymentMethod;
    @FXML
    private ChoiceBox<String> choiceBoxInstallments;
    @FXML
    private Label labelInstallments;
    @FXML
    private DatePicker datePickerSaleDate;
    @FXML
    private TextField txtSellPrice;
    @FXML
    private Button buttonSaveSale;
    @FXML
    private Button buttonReturn;
    @FXML
    private Button buttonCancel;
    private DbFunctions dbFunctions = new DbFunctions();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        applyEffects();
        applyNumericCommaMask(txtSellPrice);
        choiceBoxSeller.setStyle("-fx-font-family: 'Segoe UI';" + "-fx-font-size: 14px;");
        choiceBoxProduct.setStyle("-fx-font-family: 'Segoe UI';" + "-fx-font-size: 14px;");
        choiceBoxPaymentMethod.setStyle("-fx-font-family: 'Segoe UI';" + "-fx-font-size: 14px;");
        datePickerSaleDate.setStyle("-fx-font-family: 'Segoe UI';" + "-fx-font-size: 14px;" + "-fx-text-fill: #333333;");

        setDateFormat();
        populateChoiceBox();
    }

    private void populateChoiceBox(){
        DbFunctions db = new DbFunctions();
        // Preenche produtos
        choiceBoxProduct.getItems().addAll(db.getAllProductItems());

        // Preenche vendedores
        choiceBoxSeller.getItems().addAll(db.getAllUsers());

        // Preenche formas de pagamento
        for (PaymentSingle single : db.getAllSinglePayments()) {
            choiceBoxPaymentMethod.getItems().add(
                    new ChoiceItemPayment(single.getPaymentName(), "SINGLE")
            );
        }

        for (PaymentMulti multi : db.getAllMultiPayments()) {
            choiceBoxPaymentMethod.getItems().add(
                    new ChoiceItemPayment(multi.getPaymentName(), "MULTI")
            );
        }

        // Listener para quando o usuário escolher um método de pagamento
        choiceBoxPaymentMethod.setOnAction(e -> handlePaymentSelection());
    }

    @FXML
    private void handleSaveSale(){
        ChoiceItem selectedSeller = choiceBoxSeller.getValue();
        ChoiceItem selectProduct = choiceBoxProduct.getValue();
        ChoiceItemPayment selectPayment = choiceBoxPaymentMethod.getValue();
        String selectedInstallment = choiceBoxInstallments.getValue();
        var saleDate = datePickerSaleDate.getValue();
        String priceText = txtSellPrice.getText();

        if (selectedSeller == null || selectProduct == null || selectPayment == null || saleDate == null || priceText.isEmpty()){
            System.out.println("Preencha todos os campos antes de prosseguir!");
            return;
        }

        if (selectPayment.getType().equals("MULTI") && selectedInstallment == null){
            System.out.println("Preencha o campo de parcelas!");
            return;
        }

        System.out.println("Vendedor Selecionado: " + selectedSeller);
        System.out.println("Produto Selecionado: " + selectProduct);
        System.out.println("Forma de Pagamento: " + selectPayment);
        System.out.println("Parcela Selecionado: " + selectedInstallment);

        int sellerId = selectedSeller.getId();
        int productId = selectProduct.getId();
        String paymentMethod = selectPayment.getName();
        int installment;
        String status = "PENDENTE";
        if (selectedInstallment == null){
            installment = 1;
        }
        else {
            installment = Integer.parseInt(selectedInstallment);
        }

        BigDecimal subtotal = new BigDecimal(priceText.replace(",", "."));
        BigDecimal total = calculateTotalPrice(subtotal, selectPayment, installment);

        /*
        if (selectPayment.getType().equals("MULTI")) {
            System.out.println("Pagamento Parcelado");
            PaymentMulti paymentMulti = dbFunctions.getMultiPaymentByName(selectPayment.getName());
            BigDecimal tax = paymentMulti.getTaxForInstallment(installment);
            if (tax != null) {
                total = subtotal.add(subtotal.multiply(tax.divide(BigDecimal.valueOf(100))));
            }
        }
        if (!selectPayment.getType().equals("MULTI")) {
            System.out.println("Pagamento À vista");
            PaymentSingle paymentSingle = dbFunctions.getSinglePaymentByName(selectPayment.getName());
            BigDecimal tax = paymentSingle.getTax();
            if (tax != null) {
                total = subtotal.add(subtotal.multiply(tax.divide(BigDecimal.valueOf(100))));
            }
        }

         */

        System.out.println("Id do vendedor: " + sellerId);
        System.out.println("Id do produto: " + productId);
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Quant. parcelas: " + installment);
        System.out.println("Data final: " + saleDate);
        System.out.println("Valor: " + total);

        try {
            dbFunctions.saveSale(sellerId, productId, status, saleDate.toString(), paymentMethod, subtotal, total);
            System.out.println("Indo salvar o produto na DB");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleReturn(){
        Stage currentStage = (Stage) buttonReturn.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/admin-screen-view.fxml", "Tela Principal", currentStage);
    }

    private void setDateFormat() {
        datePickerSaleDate.setConverter(new javafx.util.StringConverter<>() {
            private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        // Define o promptText (texto de exemplo) para o formato brasileiro
        datePickerSaleDate.setPromptText("dd/MM/aaaa");
    }

    private void handlePaymentSelection() {
        ChoiceItemPayment selected = choiceBoxPaymentMethod.getValue();
        if (selected == null) return;

        // Limpa parcelas anteriores
        choiceBoxInstallments.getItems().clear();

        if (selected.getType().equals("SINGLE")) {
            // Se for pagamento à vista
            choiceBoxInstallments.setVisible(false);
            labelInstallments.setVisible(false);
        } else {
            // Pagamento parcelado
            choiceBoxInstallments.setVisible(true);
            labelInstallments.setVisible(true);

            // Busca o PaymentMulti com base no nome
            PaymentMulti paymentMulti = dbFunctions.getMultiPaymentByName(selected.getName());
            if (paymentMulti != null) {
                for (Integer installment : paymentMulti.getInstallmentTaxes().keySet()) {
                    choiceBoxInstallments.getItems().add(String.valueOf(installment));
                }
            }
        }
    }

    private BigDecimal calculateTotalPrice(BigDecimal basePrice, ChoiceItemPayment payment, int installments) {
        try {
            if (!payment.getType().equals("MULTI")) {
                System.out.println("Pagamento À vista");
                // Para pagamentos à vista
                PaymentSingle singlePayment = dbFunctions.getSinglePaymentByName(payment.getName());
                if (singlePayment != null && singlePayment.getTax() != null) {
                    BigDecimal tax = singlePayment.getTax();
                    // CORREÇÃO: converte de percentual para fração
                    BigDecimal taxFraction = tax.divide(BigDecimal.valueOf(100));
                    basePrice = basePrice.multiply(BigDecimal.ONE.subtract(taxFraction));
                    return basePrice = basePrice.setScale(2, RoundingMode.HALF_UP);
                }
            } else {
                // Para pagamentos parcelados
                System.out.println("Pagamento Parcelado");
                PaymentMulti multiPayment = dbFunctions.getMultiPaymentByName(payment.getName());
                if (multiPayment != null) {
                    BigDecimal tax = multiPayment.getInstallmentTaxes().get(installments);
                    if (tax != null) {
                        BigDecimal taxFraction = tax.divide(BigDecimal.valueOf(100));
                        basePrice = basePrice.multiply(BigDecimal.ONE.subtract(taxFraction));
                        return basePrice = basePrice.setScale(2, RoundingMode.HALF_UP);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Retorna o preço base se não encontrar taxa
        return basePrice;
    }

    private void applyNumericCommaMask(TextField textField){
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("\\d{0,7}(,\\d{0,2})?")){
                textField.setText(oldText);
            }
        });
    }

    private void applyEffects(){
        ChoiceBox<?>[] choiceBoxes = new ChoiceBox[]{
                choiceBoxSeller, choiceBoxProduct, choiceBoxPaymentMethod
        };
        for (ChoiceBox<?> choiceBox : choiceBoxes) {
            ButtonEffects.applyHoverEffect(choiceBox, 1.01, 1.01);
        }

        Button[] buttonItens = new Button[]{
                buttonSaveSale, buttonReturn, buttonCancel
        };
        for (Button button : buttonItens){
            ButtonEffects.applyHoverEffect(button, 1.01, 1.01);
        }

        ButtonEffects.applyHoverEffect(datePickerSaleDate, 1.01, 1.01);
        ButtonEffects.applyHoverEffect(txtSellPrice, 1.01, 1.01);
    }

}
