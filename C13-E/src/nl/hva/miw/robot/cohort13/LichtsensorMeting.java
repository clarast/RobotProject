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
		// neem meting in RedMode
		SampleProvider redMode = lichtSensor.getRedMode();
		float[] sample = new float[lichtSensor.sampleSize()];
		redMode.fetchSample(sample, 0);
		// sluit sensor af
		lichtSensor.close();
		this.intensiteit = sample[0];
	
	}

	public float getIntensiteit() {
		return intensiteit;
	}
	
	public void meetKleur() {
		SampleProvider colorId = lichtSensor.getColorIDMode();
		float[] sample = new float[lichtSensor.sampleSize()];
		colorId.fetchSample(sample, 0);
		// sluit sensor af
		lichtSensor.close();
		this.kleur = sample[0];
	}

	public float getKleur() {
		return kleur;
	}
	
	
	
	
	
}
