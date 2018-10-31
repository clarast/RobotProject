package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;

public class Fikkie {

	public Fikkie() {

	}

	public static void main(String[] args) {
		Fikkie fikkie = new Fikkie();
		fikkie.run();
	}

	private void run() {

		Hardware hardware = new Hardware();
		System.out.println("RECHTS lijnvolger, LINKS voor tijdrit");
		Button.waitForAnyPress();
		if (Button.RIGHT.isDown()) {
			Lijnvolger lijnvolger = new Lijnvolger(hardware);
			lijnvolger.tijdrit();
			hardware.sluitAllesBehalveLichtsensor();
		} else if (Button.LEFT.isDown()) {
			Dollen dollen = new Dollen(hardware);
			dollen.startDollen();
			hardware.sluitAllesBehalveLichtsensor();
		}
	}
}
