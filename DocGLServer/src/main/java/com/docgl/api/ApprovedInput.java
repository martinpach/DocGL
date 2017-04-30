package com.docgl.api;

/**
 * Created by Rasťo on 30.4.2017.
 */
public class ApprovedInput {
    private boolean approved;

    public ApprovedInput(){
    }

    public ApprovedInput(boolean approved) {
        this.approved = approved;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
