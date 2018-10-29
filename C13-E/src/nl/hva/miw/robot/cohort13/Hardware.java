package nl.hva.miw.robot.cohort13;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;

public class Hardware {

	private Brick brick;
	private UnregulatedMotor motorA;
	private UnregulatedMotor motorB;
	private EV3MediumRegulatedMotor motorC;
	private EV3ColorSensor lichtSensor;
	private Scherm scherm;
	private EV3IRSensor infraroodSensor;
	private EV3TouchSensor touchSensor;
	private GraphicsLCD LCD;
	private GeluidSpeler geluidSpeler;
	private Port s1;
	private Port s2;
	private Port s3;
	
	

	public Hardware() {
		this.brick = LocalEV3.get();
		this.brick = BrickFinder.getDefault();
		this.s1 = brick.getPort("S1");
		this.s2 = brick.getPort("S2");
		this.s3 = brick.getPort("S3");
	}

	public UnregulatedMotor maakMotorA() {
		this.motorA = new UnregulatedMotor(MotorPort.A);
		return motorA;
	}

	public UnregulatedMotor maakMotorB() {
		this.motorB = new UnregulatedMotor(MotorPort.B);
		return motorB;
	}

	public EV3MediumRegulatedMotor maakMotorC() {
		this.motorC = new EV3MediumRegulatedMotor(MotorPort.C);
		return motorC;
	}

	public EV3ColorSensor maakLichtsensor() {
		this.lichtSensor = new EV3ColorSensor(s1);
		return lichtSensor;
	}

	public Scherm maakScherm() {
		this.LCD = BrickFinder.getDefault().getGraphicsLCD();
		this.scherm = new Scherm(LCD);
		return scherm;
	}

	public EV3IRSensor maakInfraroodSensor() {
		this.infraroodSensor = new EV3IRSensor(s2);
		return infraroodSensor;
	}

	public EV3TouchSensor maakTouchSensor() {
		this.touchSensor = new EV3TouchSensor(s3);
		return touchSensor;
	}

	public GeluidSpeler maakGeluidSpeler() {
		this.geluidSpeler = new GeluidSpeler();
		return geluidSpeler;
	}

}