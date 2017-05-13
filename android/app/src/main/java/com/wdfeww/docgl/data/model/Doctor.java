package com.wdfeww.docgl.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Doctor implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("specialization")
    @Expose
    private String specialization;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("workplace")
    @Expose
    private String workplace;
    @SerializedName("appointmentsDuration")
    @Expose
    private Integer appointmentsDuration;
    @SerializedName("blocked")
    @Expose
    private Boolean blocked;
    @SerializedName("approved")
    @Expose
    private Boolean approved;
    @SerializedName("dateOfValidity")
    @Expose
    private Object dateOfValidity;
    @SerializedName("workingHoursSet")
    @Expose
    private Boolean workingHoursSet;

    protected Doctor(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        userName = in.readString();
        specialization = in.readString();
        phone = in.readString();
        city = in.readString();
        workplace = in.readString();
        id = in.readInt();
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public Integer getAppointmentsDuration() {
        return appointmentsDuration;
    }

    public void setAppointmentsDuration(Integer appointmentsDuration) {
        this.appointmentsDuration = appointmentsDuration;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Object getDateOfValidity() {
        return dateOfValidity;
    }

    public void setDateOfValidity(Object dateOfValidity) {
        this.dateOfValidity = dateOfValidity;
    }

    public Boolean getWorkingHoursSet() {
        return workingHoursSet;
    }

    public void setWorkingHoursSet(Boolean workingHoursSet) {
        this.workingHoursSet = workingHoursSet;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(userName);
        dest.writeString(specialization);
        dest.writeString(phone);
        dest.writeString(city);
        dest.writeString(workplace);
        dest.writeInt(id);
    }
}