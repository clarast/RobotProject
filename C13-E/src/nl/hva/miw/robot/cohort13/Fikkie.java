package nl.hva.miw.robot.cohort13;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;


public class Fikkie {

	Brick brick;
	private UnregulatedMotor motorA;
	private UnregulatedMotor motorB;
	private EV3MediumRegulatedMotor motorC;
	private EV3ColorSensor lichtSensor;
	private GraphicsLCD LCD;
	private Scherm scherm;
	private EV3IRSensor infraroodSensor;
	private EV3TouchSensor touchSensor;


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
		Lijnvolger lijnvolger = new Lijnvolger(motorA, motorB, lichtSensor, scherm);
		//lijnvolger.tijdrit();
		//motorA.close();
		//motorB.close();
		Dollen dollen = new Dollen(motorA, motorB, motorC, infraroodSensor, touchSensor, scherm);
		//lijnvolger.tijdrit();
		lichtSensor.close();
		dollen.startDollen();
		sluitenMotorenSensors();

	}

	private void aansluitenMotorsEnSensors() {
		LCD = BrickFinder.getDefault().getGraphicsLCD();
		scherm = new Scherm(LCD);
		motorA = new UnregulatedMotor(MotorPort.A);
		motorB = new UnregulatedMotor(MotorPort.B);
		motorC = new EV3MediumRegulatedMotor(MotorPort.C);
		brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1");
		Port s2 = brick.getPort("S2");
		Port s3 = brick.getPort("S3");
		lichtSensor = new EV3ColorSensor(s1);
		infraroodSensor = new EV3IRSensor(s2);
		touchSensor = new EV3TouchSensor(s3);
		
	}

	private void sluitenMotorenSensors() {
		motorA.close();
		motorB.close();
		motorC.close();
		lichtSensor.close();
		infraroodSensor.close();
		touchSensor.close();
	}
}
