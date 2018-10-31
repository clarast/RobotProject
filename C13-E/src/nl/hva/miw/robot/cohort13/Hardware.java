package nl.hva.miw.robot.cohort13;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.LocalEV3;
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
	private KopLampen koplampen;
	private Port s1;
	private Port s2;
	private Port s3;
	private Geluid melodieSpeler;

	public Hardware() {
		this.brick = LocalEV3.get();
		this.brick = BrickFinder.getDefault();
		this.s1 = brick.getPort("S1");
		this.s2 = brick.getPort("S2");
		this.s3 = brick.getPort("S3");
		this.motorA = new UnregulatedMotor(MotorPort.A);
		this.motorB = new UnregulatedMotor(MotorPort.B);
		this.motorC = new EV3MediumRegulatedMotor(MotorPort.C);
		this.lichtSensor = new EV3ColorSensor(s1);
		this.scherm = new Scherm(BrickFinder.getDefault().getGraphicsLCD());
		this.infraroodSensor = new EV3IRSensor(s2);
		this.touchSensor = new EV3TouchSensor(s3);
		this.koplampen = new KopLampen();
		this.melodieSpeler = new Geluid();
	}

	public KopLampen getKoplampen() {
		return this.koplampen;
	}
	
	public UnregulatedMotor getMotorA() {
		return this.motorA;
	}

	public UnregulatedMotor getMotorB() {
		return this.motorB;
	}

	public EV3MediumRegulatedMotor getMotorC() {
		return this.motorC;
	}

	public EV3ColorSensor getLichtsensor() {
		return this.lichtSensor;
	}

	public Scherm getScherm() {
		return this.scherm;
	}

	public EV3IRSensor getInfraroodSensor() {
		return this.infraroodSensor;
	}

	public EV3TouchSensor getTouchSensor() {
		return this.touchSensor;
	}

	public Geluid getMelodieSpeler() {
		return this.melodieSpeler;
	}
	
	public void sluitMotorA() {
		this.motorA.close();
	}

	public void sluitMotorB() {
		this.motorB.close();
	}

	public void sluitMotorC() {
		this.motorC.close();
	}

	public void sluitLichtSensor() {
		this.lichtSensor.close();
	}

	public void sluitTouchsensor() {
		this.touchSensor.close();
	}

	public void sluitInfraroodSensor() {
		this.infraroodSensor.close();
	}



}