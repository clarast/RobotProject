package nl.hva.miw.robot.cohort13;

import lejos.hardware.Brick;
import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.utility.Delay;
import lejos.hardware.motor.*;
import lejos.hardware.sensor.EV3ColorSensor;

public class Lijnvolger {

	// De sensors en motoren worden in Fikkie aangemaakt, en hier doorgegeven met de
	// constructor.
	Brick brick;
	private UnregulatedMotor motorA;
	private UnregulatedMotor motorB;
	private EV3ColorSensor lichtSensor;
	private Scherm scherm;
	private GeluidSpeler geluidspeler;

	private int motorPowerA = 40; // 40 is de startsnelheid
	private int motorPowerB = 40; // 40 is de startsnelheid

	private final double INTENSITEIT_DREMPEL_LAAG1 = 0.15;
	private final double INTENSITEIT_DREMPEL_LAAG2 = 0.30;
	private final double INTENSITEIT_RICHTWAARDE = 0.33;
	private final double INTENSITEIT_DREMPEL_HOOG1 = 0.35;
	private final double INTENSITEIT_DREMPEL_HOOG2 = 0.40;

	private Tijdswaarneming tijdswaarneming = new Tijdswaarneming();

	private LichtsensorMeting meting;
	private LichtsensorMeting finishPassageMeting;
	private Finish finish;

	public Lijnvolger(Hardware hardware) {
		//this.brick = super.maakBrick();
		this.motorA = hardware.maakMotorA();
		this.motorB = hardware.maakMotorB();
		this.lichtSensor = hardware.maakLichtsensor();
		this.scherm = hardware.maakScherm();
		this.geluidspeler = hardware.maakGeluidSpeler();
		meting = new LichtsensorMeting(lichtSensor);
		finishPassageMeting = new LichtsensorMeting(lichtSensor);
		finish = new Finish(lichtSensor, scherm, geluidspeler);
	}

	public void tijdrit() {
		finish.finishIJken();
		scherm.printOgen();
		boolean stopwatchStarted = false;

		while (finish.getAantalFinishPassages() < 2 && Button.ESCAPE.isUp()) {
			finish.setAantalFinishPassages(finishPassageMeting);
			meting.meetIntensiteit();
			this.bepaalTypeBocht();
			this.rijden();
			if (finish.getAantalFinishPassages() == 1 && !stopwatchStarted) {
				tijdswaarneming.startStopwatch();
				stopwatchStarted = true;
			}
			scherm.printTekst(tijdswaarneming.toString());
		}

		tijdswaarneming.stopStopwatch();
		motorA.stop();
		motorB.stop();
		scherm.printRondeTijd(tijdswaarneming.toString());
		Button.ENTER.waitForPress();
		scherm.schoonScherm();
	}

	public void bepaalTypeBocht() {
		// scherpe bocht (draai op plek) als boven of onder drempelwaarde. Flauwe bocht
		// als onder tweede drempelwaarde. Anders geen bocht maar rechtdoor.
		if (meting.getIntensiteit() > INTENSITEIT_DREMPEL_HOOG2
				|| meting.getIntensiteit() < INTENSITEIT_DREMPEL_LAAG1) {
			this.draaiOpDePlek();
		} else if (meting.getIntensiteit() > INTENSITEIT_DREMPEL_HOOG1
				|| meting.getIntensiteit() < INTENSITEIT_DREMPEL_LAAG2) {
			this.flauweBocht();
		} else
			this.rechtdoor();
	}

	public void draaiOpDePlek() {
		this.motorPowerA = 35;
		this.motorPowerB = 30;
		if (meting.getIntensiteit() > INTENSITEIT_DREMPEL_HOOG2) {
			motorA.backward();
			motorB.forward();
		} else {
			this.motorPowerB = 45;
			motorA.forward();
			motorB.backward();
		}
	}

	public void flauweBocht() {
		motorA.forward();
		motorB.forward();
		if (meting.getIntensiteit() > INTENSITEIT_RICHTWAARDE) {
			this.motorPowerA = 20;
			this.motorPowerB = 40;
		} else {
			this.motorPowerA = 40;
			this.motorPowerB = 20;
		}
	}

	public void rechtdoor() {
		motorA.forward();
		motorB.forward();
		this.motorPowerA = 40;
		this.motorPowerB = 40;
	}

	public void rijden() {
		motorA.setPower(motorPowerA);
		motorB.setPower(motorPowerB);
		Delay.msDelay(100);
	}	

}
