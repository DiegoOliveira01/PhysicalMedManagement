package com.physicalmed.physicalmedmanagement;

public class UserSession {

    private static UserSession instance;
    private String login;
    private String username;
    private String role;

    private UserSession(){
        // Construtor privado para Singleton
    }

    public static UserSession getInstance(){
        if (instance == null){
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(String login, String username, String role){
        this.login = login;
        this.username = username;
        this.role = role;
    }

    public String getLogin(){
        return login;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void clearSession() {
        instance = null;
    }

}
