package nl.hva.miw.robot.cohort13;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.utility.Delay;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;
import lejos.hardware.sensor.EV3ColorSensor;
//sander

public class Lijnvolger {

	private int motorPowerA = 40;
	private int motorPowerB = 40;
	private int kleurXpassage = 0;
	private final double INTENSITEIT_LAAG = 0.15;
	private final double RICHT_INTENSITEIT = 0.5;
	private final double INTENSITEIT_HOOG = 0.85;
	private Tijdswaarneming tijdswaarneming = new Tijdswaarneming();
	GraphicsLCD LCD = BrickFinder.getDefault().getGraphicsLCD();
	static UnregulatedMotor MotorA = new UnregulatedMotor(MotorPort.A);
	static UnregulatedMotor MotorB = new UnregulatedMotor(MotorPort.B);

	// initialiseer lichtsensor (in deze klasse omdat lichtsensor vanuit hier
	// aangestuurd en aangeroepen wordt)
	Brick brick = BrickFinder.getDefault();
	Port s1 = brick.getPort("S1");
	EV3ColorSensor lichtSensor = new EV3ColorSensor(s1);

	// Maak objecten meting en finish, gebruik daarin dezelfde lichtsensor
	private LichtsensorMeting meting = new LichtsensorMeting(lichtSensor);
	private LichtsensorMeting finish = new LichtsensorMeting(lichtSensor);

	void moveRobotFwd() {
		// this.achterTest();
		this.finishIJken();
		int startStopwatch = 0;

		MotorA.backward();
		MotorB.backward();

		while (kleurXpassage < 2) {
			this.setKleurXpassage();
			meting.meetIntensiteit();
			bepaalTypeBocht();
			rijden();
			if (kleurXpassage == 1 && startStopwatch == 0) {
				tijdswaarneming.startStopwatch();
				startStopwatch++;
			}
		}

		LCD.clear();
		tijdswaarneming.stopStopwatch();
		MotorA.stop();
		MotorB.stop();
		lichtSensor.close();
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
			LCD.clear();
		}
	}

	private boolean finishkleur(double kleur1, double kleur2, double kleur3) {
		if (kleur1 == finish.getR() && kleur2 == finish.getG() && kleur3 == finish.getB()) {
			return true;
		} else
			return false;
	}

	private void bepaalTypeBocht() {

		// scherpe bocht naar links / rechts
		if (meting.getIntensiteit() > INTENSITEIT_HOOG) {
			draaiOpDePlek();
		} else if (meting.getIntensiteit() < INTENSITEIT_LAAG) {
			draaiOpDePlek();

			// flauwe bocht naar links / rechts (methode voor maken!)
		} else if (meting.getIntensiteit() > this.RICHT_INTENSITEIT) {
			flauweBocht();

		} else if (meting.getIntensiteit() < this.RICHT_INTENSITEIT) {
			flauweBocht();
		}
	}

	public void rijden() {
		MotorA.setPower(motorPowerA);
		MotorB.setPower(motorPowerB);
		Delay.msDelay(100);
	}

	public void draaiOpDePlek() {
		this.motorPowerA = 40;
		this.motorPowerB = 40;
		if (meting.getIntensiteit() > INTENSITEIT_HOOG) {
			MotorA.forward();
		} else {
			MotorB.forward();
		}
	}

	public void achterTest() {
		MotorA.setPower(50);
		MotorB.setPower(50);
		MotorA.forward();
		MotorB.forward();
		Delay.msDelay(5000);

		MotorA.backward();
		Delay.msDelay(5000);
		System.exit(1);
	}

	public void flauweBocht() {
		MotorA.backward();
		MotorB.backward();
		if (meting.getIntensiteit() > RICHT_INTENSITEIT) {
			this.motorPowerA = 30;
			this.motorPowerB = 40;
		} else {
			this.motorPowerA = 40;
			this.motorPowerB = 30;
		}
	}

}
