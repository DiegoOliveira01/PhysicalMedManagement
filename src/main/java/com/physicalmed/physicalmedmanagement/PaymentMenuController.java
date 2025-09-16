package com.physicalmed.physicalmedmanagement;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentMenuController implements Initializable {

    @FXML
    private Button buttonReturn;
    @FXML
    private ImageView buttonReturnIcon;
    @FXML
    private Button buttonAddPayment;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { // Esse método irá rodar ao iniciar a tela


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

}