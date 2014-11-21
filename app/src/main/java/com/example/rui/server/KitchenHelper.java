package com.example.rui.server;

public class KitchenHelper {
	
	private boolean light;
	private boolean window;	
	
	public KitchenHelper(boolean light,
                         boolean window) {
		super();
		this.light = light;
		this.window = window;
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

}
