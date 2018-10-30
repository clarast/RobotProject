package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;

public class KopLampen implements Runnable {

	private Thread thread;

	public void setGreenBlinkingLight() {
		Button.LEDPattern(4);
	}
	
	

	
	
	

	
	//thread
	@Override
	public void run() {

		setGreenBlinkingLight();
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

}
