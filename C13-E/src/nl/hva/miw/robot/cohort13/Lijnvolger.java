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
	private final double INTENSITEIT_DREMPEL_LAAG2 = 0.25;
	private final double INTENSITEIT_RICHTWAARDE = 0.5;
	private final double INTENSITEIT_DREMPEL_HOOG1 = 0.40;
	private final double INTENSITEIT_DREMPEL_HOOG2 = 0.50;
	
	private Tijdswaarneming tijdswaarneming = new Tijdswaarneming();

	private LichtsensorMeting meting; 
	private LichtsensorMeting finishPassageMeting; 
	private Finish finish; 

	public Lijnvolger(UnregulatedMotor motorA, UnregulatedMotor motorB, EV3ColorSensor lichtSensor, GraphicsLCD LCD) {
		super();
		this.motorA = motorA;
		this.motorB = motorB;
		this.lichtSensor = lichtSensor;
		this.LCD = LCD;
		meting = new LichtsensorMeting(lichtSensor);
		finishPassageMeting = new LichtsensorMeting(lichtSensor);
		finish = new Finish(lichtSensor, LCD);
	}
	
	
	public void lichttest() {
		while (Button.ESCAPE.isUp()) {
			meting.meetIntensiteit();
			System.out.println(meting.getIntensiteit());
		}
		System.exit(1);

	}
	
	void tijdrit() {
		finish.finishIJken();
		boolean stopwatchStarted = false;

		while (finish.getAantalFinishPassages() < 2 && Button.ESCAPE.isUp()) {
			finish.setAantalFinishPassages(finishPassageMeting);
			meting.meetIntensiteit();
			System.out.println(meting.getIntensiteit());
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
		this.motorPowerB = 30;
		if (meting.getIntensiteit() > INTENSITEIT_DREMPEL_HOOG2) {
			motorA.backward();
			motorB.forward();
		} else {
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

	private void rechtdoor() {
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
