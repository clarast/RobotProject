package nl.hva.miw.robot.cohort13;

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
		lijnvolger.tijdrit();
		Dollen dollen = new Dollen(hardware);
		dollen.startDollen();
	}
}
