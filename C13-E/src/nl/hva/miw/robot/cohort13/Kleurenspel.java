package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Kleurenspel {

	private int kleurNummer;
	private int[] kleuren = new int[4];
	private int motorPowerA;
	private int motorPowerB;

	private Hardware hardware;
	private EV3TouchSensor touchSensor;
	private Scherm scherm;
	private GeluidSpeler geluidspeler;
	private UnregulatedMotor motorA;
	private UnregulatedMotor motorB;
	private EV3MediumRegulatedMotor motorC;
	private EV3ColorSensor kleurSensor;

	public Kleurenspel(Hardware hardware, EV3TouchSensor touchSensor, UnregulatedMotor motorA, UnregulatedMotor motorB,
			EV3MediumRegulatedMotor motorC, Scherm scherm, GeluidSpeler geluidspeler) {
		this.hardware = hardware;
		this.kleurSensor = hardware.maakLichtsensor();
		this.motorA = motorA;
		this.motorB = motorB;
		this.motorC = motorC;
		this.touchSensor = touchSensor;
		this.scherm = scherm;
		this.geluidspeler = geluidspeler;
	}

	public void startKleurenspel() {

			scherm.printKoekje();
			Sound.beepSequenceUp();
			Delay.msDelay(500);

			for (int meting = 0; meting < kleuren.length; meting++) {
				scherm.plaatsKoekje();
				Button.ENTER.waitForPress();
				kleuren[meting] = kleurMeting();
				// LED kleurtje
				scherm.koekjeGegeten();
			}
			reeksUitvoer(kleuren);
			hardware.sluitLichtSensor();
			scherm.printOgen();
			Delay.msDelay(500);
		}

	// deze methode verhuizen naar LichtsensorMeting?
	public int kleurMeting() {
		SensorMode kleur = kleurSensor.getColorIDMode();
		float[] sample = new float[kleur.sampleSize()];
		kleur.fetchSample(sample, 0);
		this.kleurNummer = (int) sample[0];
		return kleurNummer;
	}

	public void reeksUitvoer(int[] kleuren) {
		for (int kleur = 0; kleur < kleuren.length; kleur++) {

			switch (kleuren[kleur]) {

			case 0: // = rood = draaien links / rechts
				wiggleWiggle();
				break;
			case 1: // = groen = deuntje
				Sound.beepSequenceUp();
				break;
			case 2: // = blauw = kwispel
				kwispel();
				break;
			case 3: // = geel = draaien op de plaats
				maakDraai();
				break;
			}
		}

	}

	private void wiggleWiggle() {
		motorA.setPower(100);
		motorB.setPower(10);
		motorA.forward();
		motorB.backward();
		Delay.msDelay(500);
		motorA.backward();
		motorB.forward();
		Delay.msDelay(500);
		motorA.stop();
		motorB.stop();

	}

	public void maakDraai() {
		motorA.setPower(75);
		motorB.setPower(100);
		motorA.forward();
		motorB.backward();
		Delay.msDelay(5000);
		motorA.stop();
		motorB.stop();
	}

	public void kwispel() {

		for (int aantalKeer = 0; aantalKeer < 3; aantalKeer++) {
			motorC.setSpeed(600);
			motorC.rotateTo(45);
			Delay.msDelay(1);
			motorC.rotateTo(-45);
		}
		motorC.rotateTo(0);
	}

	public boolean isTouched() {
		SampleProvider touch = touchSensor.getTouchMode();
		float[] sample = new float[touch.sampleSize()];

		touch.fetchSample(sample, 0);

		if (sample[0] == 0)
			return false;
		else
			return true;
	}
}
