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
import java.net.URL;
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
        System.out.println("Vendedor Selecionado: " + selectedSeller);
        System.out.println("Produto Selecionado: " + selectProduct);
        System.out.println("Forma de Pagamento: " + selectPayment);

        int sellerId = selectedSeller.getId();
        int productId = selectProduct.getId();
        String paymentMethod = selectPayment.getName();
        System.out.println("Id do vendedor: " + sellerId);
        System.out.println("Id do produto: " + productId);
        System.out.println("Payment Method: " + paymentMethod);
    }

    @FXML
    private void handleReturn(){
        Stage currentStage = (Stage) buttonReturn.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/admin-screen-view.fxml", "Tela Principal", currentStage);
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
                    choiceBoxInstallments.getItems().add(installment + "x");
                }
            }
        }
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
