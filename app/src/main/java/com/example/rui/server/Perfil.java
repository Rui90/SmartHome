package com.example.rui.server;

import java.io.Serializable;

/**
 * Created by Rui on 13/10/2014.
 */
public class Perfil implements Serializable {

    private String name_perfil;
    private boolean light_perfil;
    private boolean window_perfil;

    private static final long serialVersionUID = 1L;

    public Perfil (String name_perfil, boolean window_perfil, boolean light_perfil) {
        this.name_perfil = name_perfil;
        this.window_perfil = window_perfil;
        this.light_perfil = light_perfil;
    }

    public String getName_perfil() {
        return this.name_perfil;
    }

    public boolean getLight_Perfil() {
        return this.light_perfil;
    }

    public boolean getWindow_perfil() {
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
