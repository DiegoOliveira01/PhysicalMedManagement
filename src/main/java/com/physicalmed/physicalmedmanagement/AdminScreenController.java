package com.physicalmed.physicalmedmanagement;

import com.physicalmed.physicalmedmanagement.utils.ButtonEffects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AdminScreenController implements Initializable {

    @FXML
    private TableView<Sale> tableSales;
    @FXML
    private TableColumn<Sale, String> colProductName;
    @FXML
    private TableColumn<Sale, BigDecimal> colSaleSubtotal;
    @FXML
    private TableColumn<Sale, BigDecimal> colSalePrice;
    @FXML
    private TableColumn<Sale, String> colSellerName;
    @FXML
    private TableColumn<Sale, String> colSaleDate;
    @FXML
    private TableColumn<Sale, String> colSaleStatus;
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
    private Button buttonManageSale;
    @FXML
    private Button buttonDeleteSale;
    @FXML
    private Button buttonManageUsers;
    @FXML
    private Button buttonManageProduct;
    @FXML
    private Button buttonManagePayment;
    @FXML
    private Label labelDateError;

    private final DecimalFormat moneyFormat = new DecimalFormat("R$ #,##0.00"); // Para formatção dos preços
    private DbFunctions db = new DbFunctions();
    private boolean initializing = true; // Flag para controlar a inicialização

    @Override
    public void initialize(URL url, ResourceBundle rb){
        initializing = true; // Começa a inicialização

        String UserName = UserSession.getInstance().getUsername();
        labelUserName.setText(UserName);

        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colSaleSubtotal.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        colSalePrice.setCellValueFactory(new PropertyValueFactory<>("total"));
        colSellerName.setCellValueFactory(new PropertyValueFactory<>("sellerName"));
        colSaleDate.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        colSaleStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Configuração para formatar a exibição do dado:
        colSaleSubtotal.setCellFactory(col -> new TableCell<Sale, BigDecimal>() {
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty); // Chama o método da classe pai:cite[7]
                if (empty || item == null) {
                    setText(null); // Célula vazia não mostra texto
                    setStyle(""); // Limpa o estilo
                } else {
                    setText(moneyFormat.format(item));
                    Color amareloPersonalizado = Color.rgb(196, 196, 0);
                    setTextFill(amareloPersonalizado);
                }
            }
        });

        colSalePrice.setCellFactory(col -> new TableCell<Sale, BigDecimal>() {
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(moneyFormat.format(item));
                    Color verdePersonalizado = Color.rgb(0, 128, 0);
                    setTextFill(verdePersonalizado);
                }
            }
        });

        colSaleStatus.setCellFactory(col -> new TableCell<Sale, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTextFill(null);
                } else {
                    setText(item);
                    switch (item.toUpperCase()) {
                        case "PENDENTE": setTextFill(Color.rgb(255, 156, 0)); break;
                        case "CONFIRMADO": setTextFill(Color.rgb(0, 128, 0)); break;
                        case "CANCELADO": setTextFill(Color.rgb(255, 0, 0)); break;
                        default: setTextFill(Color.BLACK); break;
                    }
                }
            }
        });

        datePickerFrom.valueProperty().addListener((observableValue, oldValue, newValue) -> applyDateFilter());
        datePickerTo.valueProperty().addListener((observableValue, oldValue, newValue) -> applyDateFilter());
        choiceBoxSale.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!initializing) { // Só executa se não estiver na inicialização
                applyChoiceBoxFilter();
            }
        });
        loadSales();

        applyEffects();
        datePickerFrom.setStyle("-fx-font-family: 'Segoe UI';" + "-fx-font-size: 14px;" + "-fx-text-fill: #333333;");
        datePickerTo.setStyle("-fx-font-family: 'Segoe UI';" + "-fx-font-size: 14px;" + "-fx-text-fill: #333333;");
        choiceBoxSale.setStyle("-fx-font-family: 'Segoe UI';" + "-fx-font-size: 14px;");

        choiceBoxSale.getItems().addAll(
            "Vendas do dia",
                "Vendas pendentes",
                "Todas as vendas"
        );
        // Define o valor padrão
        choiceBoxSale.setValue("Todas as vendas");
        initializing = false; // Finaliza a inicialização
    }

    public void loadSales(){
        System.out.println("Carregando Vendas");
        List<Sale> sales = db.getAllSales();

        ObservableList<Sale> observableList = FXCollections.observableArrayList(sales);
        tableSales.setItems(observableList);
    }

    public void startSaleAdd(){
        Stage currentStage = (Stage) buttonManageProduct.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/sale-add-view.fxml", "Cadastrar Venda", currentStage);
    }

    public void handleDeleteSale(){
        int saleId = tableSales.getSelectionModel().getSelectedItem().getSaleId();
        db.deleteSale(saleId);

        loadSales();
    }

    public void applyDateFilter(){
        // Reseta a ChoiceBox quando usar filtro de data
        choiceBoxSale.setValue("Todas as vendas");

        LocalDate fromDate = datePickerFrom.getValue();
        LocalDate toDate = datePickerTo.getValue();

        // Se ambos os campos estão preenchidos
        if (fromDate != null && toDate != null) {
            // Garantir que a data final seja maior ou igual à data inicial
            if (!fromDate.isAfter(toDate)) {
                loadSalesByDateRange(fromDate.toString(), toDate.toString());
                labelDateError.setText("");
            } else {
                // Mostrar mensagem de erro se datas estiverem invertidas
                showAlert("Erro", "A data final deve ser maior ou igual à data inicial.");
                labelDateError.setText("Preencha as datas corretamente");
            }
        } else if (fromDate != null || toDate != null) {
            // Se apenas um campo está preenchido
            //showAlert("Atenção", "Preencha ambas as datas para filtrar.");
            labelDateError.setText("Preencha ambos as datas para filtrar");
        } else {
            // Se nenhum campo está preenchido, carrega todas as vendas
            labelDateError.setText("");
            loadSales();
        }
    }

    // Filtro por ChoiceBox
    public void applyChoiceBoxFilter(){
        // Reseta os DatePickers quando usar filtro da ChoiceBox
        datePickerFrom.setValue(null);
        datePickerTo.setValue(null);
        labelDateError.setText("");

        String selectedOption = choiceBoxSale.getValue();
        if (selectedOption != null) {
            switch (selectedOption) {
                case "Vendas do dia":
                    loadSalesOfToday();
                    break;
                case "Vendas pendentes":
                    loadPendingSales();
                    break;
                case "Todas as vendas":
                    loadSales();
                    break;
                default:
                    loadSales();
                    break;
            }
        }
    }

    // Métodos de carregamento específicos
    public void loadSalesByDateRange(String fromDate, String toDate) {
        System.out.println("Carregando vendas entre as datas");
        List<Sale> sales = db.getSalesByDateRange(fromDate, toDate);
        ObservableList<Sale> observableList = FXCollections.observableArrayList(sales);
        tableSales.setItems(observableList);
    }

    public void loadSalesOfToday() {
        System.out.println("Carregando vendas do dia");
        List<Sale> sales = db.getSalesOfToday();
        ObservableList<Sale> observableList = FXCollections.observableArrayList(sales);
        tableSales.setItems(observableList);
    }

    public void loadPendingSales() {
        System.out.println("Carregando vendas pendentes");
        List<Sale> sales = db.getPendingSales();
        ObservableList<Sale> observableList = FXCollections.observableArrayList(sales);
        tableSales.setItems(observableList);
    }

    // Método auxiliar para mostrar alertas
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void startProductMenu(){
        Stage currentStage = (Stage) buttonManageProduct.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/product-menu-view.fxml", "Menu de produtos", currentStage);

    }
    public void startPaymentMenu(){
        Stage currentStage = (Stage) buttonManageProduct.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/payment-menu-view.fxml", "Menu de formas de pagamento", currentStage);
    }

    private void applyEffects(){
        Label[] labelItens = new Label[]{
                labelUserName
        };
        for (Label label : labelItens){
            ButtonEffects.applyHoverEffect(label, 1.01, 1.01);
        }

        Button[] buttonItens = new Button[]{
                buttonManageUsers, buttonManageSale, buttonDeleteSale, buttonManageProduct, buttonAddSale, buttonManagePayment
        };
        for (Button button : buttonItens){
            ButtonEffects.applyHoverEffect(button, 1.01, 1.01);
            ButtonEffects.applyPressEffect(button, 0.95, 0.95);
        }
    }

}
