package nl.hva.miw.robot.cohort13;

public class Finish {

	private int aantalFinishPassages;
	private Scherm scherm;

	public Finish(Scherm scherm) {
		super();
		this.scherm = scherm;

	}

	/**
	 * Deze methode hoogt het aantal passages op als de vorige meting finish was en de nieuwe meting niet.
	 */
	public void setAantalFinishPassages(LichtsensorMeting finishmeting) {
		finishmeting.nieuweMetingWordtOudeMeting();
		finishmeting.meetKleurRGB();
		finishmeting.meetIntensiteit();

		boolean oudeFinishMetingIsFinish = this.isFinishKleur(finishmeting.getOudeR(), finishmeting.getOudeG(),
				finishmeting.getOudeI());
		boolean nieuweFinishMetingIsFinish = this.isFinishKleur(finishmeting.getR(), finishmeting.getG(),
				finishmeting.getI());

		if (oudeFinishMetingIsFinish && !nieuweFinishMetingIsFinish) {
			aantalFinishPassages++;
			scherm.printKleurpassage();
		}
	}

	/**
	 * @param r meting voor roodwaarde
	 * @param g meting voor groenwaarde
	 * @param i meting voor intensiteitswaarde
	 * @return true als de waarden overeenkomen met de waarden van de finish.
	 * 
	 * Er staan magic numbers in de methode. Dit zijn
	 * proefondervindelijk gevonden waarden. Er is vanwege de leesbaarheid gekozen
	 * om geen final variabelen aan te maken omdat de waarden uitsluitend hier
	 * voorkomen.
	 */
	public boolean isFinishKleur(double r, double g, double i) {
		if (r > 0.10 && g < 0.10 && i > 0.20) {
			return true;
		} else
			return false;
	}

	public int getAantalFinishPassages() {
		return aantalFinishPassages;
	}

}
