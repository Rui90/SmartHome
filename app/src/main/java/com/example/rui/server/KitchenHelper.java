package com.example.rui.server;

import java.io.Serializable;

public class KitchenHelper implements Serializable {
	
	private boolean light;
	private boolean window;
    private boolean microwave;
    private boolean forno;
    private int tempForno;

    private static final long serialVersionUID = 1L;
	
	public KitchenHelper(boolean light,
                         boolean window, boolean microwave, boolean forno, int tempForno) {
		super();
		this.light = light;
		this.window = window;
        this.microwave = microwave;
        this.forno = forno;
        this.tempForno = tempForno;
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

    public boolean isMicrowave() {
        return microwave;
    }
    public void setMicrowave(boolean microwave) {
        this.microwave = microwave;
    }

    public boolean isForno() {
        return forno;
    }
    public void setForno(boolean forno) {
        this.forno = forno;
    }

    public void setTempForno(int tempForno) { this.tempForno = tempForno; }
    public int getTempForno() { return tempForno; }

}
