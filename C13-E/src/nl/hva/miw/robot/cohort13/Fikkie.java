package nl.hva.miw.robot.cohort13;

public class Fikkie {

	public Fikkie() {

	}

	public static void main(String[] args) {
		Fikkie fikkie = new Fikkie();
		fikkie.run();
	}

	private void run() {
		KopLampen k = new KopLampen();
		k.kleurenDemonstratie();
		
		Hardware hardware = new Hardware();
		// Lijnvolger lijnvolger = new Lijnvolger(hardware);
		// KopLampen lichtje = new KopLampen();
		// lichtje.start();
		// lijnvolger.tijdrit();
		Dollen dollen = new Dollen(hardware);
		dollen.startDollen();
		// try {
		// lichtje.join();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}
}