package com.example.rui.server;

import java.io.Serializable;

public class RoomHelper implements Serializable {

    private static final long serialVersionUID = 1L;
	
	public boolean isLight() {
		return light;
	}
	public void setLight(boolean light) {
		this.light = light;
	}
	public boolean isArcondicionado() {
		return arcondicionado;
	}
	public void setArcondicionado(boolean arcondicionado) {
		this.arcondicionado = arcondicionado;
	}
	public boolean isTv() {
		return tv;
	}
	public void setTv(boolean tv) {
		this.tv = tv;
	}
	public boolean isWindow() {
		return window;
	}
	public void setWindow(boolean window) {
		this.window = window;
	}
    public int getTemperatureArCond() {
        return temperatureArCond;
    }
    public void setTemperatureArCond(int temperatureArCond) {
        this.temperatureArCond = temperatureArCond;
    }

	public RoomHelper(boolean light, boolean arcondicionado, boolean tv,
                      boolean window, int temperatureArCond) {
		this.light = light;
		this.arcondicionado = arcondicionado;
		this.tv = tv;
		this.window = window;
        this.temperatureArCond = temperatureArCond;
	}
	
	private boolean light;
	private boolean arcondicionado = false;
	private boolean tv; 
	private boolean window;
    private int temperatureArCond;

}
