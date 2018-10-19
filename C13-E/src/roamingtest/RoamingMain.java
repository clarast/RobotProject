package roamingtest;

import java.io.File;

import customrobot.library.TouchSensor;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.ev3.LocalEV3;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.robotics.pathfinding.RandomSelfGeneratingNode;
import lejos.utility.Delay;

public class RoamingMain {

	static UnregulatedMotor motorA = new UnregulatedMotor(MotorPort.A);
	static UnregulatedMotor motorB = new UnregulatedMotor(MotorPort.B);
	static TouchSensor touch = new TouchSensor(SensorPort.S3);

	public static void main(String[] args) {

		Sound.beepSequenceUp(); // make sound when ready.

		System.out.println("Druk om een knop om fikkie te laten zwerven");

		Button.waitForAnyPress();

		Sound.playSample(new File("dog_bark6.wav"), Sound.VOL_MAX);
		motorA.setPower(100);
		motorB.setPower(100);

		// poort initialiseren
		Port port = LocalEV3.get().getPort("S2");

		// Maak een object van de sensor
		SensorModes sensorIR = new EV3IRSensor(port);

		// Get an instance of this sensor in measurement mode
		SampleProvider distance = sensorIR.getMode("Distance");

		while (Button.ESCAPE.isUp()) {

			Delay.msDelay(1000);

			motorA.forward();
			motorB.forward();
			motorA.setPower(80);
			motorB.setPower(80);

			// stack a filter on the sensor that gives the running average of the last 5
			// samples
			SampleProvider average = new MeanFilter(distance, 5);

			// Initialize an array of floats for fetching samples
			float[] sample = new float[average.sampleSize()];

			// Fetch a sample
			average.fetchSample(sample, 0);

			int dist = (int) sample[0];

			while (dist < 35 && Button.ESCAPE.isUp()) {
				// TODO maak een switch case om een modus te kiezen wanneer er iets in de sensor gedetecteerd wordt.
				
				switch (makeRandom()) {
				case 1: // zet 'm naar links
					motorA.forward();
					motorA.setPower(60);
					motorB.setPower(20);
					break;
					//zet 'm naar rechts
				case 2:
					motorB.forward();
					motorB.setPower(60);
					motorA.setPower(20);
					break;
				case 3:
					// blaf
					Sound.playSample(new File("dog_bark6.wav"), Sound.VOL_MAX);
					break;
					//TODO wag tail
				case 4:
					
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

		// Stop motors with brakes on.
		motorA.stop();
		motorB.stop();

		// Free up resources.
		motorA.close();
		motorB.close();

		Sound.playSample(new File("dog_bark6.wav"), Sound.VOL_MAX);// Finish programme
	}

	public static int makeRandom() {
		int random = (int) (Math.random() * 3) + 1;
		return random;
	}

}
