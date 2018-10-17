package nl.hva.miw.robot.cohort13;

import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.utility.Delay;
import lejos.robotics.RegulatedMotor;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;

public class Motoren {

	private float motorSpeedA;
	private float motorSpeedB;
	private int kleurXpassage = 0;
	private final double RICHT_INTENSITEIT = 0.50;
	private LichtsensorMeting meting = new LichtsensorMeting();
	private int oudeKleurMeting;
	private int nieuweKleurMeting = 1;
	private Tijdswaarneming tijdswaarneming = new Tijdswaarneming();

	GraphicsLCD LCD = BrickFinder.getDefault().getGraphicsLCD();
	static RegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
	static RegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.B);

	void moveRobotFwd() {

		// Display on robot screen
		LCD.clear();
		LCD.drawString("Start Moving!", 100, 20, GraphicsLCD.BASELINE | GraphicsLCD.HCENTER);

		while (kleurXpassage < 2) {
			this.setKleurXpassage();
			meting.meetIntensiteit();
			zetMotorSnelheid(meting.getIntensiteit());
			rijden();
			if (kleurXpassage == 1) {
				tijdswaarneming.getStopwatch().reset();
			}
		}
		tijdswaarneming.getStopwatch().elapsed();
		leftMotor.stop();
		rightMotor.stop();
		// toevoegen SFX

	}

	/**
	 * Deze methode hoogt de aantal passages op als een niet zwarte haakse lijn
	 * wordt gepasseerd. Methode finishkleur wordt aangeroepen om te bepalen of een
	 * gemeten kleur een meting is die niet zwart, wit of undefined is. van de float
	 * die terugkomt van de kleurmeting is een int gemaakt via casting.
	 */
	private void setKleurXpassage() {
		this.oudeKleurMeting = this.nieuweKleurMeting;
		this.nieuweKleurMeting = (int) meting.getKleur();
		System.out.println(nieuweKleurMeting);

		boolean oudeKleurMetingFinish = this.finishkleur(oudeKleurMeting);
		boolean nieuweKleurMetingFinish = this.finishkleur(nieuweKleurMeting);

		if (oudeKleurMetingFinish && !nieuweKleurMetingFinish) {
			kleurXpassage++;
		}
	}

	/**
	 * @param kleur: dit is de kleur waarvan bepaald moet worden of het een finish
	 *        kleur is.
	 * @return true als de inputkleur niet zwart, wit of undefined is.
	 */
	private boolean finishkleur(int kleur) {
		if (kleur == 0 || kleur == 1 || kleur == 6) {
			return false;
		} else
			return true;
	}

	private void zetMotorSnelheid(float lichtIntensiteit) {
		if (lichtIntensiteit == this.RICHT_INTENSITEIT) {
			this.motorSpeedA = 200;
			this.motorSpeedB = 200;
		} else if (lichtIntensiteit > this.RICHT_INTENSITEIT) {
			this.motorSpeedA = 30;
			this.motorSpeedB = 150;
		} else if (lichtIntensiteit < this.RICHT_INTENSITEIT) {
			this.motorSpeedA = 150;
			this.motorSpeedB = 30;
		}

	}

	public void rijden() {
		leftMotor.setSpeed((int) motorSpeedA);
		rightMotor.setSpeed((int) motorSpeedB);
		leftMotor.backward();
		rightMotor.backward();
		Delay.msDelay(100);

	}
}