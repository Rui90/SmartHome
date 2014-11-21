package com.example.rui.server;
public class RoomHelper {
	
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
	public RoomHelper(boolean light, boolean arcondicionado, boolean tv,
                      boolean window) {
		this.light = light;
		this.arcondicionado = arcondicionado;
		this.tv = tv;
		this.window = window;
	}
	
	private boolean light;
	private boolean arcondicionado;
	private boolean tv; 
	private boolean window;		

}
