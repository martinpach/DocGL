package com.docgl.api;

import com.docgl.entities.Admin;

/**
 * Created by Martin on 15.4.2017.
 */
public class AdminRepresentation {
    private Admin admin;
    private String token;

    /**
     * This class represents response after successful login
     * @param admin represents logged admin entity object
     * @param token represents generated token
     */
    public AdminRepresentation(Admin admin, String token) {
        this.admin = admin;
        this.token = token;
    }

    public Admin getAdmin() {
        return admin;
    }

    public String getToken() {
        return token;
    }
}
