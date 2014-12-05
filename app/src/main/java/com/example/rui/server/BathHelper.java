package com.example.rui.server;

import java.io.Serializable;

public class BathHelper implements Serializable {
	
	private boolean light;
    private int quantity;
    private int temperature;

    private static final long serialVersionUID = 1L;

	public BathHelper(boolean light, int quantity, int temperature) {
		this.light = light;
        this.quantity = quantity;
        this.temperature = temperature;
	}
	
	public void setLight(boolean light) {
		this.light = light;
	}

    public boolean isLight() {
        return light;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

}
