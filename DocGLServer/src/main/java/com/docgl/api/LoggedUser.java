package com.docgl.api;

import com.docgl.enums.UserType;

import java.security.Principal;
import java.util.Objects;

/**
 * Created by Martin on 20.4.2017.
 * Class to represent loggedUser
 * Purpose: for authorization and authentication stuff
 */
public class LoggedUser implements Principal {
    private UserType role;
    private int id;

    public LoggedUser(UserType role, int id) {
        this.role = role;
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public UserType getUserType(){
        return role;
    }

    @Override
    public String getName() {
        return id + "," + role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LoggedUser loggedUser = (LoggedUser) o;
        return Objects.equals(role, loggedUser.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }
}
