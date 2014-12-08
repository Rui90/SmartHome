package com.example.rui.smarthome;

import android.app.Application;

import com.example.rui.server.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {

    private static final String IP = "192.168.0.100";

    public String getIp() {
        return this.IP;
    }
    public MyApplication() {}

    //access points
    private boolean mode = false;
    private LinkedList<AccessPoint> accesspoints = new LinkedList<AccessPoint>();

    public void setMode(boolean b) {
        this.mode = b;
    }
    public boolean getMode() {
        return this.mode;
    }

    public AccessPoint getAccessPoint(int pos) {
        return accesspoints.get(pos);
    }

    public void addAccessPoints(AccessPoint p) {
        this.accesspoints.add(p);
    }

    public int getSize(){
        return accesspoints.size();
    }

    public  LinkedList<AccessPoint> getList(){
        return accesspoints;
    }



    //WC
    public RoomHelper getRoomHelper() {
        return this.roomHelper;
    }

    private BathHelper bathHelper;

    public MyApplication(BathHelper bathHelper) {
        this.bathHelper = bathHelper;
    }

    public BathHelper getBathHelper() {
        return this.bathHelper;
    }

    // SETS

    public void setBathHelper(BathHelper bathHelper) { this.bathHelper = bathHelper; }

    public void setRoomHelper(RoomHelper roomHelper) { this.roomHelper = roomHelper; }

    public void setBedroomHelper(BedroomHelper bedroomHelper) { this.bedroomHelper = bedroomHelper; }

    public void setKitchenHelper(KitchenHelper kitchenHelper) { this.kitchenHelper = kitchenHelper; }

    private BedroomHelper bedroomHelper;

    public MyApplication(BedroomHelper bedroomHelper) {
        this.bedroomHelper = bedroomHelper;
    }

    public BedroomHelper getBedroomHelper() { return this.bedroomHelper; }


    private KitchenHelper kitchenHelper;

    public MyApplication(KitchenHelper kitchenHelper) {
        this.kitchenHelper = kitchenHelper;
    }

    private RoomHelper roomHelper;

    public KitchenHelper getKitchenHelper(){
        return this.kitchenHelper;
    }

    public MyApplication(RoomHelper roomHelper) {
        this.roomHelper = roomHelper;
    }



    //server
    private int socket;

    public MyApplication(int socket, String x, String y, String z) {
        this.socket = socket;
    }



    public int getSocket() {
        return socket;
    }

    public void setSocket(int socket) {
        this.socket = socket;
    }
}