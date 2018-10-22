package nl.hva.miw.robot.cohort13;

import java.io.File;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;

public class Fikkie {

	Brick brick;
	private UnregulatedMotor motorA;
	private UnregulatedMotor motorB;
	private EV3ColorSensor lichtSensor;
	private GraphicsLCD LCD;

	public Fikkie() {
		super();
		brick = LocalEV3.get();
	}

	public static void main(String[] args) {
		Fikkie fikkie = new Fikkie();
		fikkie.run();
	}

	private void run() {
		this.aansluitenMotorsEnSensors();
		
		
		
//		Sound.playSample(new File("dog_bark6.wav"), Sound.VOL_MAX);
		Lijnvolger lijnvolger = new Lijnvolger(motorA, motorB, lichtSensor, LCD);
		lijnvolger.tijdrit();
	//	lijnvolger.lichttest();
		motorA.close();
		motorB.close();
		lichtSensor.close();
		
	}

	private void aansluitenMotorsEnSensors() {
		LCD = BrickFinder.getDefault().getGraphicsLCD();
		motorA = new UnregulatedMotor(MotorPort.A);
		motorB = new UnregulatedMotor(MotorPort.B);
		brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1");
		lichtSensor = new EV3ColorSensor(s1);
	}
}
