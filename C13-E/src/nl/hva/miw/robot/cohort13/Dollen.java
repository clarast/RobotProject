package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;

/**
 * @author BR In deze klassen gaat fikkie in de dollen modus. Er wordt
 *         willekeurig een actie gekozen en hij kan objecten ontwijken, op basis
 *         van zijn infraroodsensor ontwijkt fikkie ook objecten. Vanuit deze
 *         modus kan, als fikkie op zijn touchsensor wordt gedrukt, het balspel
 *         of het kleurenspel worden uitgevoerd.
 *
 */
public class Dollen {

	private Hardware hardware;
	private UnregulatedMotor motorA;
	private UnregulatedMotor motorB;
	private EV3MediumRegulatedMotor motorC;
	private EV3IRSensor infraroodSensor;
	private EV3TouchSensor touchSensor;
	private Scherm scherm;
	private GeluidSpeler geluidspeler;
	private MelodieSpeler melodieSpeler;
	private KopLampen koplampen;

	private SampleProvider distance;
	private SampleProvider average;
	private SampleProvider touch;
	private float[] sample;
	private float[] sample2;
	private int dist;

	private int counter = 0;
	private int spelTel = 1;
	private final int SAMPLE_LENGTH = 5;
	private final int NUMBER_OF_AVOIDS = 4;
	private final int NUMBER_OF_ACTIONS = 5;
	private final int START_NUMBER = 1;
	private final int MAX_DEGREES = 60;
	private final int START_DEGREES = 30;
	private final int START_KWISPELS = 2;
	private final int MAX_KWISPELS = 3;
	private final int STARTPOINT_TAIL = 0;
	private final int ACTION_1 = 1;
	private final int ACTION_2 = 2;
	private final int ACTION_3 = 3;
	private final int ACTION_4 = 4;
	private final int AVOID_SPEED_HIGH = 100;
	private final int AVOID_SPEED_LOW = 50;

	/**
	 * 
	 * @param hardware: Hier krijgt de constructor een instantie van hardware mee
	 *        vanuit de main method. Vervolgens kunnen daar allerlei methoden op
	 *        worden aangeroepen om de verschillende andere objecten binnen te
	 *        halen.
	 */
	public Dollen(Hardware hardware) {
		super();
		this.hardware = hardware;
		this.motorA = hardware.maakMotorA();
		this.motorB = hardware.maakMotorB();
		this.motorC = hardware.maakMotorC();
		this.infraroodSensor = hardware.maakInfraroodSensor();
		this.touchSensor = hardware.maakTouchSensor();
		this.scherm = hardware.maakScherm();
		this.geluidspeler = hardware.maakGeluidSpeler();
		this.koplampen = new KopLampen();
		this.melodieSpeler = new MelodieSpeler();
		this.distance = infraroodSensor.getDistanceMode();
		this.touch = touchSensor.getTouchMode();
		this.average = new MeanFilter(distance, SAMPLE_LENGTH);
		this.sample = new float[touch.sampleSize()];
		this.sample2 = new float[average.sampleSize()];

	}

	/**
	 * Hier wordt het dollen gestart. Zolang er niet op ESCAPE wordt gedrukt blijft
	 * hij dollen daarnaast worden er elke keer metingen genomen om te kijken of er
	 * een object een straal van 50cm van de infraroodsensor is. Is dit het geval,
	 * dan kiest de robot een random ontwijk actie. Zolang de afstand groter is dan
	 * 50cm dan kiest de robot een random actie om uit te voeren.
	 */
	public void startDollen() {
		welkom();

		// verkrijg een instantie van de afstandsmodus

		while (Button.ESCAPE.isUp()) {

			fetchSample();

			while (dist > 50 && Button.ESCAPE.isUp()) {
				counter++;
				if (isTouched()) {
					startSpel();
					counter = 0;
					break;
				}

				motorsFwd();
				fetchSample();
				if (counter == 10) {
					// switch case om een modus te kiezen wanneer er iets gedetecteerd wordt.
					chooseAction();
					counter = 0;
				}
			}

			chooseAvoid();
		}
		stopMotors();
		afscheid();

	}

	public void afscheid() {
		koplampen.kleurenWisselKort();
		geluidspeler.speelBlaf3x();
	}

	public void startSpel() {
		stopMotors();
		koplampen.oranjeKnipper();
		spelTel = startSpel(spelTel);
		Sound.twoBeeps();

	}

	public void welkom() {
		initiateDollen();
		drawLCD();
		geluidspeler.speelWelkomstBlaf();
		koplampen.kleurenWisselKortKnipper();
	}

	public void fetchSample() {
		average.fetchSample(sample2, 0);
		dist = (int) sample2[0];
	}

	public void motorsFwd() {
		motorA.setPower(50);
		motorB.setPower(50);
		motorA.forward();
		motorB.forward();
		Delay.msDelay(100);
	}

	private void chooseAvoid() {

		switch (makeRandomNumber(ACTION_1)) {
		case 1:
			// ontwijk rechts
			avoidRight();
			break;
		case 2:
			// ontwijk links
			avoidLeft();
			break;
		case 3:
			// draai op de plaats links
			fullTurnLeft();
			break;
		case 4:
			// draai op de plaats rechts
			fullTurnRight();
			break;
		}
	}

