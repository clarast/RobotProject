package nl.hva.miw.robot.cohort13;

import lejos.hardware.BrickFinder;

import lejos.hardware.lcd.GraphicsLCD;

import lejos.hardware.motor.Motor;

import lejos.utility.Delay;

import lejos.robotics.RegulatedMotor;

import lejos.hardware.motor.*;

import lejos.hardware.port.*;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.utility.Delay;

import lejos.hardware.Button;

public class Drive {

	GraphicsLCD LCD = BrickFinder.getDefault().getGraphicsLCD();

	static RegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);

	static RegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);

	private EV3ColorSensor lichtSensor;

	public Drive(EV3ColorSensor lichtSensor) {
		this.lichtSensor = lichtSensor;
	}

	void moverobotbkw() {

		int aantalKeerRood = 0;

		LCD.clear();

		// Display on robot screen

		LCD.drawString("Moving Backward", 100, 20, GraphicsLCD.BASELINE | GraphicsLCD.HCENTER);

		// This code will set the speed and move the robot backward for 5 seconds

		while (aantalKeerRood < 2) {

			// methode maken voor if
			if (lichtSensor.getColorID() == 0) {
				aantalKeerRood++;
			}
			// intensiteitmeting nemen
			// meegeven aan een methode die berekent hoeveel gestuurd moet worden
			leftMotor.setSpeed(1);

			rightMotor.setSpeed(1);

			leftMotor.backward();

			rightMotor.backward();

		}

		leftMotor.stop();

		rightMotor.stop();

	}

}

//	void moverobotfwd() {
//
//		LCD.clear();
//
//		// Display on robot screen
//
//		LCD.drawString("Moving Forward", 100, 20, GraphicsLCD.BASELINE | GraphicsLCD.HCENTER);
//
//		while (true) {
//
//			leftMotor.setSpeed(400);
//
//			rightMotor.setSpeed(400);
//
//			leftMotor.forward();
//
//			rightMotor.forward();
//
//			if (Button.readButtons() != 0) {
//
//				leftMotor.stop();
//
//				rightMotor.stop();
//
//				System.exit(1);
//
//			}
//
//		}
//
//	}
