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

/**
 * @author Thomas
 *
 */
public class Motoren {

	private int motorSpeedA;
	private int motorSpeedB;
	private int kleurXpassage = 0;
	private LichtsensorMeting meting = new LichtsensorMeting();
	private int oudeKleurMeting;
	private int nieuweKleurMeting = 1;

	GraphicsLCD LCD = BrickFinder.getDefault().getGraphicsLCD();
	static RegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
	static RegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);

	void moveRobotFwd() {

		LCD.clear();
		// Display on robot screen
		LCD.drawString("Moving Forward", 100, 20, GraphicsLCD.BASELINE | GraphicsLCD.HCENTER);
		// This code will set the speed and move the robot backward for 5 seconds

		while (kleurXpassage < 2) {

			// roept methode aan die de variable kleurXpassage bepaalt
			this.setKleurXpassage();

			// intensiteitmeting nemen
			meting.meetIntensiteit();
			float lichtIntensiteit = meting.getIntensiteit();

			// meegeven aan een methode die berekent hoeveel gestuurd moet worden
			// berekenSnelheid(lichtIntensiteit);
			leftMotor.setSpeed(motorSpeedA);
			rightMotor.setSpeed(motorSpeedB);
			leftMotor.backward();
			rightMotor.backward();

			// rijd 100ms en daarna nieuwe meting
			Delay.msDelay(100);
		}

		leftMotor.stop();
		rightMotor.stop();
		// toevoegen SFX

	}

	/**
	 * Deze methode hoogt de aantal passages op als een niet zwarte haakse lijn wordt gepasseerd.
	 * Methode finishkleur wordt aangeroepen om te bepalen of een gemeten kleur een
	 * meting is die niet zwart, wit of undefined is. van de float die terugkomt van de kleurmeting is een int gemaakt via casting.
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
	 * @param kleur: dit is de kleur waarvan bepaald moet worden of het een finish kleur is.
	 * @return true als de inputkleur niet zwart, wit of undefined is.
	 */
	private boolean finishkleur(int kleur) {
		if (kleur == 0 || kleur == 1 || kleur == 6) {
			return false;
		} else
			return true;
	}

	private void berekenSnelheid(float lichtIntensiteit) {
		// bereken per motor de snelheid

		// set per motor de snelheid
		motorSpeedA = 10;
		motorSpeedB = 10;
	}
}

// void moverobotfwd() {
//
// LCD.clear();
//
// // Display on robot screen
//
// LCD.drawString("Moving Forward", 100, 20, GraphicsLCD.BASELINE |
// GraphicsLCD.HCENTER);
//
// while (true) {
//
// leftMotor.setSpeed(400);
//
// rightMotor.setSpeed(400);
//
// leftMotor.forward();
//
// rightMotor.forward();
//
// if (Button.readButtons() != 0) {
//
// leftMotor.stop();
//
// rightMotor.stop();
//
// System.exit(1);
//
// }
//
// }
//
// }
