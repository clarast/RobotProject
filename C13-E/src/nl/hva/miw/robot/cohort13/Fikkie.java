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
<<<<<<< HEAD
		// KopLampen lichtje = new KopLampen();
		// lichtje.start();
		lijnvolger.tijdrit();
		//Dollen dollen = new Dollen(hardware);
		//dollen.startDollen();
		// try {
		// lichtje.join();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
=======
		MelodieSpeler melodietest = new MelodieSpeler();
		melodietest.setLiedNummer(1);
		melodietest.start();
		lijnvolger.tijdrit();
		Dollen dollen = new Dollen(hardware);
		dollen.startDollen();
		
>>>>>>> e0226b26cd57046a221a5f1e01f73a51e95454e4
	}
}
