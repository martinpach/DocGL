package com.DocGL.api;

/**
 * Created by Solanid on 11.4.2017.
 */
public class Credentials {
    private String username;
    private String password;

    public Credentials(){

    }

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
