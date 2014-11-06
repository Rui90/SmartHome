package com.example.rui.smarthome;

import android.app.Application;

import java.util.List;

/**
 * Created by Rui on 24/10/2014.
 */
public class MyApplication extends Application {

    private boolean roomACState;
    private int roomACValue;
    private int wc_water;
    private int wc_temp;
    private int kitchen_forno;
    private boolean forno;
    private boolean microwave;
    private int micro_pos;
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

    public MyApplication(int value) {
        this.wc_water = value;
    }

    public MyApplication(int value, String x) {
        this.wc_temp = value;
    }

    public MyApplication(int value, boolean status, String y) {
        this.kitchen_forno = value;
        this.forno = status;
    }

    public MyApplication(boolean microwave, int micro_pos, String x, String y){
        this.microwave = microwave;
        this.micro_pos = micro_pos;
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

    public int getWCWater() { return wc_water; }

    public void setWCWater(int value) { this.wc_water = value; }

    public int getWCTemp() { return wc_temp; }

    public void setWCTemp(int value) { this.wc_temp = value; }

    public int getKitchen_forno() { return kitchen_forno; }

    public void setKitchen_forno(int value) { this.kitchen_forno = value; }

    public boolean getForno() {
        return forno;
    }

    public void setForno(boolean status) {
        this.forno = status;
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


    public boolean isMicrowave() {
        return microwave;
    }

    public void setMicrowave(boolean microwave) {
        this.microwave = microwave;
    }

    public int getMicro_pos() {
        return micro_pos;
    }

    public void setMicro_pos(int micro_pos) {
        this.micro_pos = micro_pos;
    }
}