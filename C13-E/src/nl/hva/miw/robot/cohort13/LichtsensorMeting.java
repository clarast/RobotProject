package nl.hva.miw.robot.cohort13;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class LichtsensorMeting {

	private double i;
	private double r;
	private double g;
	private double b;
	private double oudeI;
	private double oudeR;
	private double oudeG;
	private double oudeB;
	private EV3ColorSensor lichtSensor;

	public LichtsensorMeting(Hardware hardware) {
		this.lichtSensor = hardware.getLichtsensor();
	}

	/**
	 *  Stappen in deze methode: 
	 *  1. Zet sensor op RedMode (type meting voor lichtintensiteit).
	 *  2. Maak float array aan, deze is nodig om resultaat in op te slaan.
	 *  3. Haal een meting op en zet deze in de eerste index van de gemaakte sample array.
	 *  4. Cast float naar double en zet attribuut voor intensiteit op laatste metingswaarde.
	 */
	public void meetIntensiteit() {
		SampleProvider intensiteit = lichtSensor.getRedMode();
		float[] sample = new float[intensiteit.sampleSize()];
		intensiteit.fetchSample(sample, 0);
		this.i = (double) sample[0];
	}

	/**
	 *  Stappen in deze methode: 
	 *  1. Zet sensor op getRGBMode (type meting voor RGB meting).
	 *  2. Maak float array aan, deze is nodig om resultaat in op te slaan.
	 *  3. Haal een meting op en zet deze in de eerste index van de gemaakte sample array.
	 *  4. Geef R, G en B door aan de methode afronden(), en zet de waarden die uit die methode terugkomen als waarde voor de attributen.
	 */
	public void meetKleurRGB() {
		SampleProvider rgb = lichtSensor.getRGBMode();
		float[] sample = new float[rgb.sampleSize()];
		rgb.fetchSample(sample, 0);
		this.r = (double) sample[0];
		this.g = (double) sample[1];
		this.b = (double) sample[2];
	}

	public void nieuweMetingWordtOudeMeting() {
		this.oudeR = this.r;
		this.oudeG = this.g;
		this.oudeB = this.b;
		this.oudeI = this.i;
	}
	
	public double getI() {
		return i;
	}
	
	public double getR() {
		return r;
	}

	public double getG() {
		return g;
	}

	public double getB() {
		return b;
	}

	public double getOudeR() {
		return oudeR;
	}

	public double getOudeG() {
		return oudeG;
	}

	public double getOudeB() {
		return oudeB;
	}

	public double getOudeI() {
		return oudeI;
	}
	
	
}
