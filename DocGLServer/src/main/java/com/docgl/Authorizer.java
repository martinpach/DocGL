package com.docgl;

import javax.ws.rs.NotAuthorizedException;

/**
 * Created by Martin on 20.4.2017.
 */
public class Authorizer {
    private int id;
    private String role;

    public Authorizer() {
    }

    public void checkAuthorization(String key, UserType[] roles){
        this.role = key.split(",")[1];
        for(UserType role : roles) {
            if (this.role.equals(role.getValue())) {
                return;
            }
        }
        throw new NotAuthorizedException("Don't have permission to do that!");
    }

    public void checkAuthorization(String key, UserType role){
        this.role = key.split(",")[1];
        if(!this.role.equals(role.getValue())){
            throw new NotAuthorizedException("Don't have permission to do that!");
        }
    }

    public void checkAuthentication(String key, int id){
        this.id = Integer.parseInt(key.split(",")[0]);
        if(this.id != id){
            throw new NotAuthorizedException("Don't have authentication to do that!");
        }
    }
}