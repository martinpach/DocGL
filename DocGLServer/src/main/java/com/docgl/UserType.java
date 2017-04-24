package com.docgl;

/**
 * Created by Martin on 24.4.2017.
 */
public enum UserType {
    ADMIN{
        @Override
        public String toString() {
            return "admin";
        }
    },
    PATIENT{
        @Override
        public String toString() {
            return "patient";
        }
    },
    DOCTOR{
        @Override
        public String toString() {
            return "doctor";
        }
    }
}
