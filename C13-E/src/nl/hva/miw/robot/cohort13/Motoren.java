package nl.hva.miw.robot.cohort13;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;
import lejos.hardware.sensor.EV3ColorSensor;


import lejos.hardware.Button;

public class Motoren {

	private int motorSpeedA;
	private int motorSpeedB;
	private int kleurXpassage = 0;
	
	GraphicsLCD LCD = BrickFinder.getDefault().getGraphicsLCD();
	static RegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
	static RegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);

	void moveRobotFwd() {

		LCD.clear();
		// Display on robot screen
		LCD.drawString("Moving Forward", 100, 20, GraphicsLCD.BASELINE | GraphicsLCD.HCENTER);
		// This code will set the speed and move the robot backward for 5 seconds

		while (kleurXpassage < 2) {

			// methode maken voor if met instantiering ColorSensor etc
	//		if (lichtSensorMeting.getColorID() == 0) {
				kleurXpassage++;
				//start stopwatch
			}
			
			// intensiteitmeting nemen
			LichtsensorMeting meting = new LichtsensorMeting();
			meting.meetIntensiteit();
			float lichtIntensiteit = meting.getIntensiteit();
			
			// meegeven aan een methode die berekent hoeveel gestuurd moet worden			
//			berekenSnelheid(lichtIntensiteit);
			leftMotor.setSpeed(motorSpeedA);
			rightMotor.setSpeed(motorSpeedB);
			leftMotor.backward();
			rightMotor.backward();
			
			//rijd 100ms en daarna nieuwe meting
			Delay.msDelay(100);
		
		}

//		leftMotor.stop();
//		rightMotor.stop();
//		//toevoegen SFX
//
//	}

	private void berekenSnelheid(float lichtIntensiteit) {
		//bereken per motor de snelheid
		
		//set per motor de snelheid
		motorSpeedA = 10;
        motorSpeedB =10;
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
