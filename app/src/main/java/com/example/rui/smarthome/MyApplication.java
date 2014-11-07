package com.example.rui.smarthome;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    public MyApplication() {}

    //access points
    private boolean mode = false;
    private List<AccessPoint> accesspoints = new ArrayList<AccessPoint>();

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


   // ROOM
    private boolean roomACState;
    private int roomACValue;
    private List<Perfil> perfis = new ArrayList<Perfil>();

    public MyApplication(boolean state, int value) {
        this.roomACState = state;
        this.roomACValue = value;
    }

    public void addPerfil(Perfil p) {
        perfis.add(p);
    }

    public void removePerfil(int pos) {
        perfis.remove(pos);
    }

    public int getPerfisSize() {
        return perfis.size();
    }

    public Perfil getPerfil(int pos){
       return perfis.get(pos);
    }

    public List<Perfil> getPerfis() {
        return this.perfis;
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

    //WC
    private int wc_water;
    private int wc_temp;
    public MyApplication(int value) {
        this.wc_water = value;
    }

    public MyApplication(int value, String x) {
        this.wc_temp = value;
    }

    public int getWCWater() { return wc_water; }

    public void setWCWater(int value) { this.wc_water = value; }

    public int getWCTemp() { return wc_temp; }

    public void setWCTemp(int value) { this.wc_temp = value; }

    //kitchen
    private int kitchen_forno;
    private boolean forno;
    private boolean microwave;
    private int micro_pos;

    public MyApplication(int value, boolean status, String y) {
        this.kitchen_forno = value;
        this.forno = status;
    }

    public MyApplication(boolean microwave, int micro_pos, String x, String y){
        this.microwave = microwave;
        this.micro_pos = micro_pos;
    }

    public int getKitchen_forno() { return kitchen_forno; }

    public void setKitchen_forno(int value) { this.kitchen_forno = value; }

    public boolean getForno() {
        return forno;
    }

    public void setForno(boolean status) {
        this.forno = status;
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