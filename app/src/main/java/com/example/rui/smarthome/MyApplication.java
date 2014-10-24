package com.example.rui.smarthome;

import android.app.Application;

import java.util.List;

/**
 * Created by Rui on 24/10/2014.
 */
public class MyApplication extends Application {

    private boolean roomACState;
    private int roomACValue;
    List<AccessPoint> accesspoints;

    public MyApplication(List<AccessPoint> accessPoints) {
        this.accesspoints = accessPoints;
    }

    public List<AccessPoint> getAccessPoints() {
        return accesspoints;
    }

    public void setAccessPoints(List<AccessPoint> accessPoints) {
        this.accesspoints = accessPoints;
    }

    public int getSize(){
        return accesspoints.size();
    }

    public AccessPoint getIndex(int i){
        return accesspoints.get(i);
    }

    public MyApplication(AccessPoint point1, AccessPoint point2, AccessPoint point3, AccessPoint point4) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
        this.point4 = point4;
    }

    private AccessPoint point1;
    private AccessPoint point2;
    private AccessPoint point3;
    private AccessPoint point4;

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

    public AccessPoint getPoint4() {
        return point4;
    }

    public void setPoint4(AccessPoint point4) {
        this.point4 = point4;
    }

    public AccessPoint getPoint1() {
        return point1;
    }

    public void setPoint1(AccessPoint point1) {
        this.point1 = point1;
    }

    public AccessPoint getPoint2() {
        return point2;
    }

    public void setPoint2(AccessPoint point2) {
        this.point2 = point2;
    }

    public AccessPoint getPoint3() {
        return point3;
    }

    public void setPoint3(AccessPoint point3) {
        this.point3 = point3;
    }


}