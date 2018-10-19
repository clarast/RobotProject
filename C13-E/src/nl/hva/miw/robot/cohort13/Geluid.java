package nl.hva.miw.robot.cohort13;

import java.io.File;

import lejos.hardware.Sound;

public class Geluid implements Runnable {

	@Override
	public void run() {
		
		Sound.playSample(new File("liedjeparcours.wav"), Sound.VOL_MAX);		
	}
	
	


}


