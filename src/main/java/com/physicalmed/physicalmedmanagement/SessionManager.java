package com.physicalmed.physicalmedmanagement;

public class SessionManager {

    private static SessionManager instance;
    private int productId;

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

}
