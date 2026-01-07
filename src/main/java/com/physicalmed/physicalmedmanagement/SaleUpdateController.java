package com.physicalmed.physicalmedmanagement;

import com.physicalmed.physicalmedmanagement.utils.ButtonEffects;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class SaleUpdateController implements Initializable {

    @FXML
    private Label labelProductName;
    @FXML
    private Label labelPix;
    @FXML
    private Label labelPixDiscount;
    @FXML
    private Label labelCredit;
    @FXML
    private Label labelCreditDiscount;
    @FXML
    private Label labelStock;
    @FXML
    private ImageView imageViewProduct;
    @FXML
    private Label labelSellerName;
    @FXML
    private Label labelPaymentMethod;
    @FXML
    private ChoiceBox<String> choiceBoxInstallments;
    @FXML
    private ChoiceBox<String> choiceBoxStatus;
    @FXML
    private Label labelInstallments;
    @FXML
    private Label labelError;
    @FXML
    private DatePicker datePickerSaleDate;
    @FXML
    private TextField txtSubtotalSellPrice;
    @FXML
    private TextField txtTotalSellPrice;
    @FXML
    private Button buttonUpdateSale;
    @FXML
    private Button buttonReturn;
    @FXML
    private Button buttonCancel;

    private int currentProductId;
    private int currentSaleId;
    private int currentSellerId;
    private String currentSaleStatus;
    private byte[] currentImageByte;
    private DbFunctions dbFunctions = new DbFunctions();
    private final DecimalFormat moneyFormat = new DecimalFormat("R$ #,##0.00"); // Para formatção dos preços

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        currentSaleId = SessionManager.getInstance().getSaleId();
        System.out.println("Sale Id recebido:" + currentSaleId);

        choiceBoxStatus.getItems().addAll("PENDENTE", "APROVADA", "CANCELADA");

        loadSaleData(currentSaleId);

        applyNumericCommaMask(txtSubtotalSellPrice);
        applyNumericCommaMask(txtTotalSellPrice);
        replaceNumeric(txtSubtotalSellPrice);
        replaceNumeric(txtTotalSellPrice);
        applyEffects();

    }

    private void loadSaleData(int currentSaleId){
        Sale sale = dbFunctions.getSaleById(currentSaleId);
        currentProductId = sale.getProductId();
        currentSellerId = sale.getSellerId();
        String SellerName = dbFunctions.getSellerNameById(currentSellerId);
        Product product = dbFunctions.getProductById(currentProductId);


        if (sale != null){
            labelSellerName.setText(SellerName);
            labelPaymentMethod.setText(sale.getPaymentMethod());
            txtSubtotalSellPrice.setText(sale.getSubTotal().toString());
            txtTotalSellPrice.setText(sale.getTotal().toString());
            currentSaleStatus = sale.getStatus();

            if (currentSaleStatus.equals("PENDENTE")){
                choiceBoxStatus.setValue("PENDENTE");
            }
            if (currentSaleStatus.equals("APROVADA")){
                choiceBoxStatus.setValue("APROVADA");
            }
            if (currentSaleStatus.equals("CANCELADA")){
                choiceBoxStatus.setValue("CANCELADA");
            }
        }
        else {
            System.out.println("Erro ao retornar venda");
        }

        if (product != null){
            labelProductName.setText(product.getProductName());
            labelPix.setText(moneyFormat.format(product.getPixPrice()));
            labelPixDiscount.setText(moneyFormat.format(product.getPixPriceDiscount()));
            labelCredit.setText(moneyFormat.format(product.getCreditPrice()));
            labelCreditDiscount.setText(moneyFormat.format(product.getCreditPriceDiscount()));
            labelStock.setText(String.valueOf(product.getStock()));

            if (product.getProductImage() != null){
                currentImageByte = product.getProductImage(); // Armazena os bytes da imagem atual

                Image img = new Image(new ByteArrayInputStream(product.getProductImage()));
                imageViewProduct.setImage(img);
            }
        }
        else {
            System.out.println("Erro ao retornar produto");
        }

    }

    @FXML
    private void handleUpdateSale(){

    }

    @FXML
    private void handleReturn(){
        Stage currentStage = (Stage) buttonReturn.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/admin-screen-view.fxml", "Tela Principal", currentStage);
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
                choiceBoxStatus
        };
        for (ChoiceBox<?> choiceBox : choiceBoxes) {
            ButtonEffects.applyHoverEffect(choiceBox, 1.01, 1.01);
        }

        Button[] buttonItens = new Button[]{
                buttonUpdateSale, buttonReturn, buttonCancel
        };
        for (Button button : buttonItens){
            ButtonEffects.applyHoverEffect(button, 1.01, 1.01);
        }

        ButtonEffects.applyHoverEffect(datePickerSaleDate, 1.01, 1.01);
        ButtonEffects.applyHoverEffect(txtSubtotalSellPrice, 1.01, 1.01);
        ButtonEffects.applyHoverEffect(txtTotalSellPrice, 1.01, 1.01);
    }

    private void replaceNumeric(TextField textField){
        textField.setText(textField.getText().replace(".", ","));
    }

}
