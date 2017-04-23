package com.docgl.api;

import java.security.Principal;
import java.util.Objects;

/**
 * Created by Martin on 20.4.2017.
 */
public class LoggedUser implements Principal {
    private String role;
    private int id;

    public LoggedUser(String role, int id) {
        this.role = role;
        this.id = id;
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