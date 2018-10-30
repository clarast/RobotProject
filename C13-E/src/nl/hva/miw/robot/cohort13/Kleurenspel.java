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
	private MelodieSpeler melodieSpeler;
	private UnregulatedMotor motorA;
	private UnregulatedMotor motorB;
	private EV3MediumRegulatedMotor motorC;
	private EV3ColorSensor kleurSensor;
	private KopLampen koplampen;

	public Kleurenspel(Hardware hardware, EV3TouchSensor touchSensor, UnregulatedMotor motorA, UnregulatedMotor motorB,
			EV3MediumRegulatedMotor motorC, Scherm scherm, GeluidSpeler geluidspeler, MelodieSpeler melodieSpeler,KopLampen koplampen) {
		this.hardware = hardware;
		this.kleurSensor = hardware.maakLichtsensor();
		this.motorA = motorA;
		this.motorB = motorB;
		this.motorC = motorC;
		this.touchSensor = touchSensor;
		this.scherm = scherm;
		this.geluidspeler = geluidspeler;
		this.melodieSpeler = melodieSpeler;
		this.koplampen = koplampen;
	}

	/**
	 * Deze methode wordt aangeroepen vanuit 'Dollen' en voert de sub-methods van
	 * het kleurenspel uit
	 */
	public void startKleurenspel() {

		welkomst();
		neemMetingen();
		reeksUitvoer(kleuren);
		afscheid();
	}

	/**
	 * Deze method neem een reeks kleurmetingen. De gebruiker wordt gevraagd een
	 * 'hondenkoekje' te plaatsen en op de knop te drukken. Daarna wordt de meting
	 * genomen. Wanneer de array vol is stoppen de metingen.
	 */
	private void neemMetingen() {
		
		for (int meting = 0; meting < kleuren.length; meting++) {
			scherm.plaatsKoekje();
			Button.ENTER.waitForPress();
			kleuren[meting] = kleurMeting();
			koplampen.kleurenWisselKort();
			scherm.koekjeGegeten();
		}

	}

	/**
	 * Deze method neemt een enkele kleurmeting.
	 */
	public int kleurMeting() {
		SensorMode kleur = kleurSensor.getColorIDMode();
		float[] sample = new float[kleur.sampleSize()];
		kleur.fetchSample(sample, 0);
		this.kleurNummer = (int) sample[0];
		return kleurNummer;
	}

	/**
	 * Deze methode ontvangt de met kleur ID's gevulde array en voert per waarde een
	 * specifieke actie uit.
	 */
	public void reeksUitvoer(int[] kleuren) {
		for (int kleur = 0; kleur < kleuren.length; kleur++) {

			switch (kleuren[kleur]) {

			case 0: // = rood = schuifelen links / rechts
				schuifelen();
				break;
			case 1: // = groen = deuntje
				melodieSpeler.speelVaderJacob();
				break;
			case 2: // = blauw = kwispel
				kwispel();
				break;
			case 6: // = geelwit = draai op de plaats
				maakDraai();
				break;
			}
		}

	}

	private void schuifelen() {

		for (int i = 0; i < 4; i++) {
			motorA.setPower(100);
			motorA.forward();
			Delay.msDelay(200);
			motorA.stop();
			motorB.setPower(100);
			motorB.forward();
			Delay.msDelay(200);
			motorB.stop();
		}
		motorA.stop();
		motorB.stop();

	}

	public void maakDraai() {
		motorB.setPower(100);
		motorB.forward();
		Delay.msDelay(2500);
		motorB.stop();
	}

	public void kwispel() {

		for (int aantalKeer = 0; aantalKeer < 4; aantalKeer++) {
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

	private void afscheid() {
		hardware.sluitLichtSensor();
		scherm.printOgen();
		Delay.msDelay(500);
	}

	private void welkomst() {
		Sound.beepSequenceUp();
		scherm.printKoekje();
		Delay.msDelay(500);
	}
}
