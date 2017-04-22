package com.DocGL;

/**
 * Created by Martin on 20.4.2017.
 */
public class Authorizer {
    private int id;
    private String role;

    public Authorizer() {
    }

    public boolean hasPermission(String key, String role){
        this.role = key.split(",")[1];
        return this.role.equals(role);
    }

    public boolean isAuthenticated(String key, int id){
        this.id = Integer.parseInt(key.split(",")[0]);
        return this.id == id;
    }
}