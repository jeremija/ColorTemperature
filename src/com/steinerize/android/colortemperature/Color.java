package com.steinerize.android.colortemperature;


public class Color {
	
	private float colorTemp;
	private int darkness;
	
	public Color setColorTemp(float colorTemp) {
		if (colorTemp < 1000) colorTemp = 1000;
		if (colorTemp > 40000) colorTemp = 40000;
		
		this.colorTemp = colorTemp;
		return this;
	}
	
	public Color setDarkness(int darkness) {
		this.darkness = darkness;
		return this;
	}
	
	public float getColorTemp() {
		return colorTemp;
	}
	
	public int getDarkness() {
		return darkness;
	}
	
	private int limit(int num) {
		return num < 0 ? 0 : num > 255 ? 255: num;
	}
	
	protected int getRedFromKelvin() {
		float temp = this.colorTemp / 100;
		if (temp <= 66) return 255;
		
		int red = (int) (329.698727446 * Math.pow(temp - 60, -0.1332047592));
		return limit(red);
	}
	
	protected int getGreenFromKelvin() {
		int green;
		float temp = this.colorTemp / 100;
		if (temp <= 66) {
			green = (int) (99.4708025861 * Math.log(temp) - 161.1195681661);
		}
		else {
			green =  (int) (288.1221695283 * Math.pow(temp - 60, -0.0755148492));
		}
		
		return limit(green);
	}
	
	protected int getBlueFromKelvin() {
		float temp = this.colorTemp / 100;
		if (temp >= 66) return 255;
		if (temp <= 19) return 0;
		int blue = (int) (138.5177312231 * Math.log(temp - 10) - 305.0447927307);
		return limit(blue);
	}
	
	protected int calculateDarkenedColor(int color) {
		float factor = 1 - ((float) darkness) / 100;
		return (int) (factor * color);
	}
	
	public int getRed() {
		int red = getRedFromKelvin();
		return calculateDarkenedColor(red);
	}
	
	public int getGreen() {
		int green = getGreenFromKelvin();
		return calculateDarkenedColor(green);
	}
	
	public int getBlue() {
		int blue = getBlueFromKelvin();
		return calculateDarkenedColor(blue);
	}
	
}
