package com.docgl.api;

/**
 * Created by Solanid on 11.4.2017.
 */
public class LoginInput {
    private String userName;
    private String password;
    //private String subject;

    public LoginInput(){

    }

    public LoginInput(String userName, String subject){
        this.userName=userName;
        //this.subject=subject;

    }



    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

   // public String getSubject(){return subject;}

}
