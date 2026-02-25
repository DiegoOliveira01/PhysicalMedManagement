package com.physicalmed.physicalmedmanagement.utils;

public class SessionManager {

    private static SessionManager instance;
    private int productId;
    private Boolean paymentIsSingle;
    private String paymentName;
    private int saleId;

    private SessionManager() {}

    public static SessionManager getInstance(){
        if (instance == null){
            instance = new SessionManager();
        }
        return instance;
    }

    public int getProductId(){
        return productId;
    }

    public void setProductId(int productId){
        this.productId = productId;
    }

    public Boolean getPaymentIsSingle() {
        return paymentIsSingle;
    }

    public void setPaymentIsSingle(Boolean paymentIsSingle) {
        this.paymentIsSingle = paymentIsSingle;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public int getSaleId() {return saleId; }

    public void setSaleId(int saleId) {this.saleId = saleId; }
}
