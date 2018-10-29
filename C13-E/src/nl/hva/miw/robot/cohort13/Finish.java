package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;
import lejos.hardware.sensor.EV3ColorSensor;

public class Finish {

	private int aantalFinishPassages;
	private double finishR;
	private double finishG;
	private double finishB;
	private EV3ColorSensor lichtSensor;
	private Scherm scherm;
	private GeluidSpeler geluidspeler;
	

	public Finish(EV3ColorSensor lichtSensor, Scherm scherm, GeluidSpeler geluidspeler) {
		super();
		this.lichtSensor = lichtSensor;
		this.scherm = scherm;
		this.geluidspeler = geluidspeler;
	}
	
	public Finish(double finishR, double finishG, double finishB) {
		this.finishR = finishR;
		this.finishG = finishG;
		this.finishB = finishB;
	}

	public void finishIJken() {
		scherm.printSnuffel();
		Button.ENTER.waitForPress();
		LichtsensorMeting finishMeting = new LichtsensorMeting(lichtSensor);
		finishMeting.meetKleurRGB();
		GeluidSpeler geluidspeler = new GeluidSpeler();
		//geluidspeler.speelSnuffel();
		this.finishR = finishMeting.getR();
		this.finishG = finishMeting.getG();
		this.finishB = finishMeting.getB();
		scherm.printKlaarOmTeRijden(this.finishR, this.finishG, this.finishB);
		Button.ENTER.waitForPress();
	}

	/**
	 * Deze methode hoogt de aantal passages op als de finishlijn zoals besnuffeld
	 * wordt gepasseerd.
	 */
	public void setAantalFinishPassages(LichtsensorMeting kleurmeting) {
		kleurmeting.nieuweMetingWordtOudeMeting();
		kleurmeting.meetKleurRGB();

		boolean oudeKleurMetingFinish = this.finishkleur(kleurmeting.getOudeR(), kleurmeting.getOudeG(),
				kleurmeting.getOudeB());
		boolean nieuweKleurMetingFinish = this.finishkleur(kleurmeting.getR(), kleurmeting.getG(), kleurmeting.getB());

		if (oudeKleurMetingFinish && !nieuweKleurMetingFinish) {
			aantalFinishPassages++;
			scherm.printKleurpassage();
		}
	}

	public boolean finishkleur(double kleur1, double kleur2, double kleur3) {
		if (kleur1 == this.finishR && kleur2 == this.finishG && kleur3 == this.finishB) {
			return true;
		} else
			return false;
	}

	public int getAantalFinishPassages() {
		return aantalFinishPassages;
	}

	public double getFinishR() {
		return finishR;
	}

	public double getFinishG() {
		return finishG;
	}

	public double getFinishB() {
		return finishB;
	}
	
	

}
