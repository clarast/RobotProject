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
import lejos.utility.Delay;
import lejos.hardware.Button;

public class Fikkie {

	Brick brick;
	private GraphicsLCD LCD;
	private UnregulatedMotor motorA;
	private UnregulatedMotor motorB;
	private EV3MediumRegulatedMotor motorC;
	private EV3ColorSensor lichtSensor;
	private GeluidSpeler geluidspeler;
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
		Button.LEDPattern(3);
		geluidspeler.speelWelkomstBlaf();
		//Lijnvolger lijnvolger = new Lijnvolger(motorA, motorB, lichtSensor, scherm);
		//oefenen thread geluid afspelen tijdens dollen
		GeluidSpeler geluidje = new GeluidSpeler();
		System.out.println("Hoi");
		Delay.msDelay(1000);
		Thread draadje = new Thread(geluidje);
		draadje.start();
		//System.out.println(draadje.getName());
		System.out.println("Doe je dit");
		//System.out.println(draadje.isAlive());
		Delay.msDelay(1000);
		//hieronder gaat bestaande code verder
		//lijnvolger.tijdrit();
		motorA.close();
	    motorB.close();
		Dollen dollen = new Dollen(motorA, motorB, motorC, infraroodSensor, touchSensor, scherm);
		lichtSensor.close();
		dollen.startDollen();
		sluitenMotorenSensors();
		//exception en joinen van thread 
		try {
			draadje.join();
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
	}

	private void aansluitenMotorsEnSensors() {
		LCD = BrickFinder.getDefault().getGraphicsLCD();
		scherm = new Scherm(LCD);
		geluidspeler = new GeluidSpeler();
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
