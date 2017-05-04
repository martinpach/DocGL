package com.docgl;

import com.docgl.enums.UserType;

import javax.ws.rs.NotAuthorizedException;

/**
 * Created by Martin on 20.4.2017.
 */
public class Authorizer {
    private int id;
    private UserType role;

    public Authorizer() {
    }

    /**
     * Check if logged user has required role
     * @param userRole actual role from token
     * @param roles expected roles
     */
    public void checkAuthorization(UserType userRole, UserType[] roles){
        this.role = userRole;
        for(UserType role : roles) {
            if (this.role.equals(role)) {
                return;
            }
        }
        throw new NotAuthorizedException("Don't have permission to do that!");
    }

    public void checkAuthorization(UserType userRole, UserType role){
        this.role = userRole;
        if(!this.role.equals(role)){
            throw new NotAuthorizedException("Don't have permission to do that!");
        }
    }

    /**
     * Check if logged user is not trying to see other users information
     * @param userId actual userId from token
     * @param id expected id
     */
    public void checkAuthentication(int userId, int id){
        this.id = userId;
        if(this.id != id){
            throw new NotAuthorizedException("Don't have authentication to do that!");
        }
    }
}