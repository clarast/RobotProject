package nl.hva.miw.robot.cohort13;

import java.io.File;
import lejos.hardware.Sound;

public class GeluidSpeler extends Thread {
	
	private Thread thread;

	public void speelWelkomstBlaf() {
		Sound.playSample(new File("dog_bark6.wav"), Sound.VOL_MAX);

	}

	public void speelBlaf3x() {
		Sound.playSample(new File("blaf3x.wav"), Sound.VOL_MAX);
	}
	
	public void speelDeuntje() {
		Sound.beepSequenceUp();
//	}
//
//	//loop moet uiteindelijk iets anders worden maar dit is een beginnetje
//	
//	@Override
//	public void run() {
//		for (int i = 0; i < 100; i++) {
//			speelKauwen();
//		}
		
	}
	
		public void start () {
			if (thread == null) {
				thread = new Thread (this);
				thread.start ();
			}
	}
}
