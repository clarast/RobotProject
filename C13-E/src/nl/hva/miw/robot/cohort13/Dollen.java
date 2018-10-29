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

public class Dollen  {

	Brick brick;
	protected UnregulatedMotor motorA;
    protected UnregulatedMotor motorB;
    private EV3MediumRegulatedMotor motorC;
    private EV3IRSensor infraroodSensor;
    protected EV3TouchSensor touchSensor;
    protected Scherm scherm;
    protected GeluidSpeler geluidspeler;
    
    private SampleProvider distance;
	private SampleProvider average;
	private SampleProvider touch;
	private float[] sample;
	private float[] sample2;
	private int dist;
    
	private final int SAMPLE_LENGTH = 5;
	private final int NUMBER_OF_ACTIONS = 4;
	private final int START_NUMBER = 1;
	private final int MAX_DEGREES = 60;
	private final int START_DEGREES = 30;
	private final int START_KWISPELS = 3;
	private final int MAX_KWISPELS = 8;
	private final int STARTPOINT_TAIL = 0;
	private final int ACTION_1 = 1;
	private final int ACTION_2 = 2;
	private final int ACTION_3 = 3;
	private final int AVOID_SPEED_HIGH = 120;
	private final int AVOID_SPEED_LOW = 40;

	public Dollen(Hardware hardware) {
		super();
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
		// bark();

		// verkrijg een instantie van de afstandsmodus

		while (Button.ESCAPE.isUp()) {
			if (isTouched())
				break;
			Delay.msDelay(500);

			motorA.setPower(100);
			motorB.setPower(100);
			motorA.forward();
			motorB.forward();

			average.fetchSample(sample2, 0);
			dist = (int) sample2[0];

			while (dist < 35 && Button.ESCAPE.isUp()) {
				if (isTouched())
					break;
				// switch case om een modus te kiezen wanneer er iets gedetecteerd wordt.
				chooseAction();

				// maak opnieuw een meting om te detecteren of er iets in de baan van de sensor
				// staat.

				// average = new MeanFilter(distance, 5);

				// initialize an array of floats for fetching samples

				// sample2 = new float[average.sampleSize()];

				// fetch a sample
				average.fetchSample(sample2, 0);

				dist = (int) sample2[0];
			}
		}
		stopMotors();
		// bark();

	}

	private void initiateDollen() {
		//Sound.beepSequence(); // make sound when ready.
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
		// LCD.clear();
		Delay.msDelay(200);
		scherm.printOgen();
	}

	private void chooseAction() {
		switch (makeRandomNumber(ACTION_1)) {
		case 1: // ga naar links
			avoidleft();
			break;
		// zet 'm naar rechts
		case 2:
			avoidRight();
			break;
		case 3:
			wagTail();
			break;
		case 4:
			// wag tail
			wagTail();
		}
	}

	private void bark() {
		//Sound.playSample(new File("dog_bark6.wav"), Sound.VOL_MAX);
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
			random = (int) (Math.random() * NUMBER_OF_ACTIONS) + START_NUMBER;
		} else if (actie == ACTION_2) {
			random = (int) (Math.random() * MAX_DEGREES) + START_DEGREES;
		} else if (actie == ACTION_3) {
			random = (int) (Math.random() * MAX_KWISPELS) + START_KWISPELS;
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

	public void avoidleft() {

		motorB.forward();
		motorB.setPower(AVOID_SPEED_HIGH);
		motorA.setPower(AVOID_SPEED_LOW);
	}

	public void avoidRight() {
		motorA.forward();
		motorA.setPower(AVOID_SPEED_HIGH);
		motorB.setPower(AVOID_SPEED_LOW);
	}

}
