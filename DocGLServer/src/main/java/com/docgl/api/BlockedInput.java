package com.docgl.api;

/**
 * Created by Rasťo on 30.4.2017.
 */
public class BlockedInput {
    private boolean blocked;

    public BlockedInput() {
    }

    public BlockedInput(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
