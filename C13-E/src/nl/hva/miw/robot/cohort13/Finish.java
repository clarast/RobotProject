package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;

public class Finish {

	private int aantalFinishPassages;
	private LichtsensorMeting finishPassageMeting;
	private GraphicsLCD LCD;
	
	public Finish(LichtsensorMeting finishPassageMeting, GraphicsLCD LCD) {
		super();
		this.finishPassageMeting = finishPassageMeting;
		this.LCD = LCD;
	}

	public void finishIJken() {
		LCD.clear();
		System.out.println("Snuffel finish, druk op enter als ie klaarstaat");
		Button.ENTER.waitForPress();
		finishPassageMeting.meetKleurRGB();
		LCD.clear();
		System.out.printf("Finish:\nR%.1f - G%.1f - B%.1f\nEnter als Fikkie klaar is om te rijden.",
				finishPassageMeting.getR(), finishPassageMeting.getG(), finishPassageMeting.getB());
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
