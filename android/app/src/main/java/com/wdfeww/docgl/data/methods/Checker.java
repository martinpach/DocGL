package com.wdfeww.docgl.data.methods;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Checker {
    public static boolean isNameValid(String username) {
        if (username.length() == 0) {
            return false;
        } else {

            return true;
        }
    }

    public static boolean isPasswordValid(String password) {
        String string_pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}";
        Pattern pattern = Pattern.compile(string_pattern);
        Matcher matcher = pattern.matcher(password);
        if (password.length() == 0) {
            return false;
        } else if (!matcher.matches()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isEmailValid(String email) {
        String string_pattern = "(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)";
        Pattern pattern = Pattern.compile(string_pattern);
        Matcher matcher = pattern.matcher(email);
        if (email.length() == 0) {
            return false;
        } else if (!matcher.matches()) {
            return false;
        } else {
            return true;
        }
    }

}
