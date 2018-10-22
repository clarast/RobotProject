package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.sensor.EV3ColorSensor;

public class Finish {

	private int aantalFinishPassages;
	private GraphicsLCD LCD;
	private double finishR;
	private double finishG;
	private double finishB;
	private EV3ColorSensor lichtSensor;

	public Finish(EV3ColorSensor lichtSensor, GraphicsLCD LCD) {
		super();
		this.lichtSensor = lichtSensor;
		this.LCD = LCD;
	}

	
	public void finishIJken() {
		System.out.println("Snuffel finish, druk op enter als ie klaarstaat");
		Button.ENTER.waitForPress();
		LichtsensorMeting finishMeting = new LichtsensorMeting(lichtSensor);
		finishMeting.meetKleurRGB();
		this.finishR = finishMeting.getR();
		this.finishG = finishMeting.getG();
		this.finishB = finishMeting.getB();
		System.out.printf("Finish:\nR%.1f - G%.1f - B%.1f\nEnter als Fikkie klaar is om te rijden.", this.finishR,
				this.finishG, this.finishB);
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
			LCD.clear();
			System.out.println("kleurpassage");
			LCD.clear();
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

}
