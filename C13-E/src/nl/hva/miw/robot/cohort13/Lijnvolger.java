package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.utility.Delay;
import lejos.hardware.motor.*;
import lejos.hardware.sensor.EV3ColorSensor;

public class Lijnvolger {

	// De sensors en motoren worden in Fikkie aangemaakt, en hier doorgegeven met de constructor.
	private UnregulatedMotor motorA;
	private UnregulatedMotor motorB;
	private EV3ColorSensor lichtSensor;
	private GraphicsLCD LCD;
	
	private int motorPowerA = 40; // 40 is de startsnelheid
	private int motorPowerB = 40; // 40 is de startsnelheid
	
	private final double INTENSITEIT_DREMPEL_LAAG1 = 0.15;
	private final double INTENSITEIT_DREMPEL_LAAG2 = 0.40;
	private final double INTENSITEIT_RICHTWAARDE = 0.5;
	private final double INTENSITEIT_DREMPEL_HOOG1 = 0.60;
	private final double INTENSITEIT_DREMPEL_HOOG2 = 0.80;
	
	private Tijdswaarneming tijdswaarneming = new Tijdswaarneming();

	private LichtsensorMeting meting = new LichtsensorMeting(lichtSensor);
	private LichtsensorMeting finishPassageMeting = new LichtsensorMeting(lichtSensor);
	private Finish finish = new Finish(finishPassageMeting, LCD);

	public Lijnvolger(UnregulatedMotor motorA, UnregulatedMotor motorB, EV3ColorSensor lichtSensor, GraphicsLCD LCD) {
		super();
		this.motorA = motorA;
		this.motorB = motorB;
		this.lichtSensor = lichtSensor;
		this.LCD = LCD;
	}
	
	
	void tijdrit() {
		finish.finishIJken();
		boolean stopwatchStarted = false;

		motorA.backward();
		motorB.backward();

		while (finish.getAantalFinishPassages() < 2) {
			finish.setAantalFinishPassages();
			meting.meetIntensiteit();
			this.bepaalTypeBocht();
			this.rijden();
			if (finish.getAantalFinishPassages() == 1 && !stopwatchStarted) {
				tijdswaarneming.startStopwatch();
				stopwatchStarted = true;
			}
		}

		tijdswaarneming.stopStopwatch();
		motorA.stop();
		motorB.stop();
		lichtSensor.close();
		LCD.clear();
		LCD.drawString(tijdswaarneming.toString(), 100, 20, GraphicsLCD.BASELINE | GraphicsLCD.HCENTER);
		Button.ENTER.waitForPress();
	}

	private void bepaalTypeBocht() {
		// scherpe bocht (draai op plek) als boven of onder drempelwaarde. Flauwe bocht
		// als onder tweede drempelwaarde. Anders geen bocht maar rechtdoor.
		if (meting.getIntensiteit() > INTENSITEIT_DREMPEL_HOOG2 || meting.getIntensiteit() < INTENSITEIT_DREMPEL_LAAG1) {
			this.draaiOpDePlek();
		} else if (meting.getIntensiteit() > INTENSITEIT_DREMPEL_HOOG1 || meting.getIntensiteit() < INTENSITEIT_DREMPEL_LAAG2) {
			this.flauweBocht();
		} else
			this.rechtdoor();
	}

	public void draaiOpDePlek() {
		this.motorPowerA = 35;
		this.motorPowerB = 40;
		if (meting.getIntensiteit() > INTENSITEIT_DREMPEL_HOOG2) {
			motorA.forward();
			motorB.backward();
		} else {
			motorA.backward();
			motorB.forward();
		}
	}

	public void flauweBocht() {
		motorA.backward();
		motorB.backward();
		if (meting.getIntensiteit() > INTENSITEIT_RICHTWAARDE) {
			this.motorPowerA = 45;
			this.motorPowerB = 60;
		} else {
			this.motorPowerA = 60;
			this.motorPowerB = 45;
		}
	}

	private void rechtdoor() {
		motorA.backward();
		motorB.backward();
		this.motorPowerA = 60;
		this.motorPowerB = 60;
	}

	public void rijden() {
		motorA.setPower(motorPowerA);
		motorB.setPower(motorPowerB);
		Delay.msDelay(125);
	}

}
