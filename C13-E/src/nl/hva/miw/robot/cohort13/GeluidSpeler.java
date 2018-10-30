package nl.hva.miw.robot.cohort13;

import java.io.File;
import lejos.hardware.Sound;

/**
 * In deze klasse staan de methods die .wav files afspelen
 */

public class GeluidSpeler {

	public void speelWelkomstBlaf() {
		Sound.playSample(new File("dog_bark6.wav"), Sound.VOL_MAX);

	}

	public void speelBlaf3x() {
		Sound.playSample(new File("blaf3x.wav"), Sound.VOL_MAX);
	}

	public void speelDeuntje() {
		Sound.beepSequenceUp();

	}
}
