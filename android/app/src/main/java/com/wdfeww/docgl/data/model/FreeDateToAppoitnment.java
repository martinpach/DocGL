package com.wdfeww.docgl.data.model;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FreeDateToAppoitnment {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("times")
    @Expose
    private List<String> times = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

}