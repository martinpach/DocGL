package com.DocGL.resources;

import com.DocGL.DB.AdminLoginDAO;

/**
 * Created by D33 on 4/8/2017.
 */
public class AdminResource {
    private AdminLoginDAO adminLoginDAO;
    public AdminResource(AdminLoginDAO adminLoginDAO) {
        this.adminLoginDAO=adminLoginDAO;
    }
}
