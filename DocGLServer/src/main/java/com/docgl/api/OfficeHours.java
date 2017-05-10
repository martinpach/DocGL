package com.docgl.api;

import com.docgl.entities.WorkingHours;

import java.util.List;

/**
 * Created by Rasťo on 10.5.2017.
 */
public class OfficeHours {
    //morning hours
    private String from;
    private String to;
    //afternoon hours
    private String from2;
    private String to2;

    public OfficeHours() {
        this.from = null;
        this.to = null;
        this.from2 = null;
        this.to2 = null;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom2() {
        return from2;
    }

    public void setFrom2(String from2) {
        this.from2 = from2;
    }

    public String getTo2() {
        return to2;
    }

    public void setTo2(String to2) {
        this.to2 = to2;
    }


    /**
     * This function set Doctors office hours according to dey of week and Doctors working hours.
     * @param dayOfWeek number of day in the week (monday is 1, etc...)
     * @param workingHoursList list of Doctors working hours
     */
    public void setOfficeHours(int dayOfWeek, List<WorkingHours> workingHoursList) {
        switch (dayOfWeek) {
            case 1: this.from = workingHoursList.get(0).getMondayFrom();
                    this.to = workingHoursList.get(0).getMondayTo();
                    if (workingHoursList.get(1).getMondayFrom() != null)
                        this.from2 = workingHoursList.get(1).getMondayFrom();
                        this.to2 = workingHoursList.get(1).getMondayTo();
                    break;
            case 2: this.from = workingHoursList.get(0).getTuesdayFrom();
                    this.to = workingHoursList.get(0).getTuesdayTo();
                    if (workingHoursList.get(1).getTuesdayFrom() != null)
                        this.from2 = workingHoursList.get(1).getTuesdayFrom();
                        this.to2 = workingHoursList.get(1).getTuesdayTo();
                    break;
            case 3: this.from = workingHoursList.get(0).getWednesdayFrom();
                    this.to = workingHoursList.get(0).getWednesdayTo();
                    if (workingHoursList.get(1).getWednesdayFrom() != null)
                        this.from2 = workingHoursList.get(1).getWednesdayFrom();
                        this.to2 = workingHoursList.get(1).getWednesdayTo();
                    break;
            case 4: this.from = workingHoursList.get(0).getThursdayFrom();
                    this.to = workingHoursList.get(0).getThursdayTo();
                    if (workingHoursList.get(1).getThursdayFrom() != null)
                        this.from2 = workingHoursList.get(1).getThursdayFrom();
                        this.to2 = workingHoursList.get(1).getThursdayTo();
                    break;
            case 5: this.from = workingHoursList.get(0).getFridayFrom();
                    this.to = workingHoursList.get(0).getFridayTo();
                    if (workingHoursList.get(1).getFridayFrom() != null)
                        this.from2 = workingHoursList.get(1).getFridayFrom();
                        this.to2 = workingHoursList.get(1).getFridayTo();
                    break;
        }
    }
}
