package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;
public class KopLampen extends Thread {

	private Thread thread;
	
	public void setGreenBlinkingLight() {
		Button.LEDPattern(4);
	}
	
	@Override
	public void run() {
		
			setGreenBlinkingLight();
		
		
	}
	
		public void start () {
			if (thread == null) {
				thread = new Thread (this);
				thread.start ();
			}
	}
		
	public void groenConstant() {
		Button.LEDPattern(1);
	}
	
	public void roodConstant() {
		Button.LEDPattern(2);
	}
	
	public void oranjeConstant() {
		Button.LEDPattern(3);
	}
	
	public void groenKnipper() {
		Button.LEDPattern(4);
	}
	
	public void roodKnipper() {
		Button.LEDPattern(5);
	}
	
	public void oranjeKnipper() {
		Button.LEDPattern(6);
	}
	
	public void groenPulse() {
		Button.LEDPattern(7);
	}
	
	public void roodPulse() {
		Button.LEDPattern(8);
	}
	
	public void oranjePulse() {
		Button.LEDPattern(9);
	}
	
	
	
	
}
