package com.wdfeww.docgl.data.SQLiteDatabase;



public class Notifications {


    private int _id;
    private int _userId;
    private int _isNotificationsEnabled;

    public Notifications() {}

    public Notifications( int  isNotificationsEnabled,int userId ) {


        this._isNotificationsEnabled = isNotificationsEnabled;
        this._userId = userId;

    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_userId() {
        return _userId;
    }

    public void set_userId(int _userId) {
        this._userId = _userId;
    }

    public int is_isNotificationsEnabled() {
        return _isNotificationsEnabled;
    }

    public void set_isNotificationsEnabled(int _isNotificationsEnabled) {
        this._isNotificationsEnabled = _isNotificationsEnabled;
    }
}








