package com.wdfeww.docgl.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("token")
    @Expose
    public String token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Login{" +
                "user=" + user.toString() +
                ", token='" + token + '\'' +
                '}';
    }
}