package nl.hva.miw.robot.cohort13;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.utility.Delay;
import lejos.robotics.RegulatedMotor;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;
import lejos.hardware.sensor.EV3ColorSensor;

public class Motoren {

	private int motorSpeedA;
	private int motorSpeedB;
	private int kleurXpassage = 0;
	private final double RICHT_INTENSITEIT = 0.50;
	private Tijdswaarneming tijdswaarneming = new Tijdswaarneming();
	GraphicsLCD LCD = BrickFinder.getDefault().getGraphicsLCD();
	static RegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
	static RegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.B);

	// initialiseer lichtsensor (in deze klasse omdat lichtsensor vanuit hier aangestuurd en aangeroepen wordt) 
	Brick brick = BrickFinder.getDefault();
	Port s1 = brick.getPort("S1");
	EV3ColorSensor lichtSensor = new EV3ColorSensor(s1);
	
	// Maak objecten meting en finish, gebruik daarin dezelfde lichtsensor
	private LichtsensorMeting meting = new LichtsensorMeting(lichtSensor);
	private LichtsensorMeting finish = new LichtsensorMeting(lichtSensor);

	void moveRobotFwd() {
		this.finishIJken();

		while (kleurXpassage < 2) {
			this.setKleurXpassage();
			// meting.meetIntensiteit();
			// zetMotorSnelheid(meting.getIntensiteit());
			this.motorSpeedA = 100;
			this.motorSpeedB = 100;
			vooruitRijden();
			if (kleurXpassage == 1) {
				tijdswaarneming.startStopwatch();
			}
		}
		tijdswaarneming.stopStopwatch();
		leftMotor.stop();
		rightMotor.stop();
		LCD.clear();
		LCD.drawString(tijdswaarneming.toString(), 100, 20, GraphicsLCD.BASELINE | GraphicsLCD.HCENTER);
		Delay.msDelay(5000);
		// toevoegen SFX
	}

	private void finishIJken() {
		LCD.clear();
		System.out.println("Snuffel finish, druk op enter als ie klaarstaat");
		Button.ENTER.waitForPress();
		finish.meetKleurRGB();
		LCD.clear();
		System.out.printf("Finish:\nR%.1f - G%.1f - B%.1f\nEnter als Fikkie klaar is om te rijden.", finish.getR(),
				finish.getG(), finish.getB());
		Button.ENTER.waitForPress();
		LCD.clear();
	}

	/**
	 * Deze methode hoogt de aantal passages op als een niet zwarte haakse lijn
	 * wordt gepasseerd. Methode finishkleur wordt aangeroepen om te bepalen of een
	 * gemeten kleur een meting is die niet finish is.
	 */
	private void setKleurXpassage() {
		meting.nieuweMetingWordtOudeMeting();
		meting.meetKleurRGB();

		boolean oudeKleurMetingFinish = this.finishkleur(meting.getOudeR(), meting.getOudeG(), meting.getOudeB());
		boolean nieuweKleurMetingFinish = this.finishkleur(meting.getR(), meting.getG(), meting.getB());

		if (oudeKleurMetingFinish && !nieuweKleurMetingFinish) {
			kleurXpassage++;
			LCD.clear();
			System.out.println("kleurpassage");
		}
	}

	private boolean finishkleur(double kleur1, double kleur2, double kleur3) {
		if (kleur1 == finish.getR() && kleur2 == finish.getG() && kleur3 == finish.getB()) {
			return true;
		} else
			return false;
	}

	private void zetMotorSnelheid(float lichtIntensiteit) {
		// scherpbocht naar links
		if (lichtIntensiteit > 0.9 || lichtIntensiteit < 0.1) {
			draaiOpDePlek(lichtIntensiteit);
		}
		// flauwe bocht naar links
		else if (lichtIntensiteit > this.RICHT_INTENSITEIT) {
			this.motorSpeedA = 100;
			this.motorSpeedB = 200;
		}
		// flauwe bocht naar rechts
		else if (lichtIntensiteit < this.RICHT_INTENSITEIT) {
			this.motorSpeedA = 200;
			this.motorSpeedB = 100;
		}
	}

	public void vooruitRijden() {
		leftMotor.setSpeed(motorSpeedA);
		rightMotor.setSpeed(motorSpeedB);
		leftMotor.backward();
		rightMotor.backward();
		Delay.msDelay(100);
	}

	public void draaiOpDePlek(float lichtIntensiteit) {
		if (lichtIntensiteit > 0.9) {
			this.motorSpeedA = 0;
			this.motorSpeedB = 200;
			leftMotor.setSpeed(motorSpeedA);
			rightMotor.setSpeed(motorSpeedB);
			leftMotor.forward();
			rightMotor.backward();
			Delay.msDelay(50);
			moveRobotFwd();
		} else {
			this.motorSpeedA = 200;
			this.motorSpeedB = 0;
			leftMotor.setSpeed(motorSpeedA);
			rightMotor.setSpeed(motorSpeedB);
			leftMotor.backward();
			rightMotor.forward();
			Delay.msDelay(50);
			moveRobotFwd();
		}
	}

}
