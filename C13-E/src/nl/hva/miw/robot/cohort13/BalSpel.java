package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;

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
	private Geluid melodieSpeler;
	private SensorMode seek;
	private float[] sample;
	private int direction;
	private int distance;
	private SampleProvider touch;
	private float[] sample2;

	/**
	 * 
	 * @param hardware: Hij geeft alle hardware door vanuit de dollen klasse.
	 * 
	 */
	public BalSpel(Hardware hardware) {
		super();
		this.hardware = hardware;
		this.motorA = this.hardware.getMotorA();
		this.motorB = this.hardware.getMotorB();
		this.infraroodSensor = this.hardware.getInfraroodSensor();
		this.touchSensor = this.hardware.getTouchSensor();
		this.scherm = this.hardware.getScherm();
		this.melodieSpeler = this.hardware.getMelodieSpeler();
		this.seek = this.infraroodSensor.getSeekMode();
		this.touch = this.touchSensor.getTouchMode();
		this.sample = new float[seek.sampleSize()];
		this.sample2 = new float[touch.sampleSize()];
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
	public void initiateBalspel() {
		Sound.beepSequence(); // make sound when ready.
		scherm.printHondEnBal();
		Button.waitForAnyPress();
	}

	/*
	 * deze methode tekent de ogen. Die wordt aangeroepen op de schermklasse.
	 */
	public void ready() {
		scherm.printOgen();
		melodieSpeler.speelWelkomstBlaf();
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
	public void stopMotors() {
		// stop de motoren
		motorA.stop();
		motorB.stop();
	}
}