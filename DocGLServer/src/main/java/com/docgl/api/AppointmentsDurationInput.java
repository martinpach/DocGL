package com.docgl.api;

/**
 * Created by Rasťo on 7.5.2017.
 */
public class AppointmentsDurationInput {
    private int duration;

    public AppointmentsDurationInput() {
    }

    public AppointmentsDurationInput(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
