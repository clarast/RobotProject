package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;

public class Finish {

	private int aantalFinishPassages;
	static LichtsensorMeting finishPassageMeting;
	private Scherm scherm;
	
	public Finish(LichtsensorMeting finishPassageMeting, Scherm scherm) {
		super();
		this.finishPassageMeting = finishPassageMeting;
		this.scherm = scherm;
	}

	public void finishIJken() {
		scherm.printSnuffel();
		Button.ENTER.waitForPress();
		finishPassageMeting.meetKleurRGB();
		// snuffel.wav toevoegen
		scherm.printKlaarOmTeRijden();
		Button.ENTER.waitForPress();
		LCD.clear();
	}

	/**
	 * Deze methode hoogt de aantal passages op als de finishlijn zoals besnuffeld
	 * wordt gepasseerd.
	 */
	public void setAantalFinishPassages() {
		finishPassageMeting.nieuweMetingWordtOudeMeting();
		finishPassageMeting.meetKleurRGB();

		boolean oudeKleurMetingFinish = this.finishkleur(finishPassageMeting.getOudeR(), finishPassageMeting.getOudeG(),
				finishPassageMeting.getOudeB());
		boolean nieuweKleurMetingFinish = this.finishkleur(finishPassageMeting.getR(), finishPassageMeting.getG(),
				finishPassageMeting.getB());

		if (oudeKleurMetingFinish && !nieuweKleurMetingFinish) {
			aantalFinishPassages++;
			LCD.clear();
			System.out.println("kleurpassage");
			LCD.clear();
		}
	}

	public boolean finishkleur(double kleur1, double kleur2, double kleur3) {
		if (kleur1 == finishPassageMeting.getR() && kleur2 == finishPassageMeting.getG()
				&& kleur3 == finishPassageMeting.getB()) {
			return true;
		} else
			return false;
	}

	public int getAantalFinishPassages() {
		return aantalFinishPassages;
	}

}
