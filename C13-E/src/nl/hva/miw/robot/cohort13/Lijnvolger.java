package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.utility.Delay;

public class Lijnvolger {

	private Hardware hardware;
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

	private Tijdswaarneming tijdswaarneming;
	private LichtsensorMeting lijnMeting;
	private LichtsensorMeting finishMeting;
	private Finish finish;
	private EV3MediumRegulatedMotor motorC;
	private Geluid melodiespeler;

	/**
	 * @param hardware:
	 *            als deze wordt doorgegeven kunnen motors, sensors en scherm aan
	 *            worden gesloten. Ook worden in de constructor objecten van
	 *            lichtsensormeting en finish aangemaakt, zodat deze respectievelijk
	 *            gebruikt kunnen worden om te meten of Fikkie goed loopt en of hij
	 *            de finish passeert.
	 */
	public Lijnvolger(Hardware hardware) {
		this.hardware = hardware;
		this.motorA = this.hardware.getMotorA();
		this.motorB = this.hardware.getMotorB();
		this.motorC = this.hardware.getMotorC();
		this.scherm = this.hardware.getScherm();
		this.lijnMeting = new LichtsensorMeting(this.hardware);
		this.finishMeting = new LichtsensorMeting(this.hardware);
		this.finish = new Finish(this.hardware);
		this.koplampen = this.hardware.getKoplampen();
		this.melodiespeler = this.hardware.getMelodieSpeler();
		this.tijdswaarneming = new Tijdswaarneming();
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
		this.scherm.klaarVoorTijdrit();
		Button.ENTER.waitForPress();
		this.scherm.printOgen();
		this.melodiespeler.speelWelkomstBlaf();
		boolean stopwatchStarted = false;
		while (this.finish.getAantalFinishPassages() < 2 && Button.ESCAPE.isUp()) {
			this.finish.setAantalFinishPassages(this.finishMeting);
			this.lijnMeting.meetIntensiteit();
			this.bepaalTypeBocht();
			this.rijden();
			if (this.finish.getAantalFinishPassages() == 1 && !stopwatchStarted) {
				this.tijdswaarneming.startStopwatch();
				stopwatchStarted = true;
				this.melodiespeler.start();
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
			this.koplampen.groenConstant();
			this.motorA.backward();
			this.motorB.forward();
		} else {
			this.koplampen.roodConstant();
			this.motorPowerB = 45; // bij intense zwartmeting extra krachtig (tov afwijking in wit) wegsturen om
									// ongewenst "oversteken" van de lijn bij haakse bocht te voorkomen
			this.motorA.forward();
			this.motorB.backward();
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
		this.motorA.forward();
		this.motorB.forward();
		if (this.lijnMeting.getI() > this.INTENSITEIT_RICHTWAARDE) {
			this.koplampen.groenConstant();
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
		this.koplampen.oranjeConstant();
		this.motorA.forward();
		this.motorB.forward();
		this.motorPowerA = 40;
		this.motorPowerB = 40;
	}

	/**
	 * Als de metingen zijn gedaan en de snelheid van de motoren zijn gezet wordt
	 * deze methode aangeroepen. Fikkie rijdt dan 100 ms voordat er een nieuwe
	 * meting wordt gedaan.
	 */
	public void rijden() {
		this.motorA.setPower(motorPowerA);
		this.motorB.setPower(motorPowerB);
		Delay.msDelay(100);
	}

	private void beëindigTijdrit() {
		this.tijdswaarneming.stopStopwatch();
		this.koplampen.roodKnipper();
		this.scherm.printKnipOog();
		this.kwispel();
		this.motorA.stop();
		this.motorB.stop();
		this.scherm.printRondeTijd(tijdswaarneming.toString());
		Button.ENTER.waitForPress();
		this.scherm.schoonScherm();
		this.hardware.sluitLichtSensor();
	}

	public void kwispel() {
		for (int aantalKeer = 0; aantalKeer < 3; aantalKeer++) {
			this.motorC.setSpeed(600);
			this.motorC.rotateTo(45);
			Delay.msDelay(1);
			this.motorC.rotateTo(-45);
		}
		this.motorC.rotateTo(0);
	}

}