	/**
	 * 
	 * @param spelTel: de teller van het spel is in het eerste geval op 1 gezet
	 *        zodat als eerste het kleurenspel gestart wordt. Vervolgens wordt
	 *        spelTel op 2 gezet.
	 * @return speltel wordt gereturned naar de speltel in de methode startDollen.
	 *         Daar wordt het opgeslagen in een hulpvariabele en vervolgens weer in
	 *         de methode gestopt als parameter. Zo blijft het zo dat de spellen
	 *         afwisselend worden afgespeeld.
	 */
	private int startSpel(int spelTel) {
		if (spelTel == 1) {
			maakKleurenSpel();
			spelTel = 2;
		} else if (spelTel == 2) {
			maakBalspel();
			spelTel = 1;
		}
		return spelTel;

	}

	public void maakBalspel() {
		BalSpel balspel = new BalSpel(hardware, motorA, motorB, infraroodSensor, touchSensor, scherm,geluidspeler);
		balspel.findBall();
	}

	public void maakKleurenSpel() {
		Kleurenspel kleurenspel = new Kleurenspel(hardware, touchSensor, motorA, motorA, motorC, scherm, geluidspeler,
				melodieSpeler, koplampen);
		kleurenspel.startKleurenspel();
	}

	private void initiateDollen() {
		Sound.beepSequence(); // make sound when ready.
		scherm.printTekst("Druk op de knop!");
		Button.waitForAnyPress();
	}

	/**
	 * deze methode zorgt neemt een sample of de touchsensor is aangeraakt.
	 * 
	 * @return true als de sensor is aangeraakt.
	 */
	public boolean isTouched() {
		touch.fetchSample(sample, 0);

		if (sample[0] == 0)
			return false;
		else
			return true;
	}

	private void drawLCD() {
		scherm.printOgen();
	}

	private void chooseAction() {
		switch (makeRandomNumber(ACTION_4)) {
		case 1: // ga naar links
			avoidLeft();
			break;
		case 2:
			// ga naar links
			avoidLeft();
			break;
		case 3:
			// blaf
			bark();
			break;
		case 4:
			// wag tail
			wagTail();
			break;
		case 5:
			// draai op de plaats.
			fullTurnLeft();
			fullTurnLeft();
			break;
		}
	}

	private void bark() {
		geluidspeler.speelWelkomstBlaf();
	}

	private void stopMotors() {
		// stop de motoren
		motorA.stop();
		motorB.stop();
		motorC.stop();
	}

	/**
	 * 
	 * @param actie: krijgt van de methoden die een random nummer nodig hebben een
	 *        int actie zodat bepaald kan worden welke actie gekozen kan worden. er
	 *        bestaat een random nummer voor ontwijken, aantal graden kwispelen,
	 *        aantal acties.
	 * 
	 *
	 * @return geeft het gewenste random nummer terug.
	 */
	public int makeRandomNumber(int actie) {
		int random = 0;
		if (actie == ACTION_1) {
			random = (int) (Math.random() * NUMBER_OF_AVOIDS) + START_NUMBER;
		} else if (actie == ACTION_2) {
			random = (int) (Math.random() * MAX_DEGREES) + START_DEGREES;
		} else if (actie == ACTION_3) {
			random = (int) (Math.random() * MAX_KWISPELS) + START_KWISPELS;
		} else if (actie == ACTION_4) {
			random = (int) (Math.random() * NUMBER_OF_ACTIONS) + START_KWISPELS;
		}
		return random;
	}

	public void wagTail() {
		/**
		 * kwispel de staart een willekeurig aantal keer, met een willekeurig aantal
		 * graden, daarna weer terug naar het startpunt.
		 */
		for (int aantalKeer = 0; aantalKeer < makeRandomNumber(3); aantalKeer++) {
			motorC.setSpeed(600);
			motorC.rotateTo(makeRandomNumber(ACTION_2));
			Delay.msDelay(1);
			motorC.rotateTo(-makeRandomNumber(ACTION_2));
		}
		motorC.rotateTo(STARTPOINT_TAIL);
		// make random
	}

	public void avoidLeft() {

		motorB.setPower(AVOID_SPEED_HIGH);
		motorA.setPower(AVOID_SPEED_LOW);
		motorA.backward();
		motorB.forward();
		Delay.msDelay(800);
	}

	public void avoidRight() {

		motorA.setPower(AVOID_SPEED_HIGH);
		motorB.setPower(AVOID_SPEED_LOW);
		motorB.backward();
		motorA.forward();
		Delay.msDelay(800);
	}

	public void fullTurnRight() {
		motorA.setPower(AVOID_SPEED_HIGH);
		motorB.setPower(AVOID_SPEED_LOW);
		motorB.backward();
		motorA.forward();
		Delay.msDelay(1100);
	}

	public void fullTurnLeft() {
		motorB.setPower(AVOID_SPEED_HIGH);
		motorA.setPower(AVOID_SPEED_LOW);
		motorA.backward();
		motorB.forward();
		Delay.msDelay(1100);
	}

}
