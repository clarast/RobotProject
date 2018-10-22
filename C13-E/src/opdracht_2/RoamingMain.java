package opdracht_2;

import java.io.File;

import lejos.ev3.tools.LCDDisplay;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.robotics.pathfinding.RandomSelfGeneratingNode;
import lejos.utility.Delay;

public class RoamingMain {

	static UnregulatedMotor motorA = new UnregulatedMotor(MotorPort.A);
	static UnregulatedMotor motorB = new UnregulatedMotor(MotorPort.B);
	static TouchSensor touch = new TouchSensor(SensorPort.S3);
	static RegulatedMotor motorC = new EV3MediumRegulatedMotor(MotorPort.C);
	static GraphicsLCD LCD = BrickFinder.getDefault().getGraphicsLCD();
	private static final int NUMBER_OF_ACTIONS = 4;
	private static final int START_NUMBER = 1;
	private static final int MAX_DEGREES = 60;
	private static final int START_DEGREES = 30;
	private static final int START_KWISPELS = 3;
	private static final int MAX_KWISPELS = 8;
	private static final int STARTPOINT_TAIL = 0;
	private static final int ACTION_1 = 1;
	private static final int ACTION_2 = 2;
	private static final int ACTION_3 = 3;
	private static final int AVOID_SPEED_HIGH = 120;
	private static final int AVOID_SPEED_LOW = 40;

	public static void main(String[] args) {

		Sound.beepSequenceUp(); // make sound when ready.
		LCD.drawString("PUSH MY BUTTON", 100, 20, GraphicsLCD.BASELINE | GraphicsLCD.HCENTER);
		Button.waitForAnyPress();
		// TODO: Draw doggy eyes
		LCD.clear();
		Delay.msDelay(1000);
		LCD.drawString("ROAMING MODE", 100, 20, GraphicsLCD.BASELINE | GraphicsLCD.HCENTER);

		Sound.playSample(new File("dog_bark6.wav"), Sound.VOL_MAX);


		// poort initialiseren
		Port port = LocalEV3.get().getPort("S2");

		// Maak een object van de sensor
		SensorModes sensorIR = new EV3IRSensor(port);

		// Get an instance of this sensor in measurement mode
		SampleProvider distance = sensorIR.getMode("Distance");

		while (Button.ESCAPE.isUp()) {

			Delay.msDelay(1000);
			
			motorA.setPower(200);
			motorB.setPower(200);
			motorA.backward();
			motorB.backward();
			

			// stack a filter on the sensor that gives the running average of the last 5
			// samples
			SampleProvider average = new MeanFilter(distance, 5);

			// Initialize an array of floats for fetching samples
			float[] sample = new float[average.sampleSize()];

			// Fetch a sample
			average.fetchSample(sample, 0);

			int dist = (int) sample[0];

			while (dist < 35 && Button.ESCAPE.isUp()) {
				// TODO maak een switch case om een modus te kiezen wanneer er iets voor de
				// sensor
				// gedetecteerd wordt.

				switch (makeRandomNumber(ACTION_1)) {
				case 1: // ga naar links
					avoidleft();
					break;
				// zet 'm naar rechts
				case 2:
					avoidRight();
					break;
				case 3:
					// blaf
					Sound.playSample(new File("dog_bark6.wav"), Sound.VOL_MAX);
					break;

				case 4:
					// TODO wag tail
					wagTail();
				}

				// stack a filter on the sensor that gives the running average of the last 5
				// samples
				average = new MeanFilter(distance, 5);

				// initialize an array of floats for fetching samples
				sample = new float[average.sampleSize()];

				// fetch a sample
				average.fetchSample(sample, 0);

				dist = (int) sample[0];

			}
		}

		// stop de motoren
		motorA.stop();
		motorB.stop();
		motorC.stop();

		// sluit de motoren
		motorA.close();
		motorB.close();
		motorC.close();

		Sound.playSample(new File("dog_bark6.wav"), Sound.VOL_MAX);
		// Blaf nog één keer als klaar.
	}

	public static int makeRandomNumber(int actie) {
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

	public static void wagTail() {

		for (int aantalKeer = 0; aantalKeer < makeRandomNumber(3); aantalKeer++) {
			motorC.setSpeed(600);
			motorC.rotateTo(makeRandomNumber(ACTION_2));
			Delay.msDelay(1);
			motorC.rotateTo(-makeRandomNumber(ACTION_2));
		}
		motorC.rotateTo(STARTPOINT_TAIL);
		// make random
	}

	public static void avoidleft() {

		motorB.forward();
		motorB.setPower(AVOID_SPEED_HIGH);
		motorA.setPower(AVOID_SPEED_LOW);
	}

	public static void avoidRight() {
		motorA.forward();
		motorA.setPower(AVOID_SPEED_HIGH);
		motorB.setPower(AVOID_SPEED_LOW);
	}

}
