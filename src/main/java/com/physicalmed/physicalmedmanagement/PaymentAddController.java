package com.physicalmed.physicalmedmanagement;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
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
    private Button buttonReturn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        disableTaxesTxt();
    }

    @FXML
    private void handleSavePayment(){

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

    @FXML
    private void handleReturn(){
        Stage currentStage = (Stage) buttonReturn.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/payment-menu-view.fxml", "Menu de formas de pagamento", currentStage);
    }

}
