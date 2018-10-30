package nl.hva.miw.robot.cohort13;

import lejos.hardware.Brick;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;

public class Dollen {

	private Hardware hardware;
	private UnregulatedMotor motorA;
	private UnregulatedMotor motorB;
	private EV3MediumRegulatedMotor motorC;
	private EV3IRSensor infraroodSensor;
	private EV3TouchSensor touchSensor;
	private Scherm scherm;
	private GeluidSpeler geluidspeler;

	private SampleProvider distance;
	private SampleProvider average;
	private SampleProvider touch;
	private float[] sample;
	private float[] sample2;
	private int dist;

	private int counter = 0;
	private int spelTel = 1;
	private final int SAMPLE_LENGTH = 5;
	private final int NUMBER_OF_AVOIDS = 4;
	private final int NUMBER_OF_ACTIONS = 5;
	private final int START_NUMBER = 1;
	private final int MAX_DEGREES = 60;
	private final int START_DEGREES = 30;
	private final int START_KWISPELS = 2;
	private final int MAX_KWISPELS = 3;
	private final int STARTPOINT_TAIL = 0;
	private final int ACTION_1 = 1;
	private final int ACTION_2 = 2;
	private final int ACTION_3 = 3;
	private final int ACTION_4 = 4;
	private final int AVOID_SPEED_HIGH = 100;
	private final int AVOID_SPEED_LOW = 50;

	public Dollen(Hardware hardware) {
		super();
		this.hardware = hardware;
		this.motorA = hardware.maakMotorA();
		this.motorB = hardware.maakMotorB();
		this.motorC = hardware.maakMotorC();
		this.infraroodSensor = hardware.maakInfraroodSensor();
		this.touchSensor = hardware.maakTouchSensor();
		this.scherm = hardware.maakScherm();
		this.geluidspeler = hardware.maakGeluidSpeler();
		this.distance = infraroodSensor.getDistanceMode();
		this.touch = touchSensor.getTouchMode();
		this.average = new MeanFilter(distance, SAMPLE_LENGTH);
		this.sample = new float[touch.sampleSize()];
		this.sample2 = new float[average.sampleSize()];

	}

	public void startDollen() {
		initiateDollen();
		drawLCD();
		geluidspeler.speelWelkomstBlaf();

		// verkrijg een instantie van de afstandsmodus

		while (Button.ESCAPE.isUp()) {

			average.fetchSample(sample2, 0);
			dist = (int) sample2[0];

			while (dist > 50 && Button.ESCAPE.isUp()) {
				counter++;
				if (isTouched()) {
					stopMotors();
					spelTel = startSpel(spelTel);
					Sound.twoBeeps();
					counter = 0;
					break;
				}

				motorA.setPower(50);
				motorB.setPower(50);
				motorA.forward();
				motorB.forward();
				Delay.msDelay(100);

				average.fetchSample(sample2, 0);
				dist = (int) sample2[0];

				if (counter == 10) {
					// switch case om een modus te kiezen wanneer er iets gedetecteerd wordt.
					chooseAction();
					counter = 0;
				}
			}

			chooseAvoid(); 
		}
		stopMotors();
		geluidspeler.speelBlaf3x();

	}

	private void chooseAvoid() {

		switch (makeRandomNumber(ACTION_1)) {
		case 1:
			avoidRight();
			break;
		case 2:
			avoidLeft();
			break;
		case 3:
			fullTurnLeft();
			break;
		case 4:
			fullTurnRight();
			break;
		}
	}

	private int startSpel(int spelTel) {
		if (spelTel == 1) {
			Kleurenspel kleurenspel = new Kleurenspel(hardware, touchSensor, motorA, motorA, motorC, scherm, geluidspeler);
			kleurenspel.startKleurenspel();
			spelTel = 2;
		} else if (spelTel == 2) {
			BalSpel balspel = new BalSpel(hardware, motorA, motorB, infraroodSensor, touchSensor, scherm);
			balspel.findBall();
			spelTel = 1;
		}
		return spelTel;

	}

	private void initiateDollen() {
		Sound.beepSequence(); // make sound when ready.
		scherm.printTekst("Druk op de knop!");
		Button.waitForAnyPress();
	}

	public boolean isTouched() {
		touch.fetchSample(sample, 0);

		if (sample[0] == 0)
			return false;
		else
			return true;
	}

	private void drawLCD() {
		scherm.printOgen();
	}

	private void chooseAction() {
		switch (makeRandomNumber(ACTION_4)) {
		case 1: // ga naar links
			avoidLeft();
			break;
		case 2:
			avoidLeft();
			break;
		case 3:
			bark();
			break;
		case 4:
			// wag tail
			wagTail();
			break;
		case 5:
			fullTurnLeft();
			fullTurnLeft();
			break;
		}
	}

	private void bark() {
		geluidspeler.speelWelkomstBlaf();
	}

	private void stopMotors() {
		// stop de motoren
		motorA.stop();
		motorB.stop();
		motorC.stop();
	}

	public int makeRandomNumber(int actie) {
		int random = 0;
		if (actie == ACTION_1) {
			random = (int) (Math.random() * NUMBER_OF_AVOIDS) + START_NUMBER;
		} else if (actie == ACTION_2) {
			random = (int) (Math.random() * MAX_DEGREES) + START_DEGREES;
		} else if (actie == ACTION_3) {
			random = (int) (Math.random() * MAX_KWISPELS) + START_KWISPELS;
		} else if (actie == ACTION_4) {
			random = (int) (Math.random() * NUMBER_OF_ACTIONS) + START_KWISPELS;
		}
		return random;
	}

	public void wagTail() {

		for (int aantalKeer = 0; aantalKeer < makeRandomNumber(3); aantalKeer++) {
			motorC.setSpeed(600);
			motorC.rotateTo(makeRandomNumber(ACTION_2));
			Delay.msDelay(1);
			motorC.rotateTo(-makeRandomNumber(ACTION_2));
		}
		motorC.rotateTo(STARTPOINT_TAIL);
		// make random
	}

	public void avoidLeft() {

		motorB.setPower(AVOID_SPEED_HIGH);
		motorA.setPower(AVOID_SPEED_LOW);
		motorA.backward();
		motorB.forward();
		Delay.msDelay(800);
	}

	public void avoidRight() {

		motorA.setPower(AVOID_SPEED_HIGH);
		motorB.setPower(AVOID_SPEED_LOW);
		motorB.backward();
		motorA.forward();
		Delay.msDelay(800);
	}

	public void fullTurnRight() {
		motorA.setPower(AVOID_SPEED_HIGH);
		motorB.setPower(AVOID_SPEED_LOW);
		motorB.backward();
		motorA.forward();
		Delay.msDelay(1100);
	}

	public void fullTurnLeft() {
		motorB.setPower(AVOID_SPEED_HIGH);
		motorA.setPower(AVOID_SPEED_LOW);
		motorA.backward();
		motorB.forward();
		Delay.msDelay(1100);
	}

}
