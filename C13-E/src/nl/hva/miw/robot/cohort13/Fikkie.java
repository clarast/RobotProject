package nl.hva.miw.robot.cohort13;

import lejos.hardware.Brick;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.utility.Delay;

public class Fikkie {

	Brick brick;

	public Fikkie() {
		super();
		brick = LocalEV3.get();
	}

	public static void main(String[] args) {
		Fikkie fikkie = new Fikkie();
		fikkie.run();
	}

	private void run() {
		TextLCD display = brick.getTextLCD();
		display.drawString("Push the button!", 0, 4);
		// Wav file afspelen + LCD kleurtjes => klasse SFX
		waitForKey(Button.ENTER);
		// tienTestMetingenKleur();
		Motoren motor = new Motoren();
		motor.moveRobotFwd();
	}

	public void waitForKey(Key key) {
		while (key.isUp()) {
			Delay.msDelay(100);
		}
		while (key.isDown()) {
			Delay.msDelay(100);
		}
	}

	/**
	 * deze methode is een testmethode (niet functioneel, wordt uiteindelijk niet in
	 * het programma gebruikt) en print 10 kleurmetingen met 5 seconden tussentijd
	 * (sluit programma einde methode).
	 */
	private void tienTestMetingenKleur() {
		LichtsensorMeting kleurtest = new LichtsensorMeting();
		for (int i = 0; i < 10; i++) {
			kleurtest.meetKleur();
			System.out.println(kleurtest.getKleur());
			Delay.msDelay(5000);
		}
		System.exit(1);
	}
}