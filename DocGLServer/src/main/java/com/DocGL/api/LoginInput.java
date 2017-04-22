package com.docgl.api;

/**
 * Created by Solanid on 11.4.2017.
 */
public class LoginInput {
    private String username;
    private String password="";
    private String subject;

    public LoginInput(){

    }

    public LoginInput(String username, String subject){
        this.username=username;
        this.subject=subject;

    }



    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSubject(){return subject;}

}
