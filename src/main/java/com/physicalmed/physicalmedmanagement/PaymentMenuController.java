package com.physicalmed.physicalmedmanagement;

import com.physicalmed.physicalmedmanagement.utils.ButtonEffects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentMenuController implements Initializable {

    @FXML
    private Label labelForTax;
    @FXML
    private Label labelTax;
    @FXML
    private Label labelForTax1;
    @FXML
    private Label labelTax1;
    @FXML
    private Label labelForTax2;
    @FXML
    private Label labelTax2;
    @FXML
    private Label labelForTax3;
    @FXML
    private Label labelTax3;
    @FXML
    private Label labelForTax4;
    @FXML
    private Label labelTax4;
    @FXML
    private Label labelForTax5;
    @FXML
    private Label labelTax5;
    @FXML
    private Label labelForTax6;
    @FXML
    private Label labelTax6;
    @FXML
    private Label labelForTax7;
    @FXML
    private Label labelTax7;
    @FXML
    private Label labelForTax8;
    @FXML
    private Label labelTax8;
    @FXML
    private Label labelForTax9;
    @FXML
    private Label labelTax9;
    @FXML
    private Label labelForTax10;
    @FXML
    private Label labelTax10;
    @FXML
    private Label labelForTax11;
    @FXML
    private Label labelTax11;
    @FXML
    private Label labelForTax12;
    @FXML
    private Label labelTax12;
    @FXML
    private Label labelPaymentName;
    @FXML
    private Label labelPaymentDivisible;
    @FXML
    private TableView<PaymentSingle> tablePaymentSingle;
    @FXML
    private TableColumn<PaymentSingle, String> colNameSingle;
    @FXML
    private TableView<PaymentMulti> tablePaymentMulti;
    @FXML
    private TableColumn<PaymentMulti, String> colNameMulti;
    @FXML
    private Button buttonReturn;
    @FXML
    private ImageView buttonReturnIcon;
    @FXML
    private Button buttonAddPayment;
    @FXML
    private Button buttonUpdatePayment;
    @FXML
    private Button buttonDeletePayment;

    private DbFunctions db = new DbFunctions();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { // Esse método irá rodar ao iniciar a tela
        // Para o efeito ao clicar nos botões
        ButtonEffects.applyPressEffect(buttonAddPayment, 0.95, 0.95);
        ButtonEffects.applyPressEffect(buttonUpdatePayment, 0.95, 0.95);
        ButtonEffects.applyPressEffect(buttonDeletePayment, 0.95, 0.95);
        ButtonEffects.applyPressEffect(buttonReturn, 0.95, 0.95);
        applyEffects(); // Aplica efeitos em labels e buttons


        colNameSingle.setCellValueFactory(new PropertyValueFactory<>("paymentName"));
        colNameMulti.setCellValueFactory(new PropertyValueFactory<>("paymentName"));

        loadSinglePayments();
        loadMultiPayments();

        tablePaymentSingle.getSelectionModel().selectedItemProperty().addListener( // Evento ao selecionar produto
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null){
                        tablePaymentMulti.getSelectionModel().clearSelection();
                        showPaymentDetailSingle(newSelection);
                    }
                }
        );
        tablePaymentMulti.getSelectionModel().selectedItemProperty().addListener( // Evento ao selecionar produto
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null){
                        tablePaymentSingle.getSelectionModel().clearSelection();
                        showPaymentDetailMulti(newSelection);
                    }
                }
        );
    }

    private void loadSinglePayments(){
        System.out.println("Carregando Tabela Single");
        List<PaymentSingle> payments = db.getAllSinglePayments();
        ObservableList<PaymentSingle> observableList = FXCollections.observableArrayList(payments);
        tablePaymentSingle.setItems(observableList);
    }

    private void showPaymentDetailSingle(PaymentSingle paymentSingle){
        System.out.println("Mostrando detalhes Single");
        disableMultiTaxLabels();
        labelPaymentName.setText(paymentSingle.getPaymentName());
        labelPaymentDivisible.setText("Não possibilita parcelamento");
        labelTax.setText(String.valueOf(paymentSingle.getTax()).concat("%"));
    }

    private void loadMultiPayments(){
        System.out.println("Carregando Tabela Multi");
        List<PaymentMulti> payments = db.getAllMultiPayments();
        ObservableList<PaymentMulti> observableList = FXCollections.observableArrayList(payments);
        tablePaymentMulti.setItems(observableList);
    }

    private void showPaymentDetailMulti (PaymentMulti paymentMulti){
        System.out.println("Mostrando detalhes Multi");
        disableSingleTaxLabels(); // deixa visíveis os labels de 1..12
        labelPaymentName.setText(paymentMulti.getPaymentName());
        labelPaymentDivisible.setText("Sim, possibilita parcelamento");

        // Array com os labels de taxa (index 0 => 1x, index 11 => 12x)
        Label[] taxLabels = new Label[] {
                labelTax1, labelTax2, labelTax3, labelTax4, labelTax5, labelTax6,
                labelTax7, labelTax8, labelTax9, labelTax10, labelTax11, labelTax12
        };

        for (int i = 1; i <= 12; i++) {
            BigDecimal tax = paymentMulti.getTaxForInstallment(i);
            Label lbl = taxLabels[i - 1];
            if (tax != null) {
                lbl.setText(tax.toString().concat("%"));
            } else {
                lbl.setText("---"); // sem taxa definida para essa parcela
            }
        }
    }


    @FXML
    private void handleReturn(){
        Stage currentStage = (Stage) buttonReturn.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/admin-screen-view.fxml", "Tela Principal", currentStage);
    }

    @FXML
    private void handleAddPayment(){
        Stage currentStage = (Stage) buttonReturn.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/payment-add-view.fxml", "Cadastrar Nova Forma De Pagamento", currentStage);
    }

    @FXML
    private void handleUpdatePayment(){
        PaymentSingle paymentSingleSelected = tablePaymentSingle.getSelectionModel().getSelectedItem();
        PaymentMulti paymentMultiSelected = tablePaymentMulti.getSelectionModel().getSelectedItem();

        if (paymentSingleSelected != null){
            String paymentName = paymentSingleSelected.getPaymentName();

            SessionManager.getInstance().setPaymentIsSingle(true);
            SessionManager.getInstance().setPaymentName(paymentName);
            System.out.println("Item selecionado: " + paymentName);

            Stage currentStage = (Stage) buttonUpdatePayment.getScene().getWindow();
            ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/payment-update-view.fxml", "Atualizar Forma De Pagamento", currentStage);
        }
        else if (paymentMultiSelected != null){
            String paymentName = paymentMultiSelected.getPaymentName();

            SessionManager.getInstance().setPaymentIsSingle(false);
            SessionManager.getInstance().setPaymentName(paymentName);
            System.out.println("Item selecionado: " + paymentName);

            Stage currentStage = (Stage) buttonUpdatePayment.getScene().getWindow();
            ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/payment-update-view.fxml", "Atualizar Forma De Pagamento", currentStage);
        }

        else {
            System.out.println("Nenhum Item Selecionado");
        }

    }

    @FXML
    private void handleDeletePayment(){
        PaymentSingle paymentSingleSelected = tablePaymentSingle.getSelectionModel().getSelectedItem();
        PaymentMulti paymentMultiSelected = tablePaymentMulti.getSelectionModel().getSelectedItem();

        if (paymentSingleSelected != null){
            String paymentName = paymentSingleSelected.getPaymentName();

            SessionManager.getInstance().setPaymentName(paymentName);
            System.out.println("Item selecionado: " + paymentName);

            db.deletePayment(paymentName);
            loadSinglePayments();
        }
        else if (paymentMultiSelected != null){
            String paymentName = paymentMultiSelected.getPaymentName();

            SessionManager.getInstance().setPaymentName(paymentName);
            System.out.println("Item selecionado: " + paymentName);

            db.deletePayment(paymentName);
            loadMultiPayments();
        }

        else {
            System.out.println("Nenhum Item Selecionado");
        }

    }

    private void disableMultiTaxLabels(){
        labelForTax.setVisible(true);
        labelTax.setVisible(true);
        labelForTax1.setVisible(false);
        labelForTax2.setVisible(false);
        labelForTax3.setVisible(false);
        labelForTax4.setVisible(false);
        labelForTax5.setVisible(false);
        labelForTax6.setVisible(false);
        labelForTax7.setVisible(false);
        labelForTax8.setVisible(false);
        labelForTax9.setVisible(false);
        labelForTax10.setVisible(false);
        labelForTax11.setVisible(false);
        labelForTax12.setVisible(false);
        labelTax1.setVisible(false);
        labelTax2.setVisible(false);
        labelTax3.setVisible(false);
        labelTax4.setVisible(false);
        labelTax5.setVisible(false);
        labelTax6.setVisible(false);
        labelTax7.setVisible(false);
        labelTax8.setVisible(false);
        labelTax9.setVisible(false);
        labelTax10.setVisible(false);
        labelTax11.setVisible(false);
        labelTax12.setVisible(false);
    }

    private void disableSingleTaxLabels(){
        labelForTax.setVisible(false);
        labelTax.setVisible(false);
        labelForTax1.setVisible(true);
        labelForTax2.setVisible(true);
        labelForTax3.setVisible(true);
        labelForTax4.setVisible(true);
        labelForTax5.setVisible(true);
        labelForTax6.setVisible(true);
        labelForTax7.setVisible(true);
        labelForTax8.setVisible(true);
        labelForTax9.setVisible(true);
        labelForTax10.setVisible(true);
        labelForTax11.setVisible(true);
        labelForTax12.setVisible(true);
        labelTax1.setVisible(true);
        labelTax2.setVisible(true);
        labelTax3.setVisible(true);
        labelTax4.setVisible(true);
        labelTax5.setVisible(true);
        labelTax6.setVisible(true);
        labelTax7.setVisible(true);
        labelTax8.setVisible(true);
        labelTax9.setVisible(true);
        labelTax10.setVisible(true);
        labelTax11.setVisible(true);
        labelTax12.setVisible(true);
    }

    private void applyEffects(){
        Label[] labelItens = new Label[]{
                labelTax, labelTax1, labelTax2, labelTax3, labelTax4, labelTax5, labelTax6,
                labelTax7, labelTax8, labelTax9, labelTax10, labelTax11, labelTax12,
                labelForTax, labelForTax1, labelForTax2, labelForTax3, labelForTax4, labelForTax5,
                labelForTax6, labelForTax7, labelForTax8, labelForTax9, labelForTax10, labelForTax11, labelForTax12,
                labelPaymentName, labelPaymentDivisible
        };
        for (Label label : labelItens){
            ButtonEffects.applyHoverEffect(label, 1.01, 1.01);
        }

        Button[] buttonItens = new Button[]{
                buttonAddPayment, buttonUpdatePayment, buttonDeletePayment, buttonReturn
        };
        for (Button button : buttonItens){
            ButtonEffects.applyHoverEffect(button, 1.01, 1.01);
        }
    }
}