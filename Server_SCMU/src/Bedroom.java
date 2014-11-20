
public class Bedroom {
	
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
	
	public void setModo(int modo) {
		this.modo = modo; 
	}
	
	public int getModo(int modo) {
		return this.modo; 
	}
	public Bedroom(boolean light, boolean window, int modo) {
		this.light = light;
		this.modo = modo;
		this.window = window;
	}
	
	private boolean light;
	private boolean window;	
	private int modo; 

}
