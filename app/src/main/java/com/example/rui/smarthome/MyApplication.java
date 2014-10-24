package com.example.rui.smarthome;

import android.app.Application;

/**
 * Created by Rui on 24/10/2014.
 */
public class MyApplication extends Application {

    public MyApplication() {}

    public MyApplication(boolean state, int value) {
        this.roomACState = state;
        this.roomACValue = value;
    }

    public boolean getRoomACState() {
        return roomACState;
    }

    public void setRoomACState(boolean roomACState) {
        this.roomACState = roomACState;
    }

    public int getRoomACValue() {
        return roomACValue;
    }

    public void setRoomACValue(int roomACValue) {
        this.roomACValue = roomACValue;
    }

    private boolean roomACState;
    private int roomACValue;


}