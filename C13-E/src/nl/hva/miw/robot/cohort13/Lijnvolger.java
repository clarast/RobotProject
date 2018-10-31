package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.utility.Delay;
import lejos.hardware.sensor.EV3ColorSensor;

public class Lijnvolger {

	private GeluidSpeler geluidspeler;
	private EV3ColorSensor lichtSensor;
	private Scherm scherm;
	private UnregulatedMotor motorA;
	private UnregulatedMotor motorB;
	private int motorPowerA;
	private int motorPowerB;
	private KopLampen koplampen;

	private final double INTENSITEIT_DREMPEL_LAAG1 = 0.15;
	private final double INTENSITEIT_DREMPEL_LAAG2 = 0.30;
	private final double INTENSITEIT_RICHTWAARDE = 0.33;
	private final double INTENSITEIT_DREMPEL_HOOG1 = 0.35;
	private final double INTENSITEIT_DREMPEL_HOOG2 = 0.40;

	private Tijdswaarneming tijdswaarneming = new Tijdswaarneming();
	private LichtsensorMeting lijnMeting;
	private LichtsensorMeting finishMeting;
	private Finish finish;

	/**
	 * @param hardware: als deze wordt doorgegeven kunnen motors, sensors en scherm
	 *        aan worden gesloten. Ook worden in de constructor objecten van
	 *        lichtsensormeting en finish aangemaakt, zodat deze respectievelijk
	 *        gebruikt kunnen worden om te meten of Fikkie goed loopt en of hij de
	 *        finish passeert.
	 */
	public Lijnvolger(Hardware hardware) {
		this.geluidspeler = hardware.maakGeluidSpeler();
		this.motorA = hardware.maakMotorA();
		this.motorB = hardware.maakMotorB();
		this.lichtSensor = hardware.maakLichtsensor();
		this.scherm = hardware.maakScherm();
		lijnMeting = new LichtsensorMeting(lichtSensor);
		finishMeting = new LichtsensorMeting(lichtSensor);
		finish = new Finish(scherm);
		this.koplampen = new KopLampen();
	}

	/**
	 * Deze methode laat Fikkie de tijdrit lopen zolang hij in de while loop blijft.
	 * Hij kan hier uit geraken door de finish twee keer te passeren (een keer bij
	 * start en een keer bij finish) of als er op escape wordt gedrukt. De stopwatch
	 * wordt bij de eerste passage gestart, stopgezet enw eergegeven bij de tweede
	 * passage.
	 */
	public void tijdrit() {
		this.koplampen.groenPulse();
		scherm.klaarVoorTijdrit();
		Button.ENTER.waitForPress();
		scherm.printOgen();
		geluidspeler.speelWelkomstBlaf();
		boolean stopwatchStarted = false;
		while (finish.getAantalFinishPassages() < 2 && Button.ESCAPE.isUp()) {
			finish.setAantalFinishPassages(finishMeting);
			lijnMeting.meetIntensiteit();
			this.bepaalTypeBocht();
			this.rijden();
			if (finish.getAantalFinishPassages() == 1 && !stopwatchStarted) {
				tijdswaarneming.startStopwatch();
				stopwatchStarted = true;
			}
		}

		beëindigTijdrit();
	}

	/**
	 * In deze methode wordt afhankelijk van de gemeten intensiteit bepaald of er
	 * (i) een "draaiOpPlek" bocht nodig is, (ii) een "flauweBocht of (iii) dat er
	 * "rechtdoor" gelopen kan worden.
	 */
	public void bepaalTypeBocht() {
		if (lijnMeting.getI() > INTENSITEIT_DREMPEL_HOOG2 || lijnMeting.getI() < INTENSITEIT_DREMPEL_LAAG1) {
			this.draaiOpDePlek();
		} else if (lijnMeting.getI() > INTENSITEIT_DREMPEL_HOOG1 || lijnMeting.getI() < INTENSITEIT_DREMPEL_LAAG2) {
			this.flauweBocht();
		} else
			this.rechtdoor();
	}

	/**
	 * Bij deze methode wordt de snelheid en richting van de motoren gezet voor het
	 * type bocht "draaiOpPlek". Er staan magic numbers in de methode. Dit zijn
	 * proefondervindelijk gevonden waarden. Er is vanwege de leesbaarheid gekozen
	 * om geen final variabelen aan te maken omdat de waarden uitsluitend hier
	 * voorkomen.
	 */
	public void draaiOpDePlek() {
		this.motorPowerA = 35;
		this.motorPowerB = 30;
		if (lijnMeting.getI() > INTENSITEIT_DREMPEL_HOOG2) {
			koplampen.groenConstant();
			motorA.backward();
			motorB.forward();
		} else {
			koplampen.roodConstant();
			this.motorPowerB = 45; // bij intense zwartmeting extra krachtig (tov afwijking in wit) wegsturen om
									// ongewenst "oversteken" van de lijn bij haakse bocht te voorkomen
			motorA.forward();
			motorB.backward();
		}
	}

	/**
	 * Bij deze methode wordt de snelheid en richting van de motoren gezet voor het
	 * type bocht "flauweBocht". Er staan magic numbers in de methode. Dit zijn
	 * proefondervindelijk gevonden waarden. Er is vanwege de leesbaarheid gekozen
	 * om geen final variabelen aan te maken omdat de waarden uitsluitend hier
	 * voorkomen.
	 */
	public void flauweBocht() {
		motorA.forward();
		motorB.forward();
		if (lijnMeting.getI() > INTENSITEIT_RICHTWAARDE) {
			koplampen.groenConstant();
			this.motorPowerA = 20;
			this.motorPowerB = 40;
		} else {
			koplampen.roodConstant();
			this.motorPowerA = 40;
			this.motorPowerB = 20;
		}
	}

	/**
	 * Bij deze methode wordt de snelheid en richting van de motoren gezet voor
	 * rechtdoor rijden. Er staan magic numbers in de methode. Dit zijn
	 * proefondervindelijk gevonden waarden. Er is vanwege de leesbaarheid gekozen
	 * om geen final variabelen aan te maken omdat de waarden uitsluitend hier
	 * voorkomen.
	 */
	public void rechtdoor() {
		koplampen.oranjeConstant();
		motorA.forward();
		motorB.forward();
		this.motorPowerA = 40;
		this.motorPowerB = 40;
	}

	/**
	 * Als de metingen zijn gedaan en de snelheid van de motoren zijn gezet wordt
	 * deze methode aangeroepen. Fikkie rijdt dan 100 ms voordat er een nieuwe
	 * meting wordt gedaan.
	 */
	public void rijden() {
		motorA.setPower(motorPowerA);
		motorB.setPower(motorPowerB);
		Delay.msDelay(100);
	}

	private void beëindigTijdrit() {
		tijdswaarneming.stopStopwatch();
		koplampen.roodKnipper();
		scherm.printKnipOog();
		motorA.stop();
		motorB.stop();
		scherm.printRondeTijd(tijdswaarneming.toString());
		Button.ENTER.waitForPress();
		scherm.schoonScherm();
	}
}
