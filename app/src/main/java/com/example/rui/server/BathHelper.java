package com.example.rui.server;
public class BathHelper{
	
	private boolean light;
    private int quantity;
    private int temperature;

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
