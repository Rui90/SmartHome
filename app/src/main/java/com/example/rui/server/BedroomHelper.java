package com.example.rui.server;

import java.util.LinkedList;

public class BedroomHelper {


    private boolean light;
    private boolean window;
    private int modo;
    private LinkedList<Perfil> perfis;

    public BedroomHelper(boolean light, boolean window, int modo, LinkedList<Perfil> perfis) {
        this.light = light;
        this.modo = modo;
        this.window = window;
        this.perfis = perfis;
    }

    public boolean isLight() {
        return light;
    }
    public void setLight(boolean light) {
        this.light = light;
    }

    public boolean isWindow() {
        return window;
    }
    public void setWindow(boolean window) {
        this.window = window;
    }

    public void setModo(int modo) {
        this.modo = modo;
    }

    public int getModo(int modo) {
        return this.modo;
    }

    public void setPerfis(LinkedList<Perfil> perfis) {
        this.perfis = perfis;
    }

    public LinkedList<Perfil> getPerfis() {
        return this.perfis;
    }

}
