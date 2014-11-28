package com.example.rui.server;
public class BathHelper{
	
	private boolean light;

	public BathHelper(boolean light) {
		this.light = light; 
	}
	
	public void setLight(boolean light) {
		this.light = light;
	}
	
	public boolean getLight() {
		return this.light; 
	}
}
