package com.wdfeww.docgl.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Appointment implements Parcelable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("doctor")
    @Expose
    private Doctor doctor;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("patientFirstName")
    @Expose
    private String patientFirstName;
    @SerializedName("patientLastName")
    @Expose
    private String patientLastName;
    @SerializedName("canceled")
    @Expose
    private Boolean canceled;

    protected Appointment(Parcel in) {
        note = in.readString();
        doctor = in.readParcelable(Doctor.class.getClassLoader());
        time = in.readString();
        date = in.readString();
        patientFirstName = in.readString();
        patientLastName = in.readString();
        id = in.readInt();
    }

    public static final Creator<Appointment> CREATOR = new Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel in) {
            return new Appointment(in);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public Boolean getCanceled() {
        return canceled;
    }

    public void setCanceled(Boolean canceled) {
        this.canceled = canceled;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(note);
        dest.writeParcelable(doctor, flags);
        dest.writeString(time);
        dest.writeString(date);
        dest.writeString(patientFirstName);
        dest.writeString(patientLastName);
        dest.writeInt(id);
    }
}