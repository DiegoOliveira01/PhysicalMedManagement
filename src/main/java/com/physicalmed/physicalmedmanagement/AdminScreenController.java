package com.physicalmed.physicalmedmanagement;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminScreenController implements Initializable {
    @FXML
    private DatePicker datePickerFrom;
    @FXML
    private DatePicker datePickerTo;
    @FXML
    private ChoiceBox<String> choiceBoxSale;
    @FXML
    private Label labelUserName;
    @FXML
    private Button buttonAddSale;
    @FXML
    private Button buttonManageProduct;
    @FXML
    private Button buttonManagePayment;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        String UserName = UserSession.getInstance().getUsername();
        labelUserName.setText(UserName);

        datePickerFrom.setStyle("-fx-font-family: 'Segoe UI';" + "-fx-font-size: 14px;" + "-fx-text-fill: #333333;");
        datePickerTo.setStyle("-fx-font-family: 'Segoe UI';" + "-fx-font-size: 14px;" + "-fx-text-fill: #333333;");
        choiceBoxSale.setStyle("-fx-font-family: 'Segoe UI';" + "-fx-font-size: 14px;");

        choiceBoxSale.getItems().addAll(
            "Vendas do dia",
                "Vendas pendentes",
                "Todas as vendas"
        );

        // Para o efeito do botão
        buttonManageProduct.setOnMousePressed(e ->{
            buttonManageProduct.setScaleX(0.95);
            buttonManageProduct.setScaleY(0.95);
        });
        buttonManageProduct.setOnMouseReleased(e ->{
            buttonManageProduct.setScaleX(1);
            buttonManageProduct.setScaleY(1);
        });
    }

    public void startSaleAdd(){
        Stage currentStage = (Stage) buttonManageProduct.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/sale-add-view.fxml", "Cadastrar Venda", currentStage);
    }

    public void startProductMenu(){
        Stage currentStage = (Stage) buttonManageProduct.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/product-menu-view.fxml", "Menu de produtos", currentStage);

    }
    public void startPaymentMenu(){
        Stage currentStage = (Stage) buttonManageProduct.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/payment-menu-view.fxml", "Menu de formas de pagamento", currentStage);
    }

}
