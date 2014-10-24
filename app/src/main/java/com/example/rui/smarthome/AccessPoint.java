package com.example.rui.smarthome;

import android.net.wifi.ScanResult;

/**
 * Created by Alexandre on 24-10-2014.
 */
public class AccessPoint {

    private String scanResult;
    private double distance;

    public AccessPoint(String scanR, double distance){
        scanR = scanResult;
        distance = distance;
    }

    public String getScanResult(){
        return scanResult;
    }

    public double getDistance(){
        return distance;
    }

    public void setScanResult(String scan){
        scanResult = scan;
    }

    public void setDistance(double dist){
        distance = dist;
    }
}
