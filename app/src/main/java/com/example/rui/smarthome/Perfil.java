package com.example.rui.smarthome;

/**
 * Created by Rui on 13/10/2014.
 */
public class Perfil {

    private String name_perfil;
    private boolean light_perfil;
    private int value;

    public Perfil (String name_perfil, boolean light_perfil, int value) {
        this.name_perfil = name_perfil;
        this.light_perfil = light_perfil;
        this.value = value;
    }

    public String getName_perfil() {
        return this.name_perfil;
    }

    public boolean getLight_Perfil() {
        return this.light_perfil;
    }

    public int getValue() {
        return this.value;
    }

    public void setNamePerfil(String perfil) {
        this.name_perfil = perfil;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void light(boolean onOff){
        this.light_perfil = onOff;
    }

}
