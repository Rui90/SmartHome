package com.example.rui.server;

/**
 * Created by Rui on 13/10/2014.
 */
public class Perfil {

    private String name_perfil;
    private boolean light_perfil;
    private boolean window_perfil;

    public Perfil (String name_perfil, boolean light_perfil, boolean window_perfil) {
        this.name_perfil = name_perfil;
        this.light_perfil = light_perfil;
        this.window_perfil = window_perfil;
    }

    public String getName_perfil() {
        return this.name_perfil;
    }

    public boolean getLight_Perfil() {
        return this.light_perfil;
    }

    public boolean getValue() {
        return this.window_perfil;
    }

    public void setNamePerfil(String perfil) {
        this.name_perfil = perfil;
    }

    public void setWindow_perfil(boolean window_perfil) {
        this.window_perfil = window_perfil;
    }

    public void setLight_perfil(boolean value){
        this.light_perfil = value;
    }

    public void light(boolean onOff){
        this.light_perfil = onOff;
    }

}
