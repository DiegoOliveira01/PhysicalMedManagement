package com.physicalmed.physicalmedmanagement;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PaymentAddController implements Initializable {

    @FXML
    private TextField txtName;
    @FXML
    private CheckBox checkBoxInstallment;
    @FXML
    private TextField txtTax;
    @FXML
    private TextField txtTax1x;
    @FXML
    private TextField txtTax2x;
    @FXML
    private TextField txtTax3x;
    @FXML
    private TextField txtTax4x;
    @FXML
    private TextField txtTax5x;
    @FXML
    private TextField txtTax6x;
    @FXML
    private TextField txtTax7x;
    @FXML
    private TextField txtTax8x;
    @FXML
    private TextField txtTax9x;
    @FXML
    private TextField txtTax10x;
    @FXML
    private TextField txtTax11x;
    @FXML
    private TextField txtTax12x;
    @FXML
    private Label labelTax;
    @FXML
    private Label labelTax1x;
    @FXML
    private Label labelTax2x;
    @FXML
    private Label labelTax3x;
    @FXML
    private Label labelTax4x;
    @FXML
    private Label labelTax5x;
    @FXML
    private Label labelTax6x;
    @FXML
    private Label labelTax7x;
    @FXML
    private Label labelTax8x;
    @FXML
    private Label labelTax9x;
    @FXML
    private Label labelTax10x;
    @FXML
    private Label labelTax11x;
    @FXML
    private Label labelTax12x;
    @FXML
    private Label labelError;
    @FXML
    private Button buttonReturn;
    @FXML
    private Button buttonSavePayment;
    @FXML
    private Button buttonCancel;
    @FXML
    private ImageView buttonReturnIcon;

    private final DbFunctions dbFunctions = new DbFunctions();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        disableTaxesTxt(); // Iniciar com a CheckBox marcada

        // Para o efeito ao clicar nos botões
        buttonSavePayment.setOnMousePressed(e ->{
            buttonSavePayment.setScaleX(0.95);
            buttonSavePayment.setScaleY(0.95);
        });
        buttonSavePayment.setOnMouseReleased(e ->{
            buttonSavePayment.setScaleX(1);
            buttonSavePayment.setScaleY(1);
        });
        buttonCancel.setOnMousePressed(e ->{
            buttonCancel.setScaleX(0.95);
            buttonCancel.setScaleY(0.95);
        });
        buttonCancel.setOnMouseReleased(e ->{
            buttonCancel.setScaleX(1);
            buttonCancel.setScaleY(1);
        });
        buttonReturn.setOnMousePressed(e ->{
            buttonReturn.setScaleX(0.95);
            buttonReturn.setScaleY(0.95);
            buttonReturnIcon.setScaleX(0.90);
            buttonReturnIcon.setScaleY(0.90);
        });
        buttonReturn.setOnMouseReleased(e ->{
            buttonReturn.setScaleX(1);
            buttonReturn.setScaleY(1);
            buttonReturnIcon.setScaleX(1);
            buttonReturnIcon.setScaleY(1);
        });

        // Define oque pode ser digitado nos campos txt
        applyLettersOnlyMask(txtName);
        applyNumericCommaMask(txtTax);
        applyNumericCommaMask(txtTax1x);
        applyNumericCommaMask(txtTax2x);
        applyNumericCommaMask(txtTax3x);
        applyNumericCommaMask(txtTax4x);
        applyNumericCommaMask(txtTax5x);
        applyNumericCommaMask(txtTax6x);
        applyNumericCommaMask(txtTax7x);
        applyNumericCommaMask(txtTax8x);
        applyNumericCommaMask(txtTax9x);
        applyNumericCommaMask(txtTax10x);
        applyNumericCommaMask(txtTax11x);
        applyNumericCommaMask(txtTax12x);
    }

    @FXML
    private void handleSavePayment(){
        boolean checkBoxIsSelected = checkBoxInstallment.isSelected();
        labelError.setText("");

        if (checkBoxIsSelected){ // CheckBox Marcada (Pagamento à vista)
            if (txtName.getText().isEmpty() || txtTax.getText().isEmpty()){ // Checa se os campos estão preenchidos
                labelError.setText("Preencha o campo de taxa para essa forma de pagamento, ser não houver taxa coloque 0,00");
                return;
            }
            else { // Se os campos estiverem preenchidos segue para salvar no banco
                try {
                    String paymentName = txtName.getText();
                    int installments = 0;
                    BigDecimal tax = new BigDecimal(txtTax.getText().replace(",", "."));

                    dbFunctions.savePaymentSingleTax(paymentName, installments, tax);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }
        if (!checkBoxIsSelected){ // CheckBox Desmarcada (Pagamento parcelado)
            if (txtName.getText().isEmpty() || txtTax1x.getText().isEmpty() || txtTax2x.getText().isEmpty() || txtTax3x.getText().isEmpty() // Checa se os campos estão preenchidos
            || txtTax4x.getText().isEmpty() || txtTax5x.getText().isEmpty() || txtTax6x.getText().isEmpty()
            || txtTax7x.getText().isEmpty() || txtTax8x.getText().isEmpty() || txtTax9x.getText().isEmpty()
            || txtTax10x.getText().isEmpty() || txtTax11x.getText().isEmpty() || txtTax12x.getText().isEmpty()){
                labelError.setText("Preencha todos os campos de taxa para essa forma de pagamento");
                return;
            }
            else { // Se os campos estiverem preenchidos segue para salvar no banco
                try {
                    String paymentName = txtName.getText();
                    BigDecimal tax1 = new BigDecimal(txtTax1x.getText().replace(",", "."));
                    BigDecimal tax2 = new BigDecimal(txtTax2x.getText().replace(",", "."));
                    BigDecimal tax3 = new BigDecimal(txtTax3x.getText().replace(",", "."));
                    BigDecimal tax4 = new BigDecimal(txtTax4x.getText().replace(",", "."));
                    BigDecimal tax5 = new BigDecimal(txtTax5x.getText().replace(",", "."));
                    BigDecimal tax6 = new BigDecimal(txtTax6x.getText().replace(",", "."));
                    BigDecimal tax7 = new BigDecimal(txtTax7x.getText().replace(",", "."));
                    BigDecimal tax8 = new BigDecimal(txtTax8x.getText().replace(",", "."));
                    BigDecimal tax9 = new BigDecimal(txtTax9x.getText().replace(",", "."));
                    BigDecimal tax10 = new BigDecimal(txtTax10x.getText().replace(",", "."));
                    BigDecimal tax11 = new BigDecimal(txtTax11x.getText().replace(",", "."));
                    BigDecimal tax12 = new BigDecimal(txtTax12x.getText().replace(",", "."));

                    dbFunctions.savePaymentMultiTax(paymentName, tax1, tax2, tax3, tax4, tax5, tax6, tax7, tax8, tax9, tax10, tax11, tax12);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @FXML
    private void handleInstallmentsVisibility(){
        boolean checkBoxIsSelected =  checkBoxInstallment.isSelected();

        if (checkBoxIsSelected){ // CheckBox Marcada (Pagamento à vista)
            disableTaxesTxt();
            enableTaxTxt();
        }
        if (!checkBoxIsSelected){ // CheckBox Desmarcada (Pagamento parcelado)
            disableTaxTxt();
            enableTaxesTxt();
        }
    }

    private void disableTaxesTxt(){
        txtTax1x.setVisible(false);
        txtTax2x.setVisible(false);
        txtTax3x.setVisible(false);
        txtTax4x.setVisible(false);
        txtTax5x.setVisible(false);
        txtTax6x.setVisible(false);
        txtTax7x.setVisible(false);
        txtTax8x.setVisible(false);
        txtTax9x.setVisible(false);
        txtTax10x.setVisible(false);
        txtTax11x.setVisible(false);
        txtTax12x.setVisible(false);
        labelTax1x.setVisible(false);
        labelTax2x.setVisible(false);
        labelTax3x.setVisible(false);
        labelTax4x.setVisible(false);
        labelTax5x.setVisible(false);
        labelTax6x.setVisible(false);
        labelTax7x.setVisible(false);
        labelTax8x.setVisible(false);
        labelTax9x.setVisible(false);
        labelTax10x.setVisible(false);
        labelTax11x.setVisible(false);
        labelTax12x.setVisible(false);
    }
    private void enableTaxesTxt(){
        txtTax1x.setVisible(true);
        txtTax2x.setVisible(true);
        txtTax3x.setVisible(true);
        txtTax4x.setVisible(true);
        txtTax5x.setVisible(true);
        txtTax6x.setVisible(true);
        txtTax7x.setVisible(true);
        txtTax8x.setVisible(true);
        txtTax9x.setVisible(true);
        txtTax10x.setVisible(true);
        txtTax11x.setVisible(true);
        txtTax12x.setVisible(true);
        labelTax1x.setVisible(true);
        labelTax2x.setVisible(true);
        labelTax3x.setVisible(true);
        labelTax4x.setVisible(true);
        labelTax5x.setVisible(true);
        labelTax6x.setVisible(true);
        labelTax7x.setVisible(true);
        labelTax8x.setVisible(true);
        labelTax9x.setVisible(true);
        labelTax10x.setVisible(true);
        labelTax11x.setVisible(true);
        labelTax12x.setVisible(true);
    }
    private void disableTaxTxt(){
        txtTax.setVisible(false);
        labelTax.setVisible(false);
    }
    private void enableTaxTxt(){
        txtTax.setVisible(true);
        labelTax.setVisible(true);
    }

    private void applyNumericCommaMask(TextField textField){
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("\\d{0,2}(,\\d{0,2})?")){
                textField.setText(oldText);
            }
        });
    }

    private void applyLettersOnlyMask(TextField textField) {
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("[a-zA-ZÀ-ÿ\\s]*")) {
                textField.setText(oldText);
            }
        });
    }

    @FXML
    private void handleReturn(){
        Stage currentStage = (Stage) buttonReturn.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/payment-menu-view.fxml", "Menu de formas de pagamento", currentStage);
    }

}
