package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;
import lejos.utility.Delay;

public class Fikkie {

	public Fikkie() {

	}

	public static void main(String[] args) {
		Fikkie fikkie = new Fikkie();
		fikkie.run();
	}

	private void run() {

		Hardware hardware = new Hardware();
		Lijnvolger lijnvolger = new Lijnvolger(hardware);
		KopLampen lichtje = new KopLampen();
		lichtje.start();
		MelodieSpeler melodietje = new MelodieSpeler();
		melodietje.setLiedNummer(2);
		melodietje.start();
		lijnvolger.tijdrit();
		
		melodietje.shutDown();

		/*
		 * Dollen dollen = new Dollen(hardware); dollen.startDollen();
		 * melodietje.setLiedNummer(1); melodietje.start();
		 */

		

	}
}
