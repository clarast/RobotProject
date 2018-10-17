package nl.hva.miw.robot.cohort13;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;
import lejos.hardware.sensor.EV3ColorSensor;

import lejos.hardware.Button;

public class Motoren {

	private float motorSpeedA;
	private float motorSpeedB;
	private int kleurXpassage = 0;
	private final double RICHT_INTENSITEIT = 0.50;
	private LichtsensorMeting meting = new LichtsensorMeting();
	private int oudeKleurMeting;
	private int nieuweKleurMeting = 1;

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
		}

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
			this.motorSpeedA = Motor.A.getMaxSpeed();
			this.motorSpeedB = Motor.B.getMaxSpeed();
		} else if (lichtIntensiteit > this.RICHT_INTENSITEIT) {
			this.motorSpeedA = 80;
			this.motorSpeedB = 100;
		} else if (lichtIntensiteit < this.RICHT_INTENSITEIT) {
			this.motorSpeedA = 100;
			this.motorSpeedB = 80;
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