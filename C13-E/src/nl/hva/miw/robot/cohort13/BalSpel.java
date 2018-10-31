package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * @author BR Deze klasse wordt vanuit roamingmode aangeroepen. In deze modus
 *         gaat fikkie achter een beacon aan rijden.
 */
public class BalSpel {

	private UnregulatedMotor motorA;
	private UnregulatedMotor motorB;
	private EV3IRSensor infraroodSensor;
	private EV3TouchSensor touchSensor;
	private Hardware hardware;
	private Scherm scherm;
	private GeluidSpeler geluidspeler;
	private SensorMode seek;
	private float[] sample;
	private int direction;
	private int distance;
	private SampleProvider touch;
	private float[] sample2;
	private KopLampen koplampen;

	/**
	 * 
	 * @param hardware: Hij geeft alle hardware door vanuit de dollen klasse.
	 * 
	 */
	public BalSpel(Hardware hardware, UnregulatedMotor motorA, UnregulatedMotor motorB, EV3IRSensor infraroodSensor,
			EV3TouchSensor touchSensor, Scherm scherm, GeluidSpeler geluidspeler) {
		super();
		this.hardware = hardware;
		this.motorA = motorA;
		this.motorB = motorB;
		this.infraroodSensor = infraroodSensor;
		this.touchSensor = touchSensor;
		this.scherm = scherm;
		this.geluidspeler = geluidspeler;
		this.seek = infraroodSensor.getSeekMode();
		this.touch = touchSensor.getTouchMode();
		this.sample = new float[seek.sampleSize()];
		this.sample2 = new float[touch.sampleSize()];
		this.geluidspeler = geluidspeler;
		this.koplampen = new KopLampen();

	}

	/**
	 * In deze methode wordt het balspel gestart. Er wordt eerst een geluid
	 * afgespeeld. allereerst wordt er een meting genomen om te kijken of de beacon
	 * in zijn richting staat. Daar rijdt de robot vervolgens naartoe. Als er op
	 * zijn neus gedrukt wordt zal deze modus beeindigen en zal de robot terugkeren
	 * naar dollen-modus.
	 */
	public void findBall() {
		initiateBalspel();
		ready();
		while (Button.ESCAPE.isUp()) {
			if (isTouched()) {
				break;
			}
			fetchSample();
			setMotorPower();

			if (direction > 5) {
				goRight();
			} else if (direction < -5) {
				goLeft();
			} else {
				if (distance < Integer.MAX_VALUE) {
					geluidspeler.speelWelkomstBlaf();
					goFwd();
				} else {
					stopMotors();
				}
			}
		}
		stopMotors();
	}

	public void goFwd() {
		motorA.forward();
		motorB.forward();
	}

	public void goLeft() {
		motorB.forward();
		motorA.stop();
	}

	public void goRight() {
		motorA.forward();
		motorB.stop();
	}

	public void setMotorPower() {
		motorA.setPower(100);
		motorB.setPower(100);
	}

	public void fetchSample() {
		seek.fetchSample(sample, 0);
		direction = (int) sample[0];
		distance = (int) sample[1];
	}

	/*
	 * Deze methode initieert het balspel, speelt een geluid, print een schermtekst
	 * en wacht tot er op een knop gedrukt wordt.
	 */
	private void initiateBalspel() {
		Sound.beepSequence(); // make sound when ready.
		scherm.printTekst("Druk op de knop!");
		Button.waitForAnyPress();
		if (Button.ESCAPE.isDown())
			System.exit(0);
	}

	/*
	 * deze methode tekent de ogen. Die wordt aangeroepen op de schermklasse.
	 */
	private void ready() {
		scherm.printOgen();
		geluidspeler.speelWelkomstBlaf();
	}

	/**
	 * neem een sample en kijk of de knop is ingdrukt.
	 * 
	 * @return true als de knop ingedrukt wordt.
	 */
	public boolean isTouched() {

		touch.fetchSample(sample2, 0);

		if (sample2[0] == 0)
			return false;
		else
			return true;
	}

	/**
	 * stop de motoren
	 */
	private void stopMotors() {
		// stop de motoren
		motorA.stop();
		motorB.stop();
	}
}