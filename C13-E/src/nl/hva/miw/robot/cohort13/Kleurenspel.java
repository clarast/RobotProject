package nl.hva.miw.robot.cohort13;

import lejos.hardware.Brick;
import lejos.hardware.Button;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Kleurenspel {

	private int[] kleuren = new int[4];
	private EV3ColorSensor kleurSensor;

	public Kleurenspel(Hardware hardware) {
		super();
		this.kleurSensor = hardware.maakLichtsensor();
		this.motorA = hardware.motorA;
		this.motorB = hardware.motorB;
		this.touchSensor = hardware.touchSensor;
		this.scherm = hardware.scherm;
		this.geluidspeler = hardware.geluidspeler;
	}

	public void startKleurenspel() {

		//kleurSensor.close();
		scherm.printOgen();
		Delay.msDelay(2000);

		while (Button.ESCAPE.isUp()) {
			if (isTouched())
				break;
			Delay.msDelay(500);

			// print scherm met tekst "Fikkie wil een koekje!" + afbeelding botje
			// for loop met [4] als voorwaarde (dus neemt 4 metingen)
			for (int meting = 0; meting < kleuren.length; meting++) {
				scherm.printKoekje();
				scherm.plaatsKoekje();
				while (Button.ESCAPE.isUp()) {
					if (isTouched())
						break; }
//				kleuren[meting] = kleurMeting();
				geluidspeler.speelKauwen();
				scherm.koekjeGegeten();
				// na elke meting => kauwgeluid + links/rechts actie + print schermafbeelding
			}
		}
		// na einde for loop => roep methode aan die acties afspeelt (switch)
	}

//	public int kleurMeting() {
//		int kleurNummer;
//		this.kleurSensor = new EV3ColorSensor(portS1);
//		SampleProvider colorMode = kleurSensor.getColorIDMode();
//		float[] sample = new float[colorMode.sampleSize()];
//		colorMode.fetchSample(sample, 0);
//		kleurNummer = (int) sample[0];
//		System.out.println(kleurNummer);
//		return kleurNummer;
//	}

	public boolean isTouched() {
		SampleProvider touch = touchSensor.getTouchMode();
		float[] sample = new float[touch.sampleSize()];

		touch.fetchSample(sample, 0);

		if (sample[0] == 0)
			return false;
		else
			return true;
	}
}
