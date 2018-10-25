package nl.hva.miw.robot.cohort13;

import java.io.File;
import lejos.hardware.Sound;

public class GeluidSpeler extends Thread {
	
	private Thread thread;

	public void speelWelkomstBlaf() {
		Sound.playSample(new File("welkomstblaf.wav"), Sound.VOL_MAX);
	}

	public void speelSnuffel() {
		Sound.playSample(new File("snuffel.wav"), Sound.VOL_MAX);

	}

	public void speelKauwen() {
		Sound.playSample(new File("kauwen.wav"), Sound.VOL_MAX);

	}

	public void speelBlaf1() {
		Sound.playSample(new File("blaf1.wav"), Sound.VOL_MAX);
	}

	public void speelBlaf2() {
		Sound.playSample(new File("blaf2.wav"), Sound.VOL_MAX);
	}

	public void speelBlaf3() {
		Sound.playSample(new File("blaf3.wav"), Sound.VOL_MAX);
	}

	//loop moet uiteindelijk iets anders worden maar dit is een beginnetje
	
	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			speelKauwen();
		}
		
	}
	
		public void start () {
			if (thread == null) {
				thread = new Thread (this);
				thread.start ();
			}
	}
}
