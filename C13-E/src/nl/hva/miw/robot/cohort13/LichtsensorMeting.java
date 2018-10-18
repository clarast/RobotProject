package nl.hva.miw.robot.cohort13;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class LichtsensorMeting {

	private float intensiteit;
	private float kleur;
	
	// initialiseer sensor
	Brick brick = BrickFinder.getDefault();
	Port s1 = brick.getPort("S1");
	EV3ColorSensor lichtSensor = new EV3ColorSensor(s1);
	
	public void meetIntensiteit() {
		// zet sensor op RedMode (type meting voor lichtintensiteit)
		SampleProvider redMode = lichtSensor.getRedMode();
		// maak float array aan, deze is nodig om resultaat in op te slaan
		float[] sample = new float[redMode.sampleSize()];
		// haal een meting op en zet deze in de eerste index van de gemaakte sample array
		redMode.fetchSample(sample, 0);
		// zet attribuut op laatste metingswaarde
		this.intensiteit = sample[0];
	}

	public float getIntensiteit() {
		return intensiteit;
	}

	public void meetKleur() {
		// zet sensor op RedMode (type meting voor kleurmeting)
		SampleProvider colorId = lichtSensor.getColorIDMode();
		// maak float array aan, deze is nodig om resultaat in op te slaan
		float[] sample = new float[colorId.sampleSize()];
		// haal een sample op
		colorId.fetchSample(sample, 0);
		// zet attribuut op laatste metingswaarde
		this.kleur = sample[0];

		// ID 0: Undefined
		// ID 1: Black
		// ID 2: Blue
		// ID 3: Green
		// ID 4: Yellow
		// ID 5: Red
		// ID 6: White
		// bron: http://www.aplu.ch/classdoc/ev3jliba/ch/aplu/ev3/ColorSensor.html

	}

	public float getKleur() {
		return kleur;
	}

}
