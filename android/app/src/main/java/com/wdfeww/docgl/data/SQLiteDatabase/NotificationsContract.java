package com.wdfeww.docgl.data.SQLiteDatabase;

import android.provider.BaseColumns;


public final class NotificationsContract {

    private NotificationsContract() {}

    public static class Notifications implements BaseColumns {

        public static final String TABLE_NAME = "Notifications";
        public static final String COLUMN_NOTIFICATIONS = "notifications";
        public static final String COLUMN_USERID = "UserID";

    }
}









