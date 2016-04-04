package org.omilab.robot.mind;

public class ExpressionModelMind {
	private short[][] display;	
	private String LCD;
	private int NoiseDuration;
	
	private short displayColor;
	private short[] LCDColor;
	private boolean setDisplay;
	private boolean setLCD;
	private boolean setNoise;
	private boolean flippedDisplay;
	
	public static final short DISPLAYOFF = 0;
	public static final short DISPLAYGREEN = 1;
	public static final short DISPLAYRED = 2;
	public static final short DISPLAYYELLOW = 3;
	public static final short DISPLAYBICOLOR = 4;
	
	public static final short[][] DISPLAYFORWARD = {
		{0,0,0,1,1,0,0,0},
		{0,0,1,1,1,1,0,0},
		{0,1,1,1,1,1,1,0},
		{1,1,1,1,1,1,1,1},
		{1,0,0,1,1,0,0,1},
		{0,0,0,1,1,0,0,0},
		{0,0,0,1,1,0,0,0},
		{0,0,0,1,1,0,0,0}};
	public static final short[][] DISPLAYARROWLEFT = {
		{0,0,0,1,1,0,0,0},
		{0,0,1,1,0,0,0,0},
		{0,1,1,1,0,0,0,0},
		{1,1,1,1,1,1,1,1},
		{1,1,1,1,1,1,1,1},
		{0,1,1,1,0,0,0,0},
		{0,0,1,1,0,0,0,0},
		{0,0,0,1,1,0,0,0}};
	
	public static final short[][] DISPLAYARROWRIGHT = {
		{0,0,0,1,1,0,0,0},
		{0,0,0,0,1,1,0,0},
		{0,0,0,0,1,1,1,0},
		{1,1,1,1,1,1,1,1},
		{1,1,1,1,1,1,1,1},
		{0,0,0,0,1,1,1,0},
		{0,0,0,0,1,1,0,0},
		{0,0,0,1,1,0,0,0}};

	public static final short[][] DISPLAYBACKWARD = {
		{0,0,0,1,1,0,0,0},
		{0,0,0,1,1,0,0,0},
		{0,0,0,1,1,0,0,0},
		{1,0,0,1,1,0,0,1},
		{1,1,1,1,1,1,1,1},
		{0,1,1,1,1,1,1,0},
		{0,0,1,1,1,1,0,0},
		{0,0,0,1,1,0,0,0}};
	public static final short[][] DISPLAYSTOP = {
		{1,0,0,0,0,0,0,1},
		{0,1,0,0,0,0,1,0},
		{0,0,1,0,0,1,0,0},
		{0,0,0,1,1,0,0,0},
		{0,0,0,1,1,0,0,0},
		{0,0,1,0,0,1,0,0},
		{0,1,0,0,0,0,1,0},
		{1,0,0,0,0,0,0,1}};
	
	public static final short[][] DISPLAYBLANK = {
		{0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0}};
	
	public static final short[] LCDRED = {255,0,0};
	public static final short[] LCDGREEN = {0,255,0};
	public static final short[] LCDBLUE = {255,0,255};
	public static final short[] LCDYELLOW = {255,255,0};
	public static final short[] LCDCYAN = {0,255,255};
	public static final short[] LCDMAGENTA = {255,0,255};
	public static final short[] LCDWHITE = {255,255,255};

	public ExpressionModelMind() {
		setDisplay(DISPLAYBLANK);
		setDisplayColor(DISPLAYOFF);
		setFlippedDisplay(false);
		setLCD("");
		setLCDColor(LCDCYAN);
		setSetLCD(false);
		setSetDisplay(false);
		setSetNoise(false);
	}
	
	public short[][] getDisplay() {
		return display;
	}
	
	public short getDisplayColor() {
		return displayColor;
	}
	
	public String getLCD() {
		return LCD;
	}
	public short[] getLCDColor() {
		return LCDColor;
	}
	public boolean isFlippedDisplay() {
		return flippedDisplay;
	}
	public boolean isSetDisplay() {
		return setDisplay;
	}
	public boolean isSetLCD() {
		return setLCD;
	}
	public void setDisplay(short[][] display) {
		this.display = display;
	}
	public void setDisplayColor(short displayColor) {
		this.displayColor = displayColor;
	}
	public void setFlippedDisplay(boolean flippedDisplay) {
		this.flippedDisplay = flippedDisplay;
	}
	public void setLCD(String LCD) {
		this.LCD = LCD;
	}
	public void setLCDColor(short[] LCDColor) {
		this.LCDColor = LCDColor;
	}
	public void setSetDisplay(boolean setDisplay) {
		this.setDisplay = setDisplay;
	}
	public void setSetLCD(boolean setLCD) {
		this.setLCD = setLCD;
	}
	public int getNoiseDuration() {
		return NoiseDuration;
	}
	public void setNoiseDuration(int NoiseDuration) {
		this.NoiseDuration = NoiseDuration;
	}
	public boolean isSetNoise() {
		return setNoise;
	}
	public void setSetNoise(boolean setNoise) {
		this.setNoise = setNoise;
	}
}
