package com.example.rui.smarthome;

import android.app.Application;

import com.example.rui.server.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {

    private static final String IP = "192.168.1.100";

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

    public MyApplication(RoomHelper roomHelper) {
        this.roomHelper = roomHelper;
    }

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

    //server
    private int socket;

    public MyApplication(int socket, String x, String y, String z) {
        this.socket = socket;
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

    public int getSocket() {
        return socket;
    }

    public void setSocket(int socket) {
        this.socket = socket;
    }
}