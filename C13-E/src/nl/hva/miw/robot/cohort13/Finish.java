package nl.hva.miw.robot.cohort13;

public class Finish {

	private int aantalFinishPassages;
	private Scherm scherm;

	public Finish(Hardware hardware) {
		super();
		this.scherm = hardware.getScherm();
	}

	/**
	 * Deze methode hoogt het aantal passages op als de vorige meting finish was en
	 * de nieuwe meting niet.
	 * 
	 * @param lijnvolger:
	 *            deze is nodig om de motorsnelheden te zetten als fikkie de
	 *            start/finish overkomt. Het stellen van deze snelheden zorgt ervoor
	 *            dat Fikkie een kleine bocht naar rechts maakt om te voorkomen dat
	 *            ie over de zwarte lijn oversteekt. 
	 */
	public void setAantalFinishPassages(LichtsensorMeting finishmeting, Lijnvolger lijnvolger) {
		finishmeting.nieuweMetingWordtOudeMeting();
		finishmeting.meetKleurRGB();
		finishmeting.meetIntensiteit();

		boolean oudeFinishMetingIsFinish = this.isFinishKleur(finishmeting.getOudeR(), finishmeting.getOudeG(),
				finishmeting.getOudeI());
		boolean nieuweFinishMetingIsFinish = this.isFinishKleur(finishmeting.getR(), finishmeting.getG(),
				finishmeting.getI());

		if (oudeFinishMetingIsFinish && !nieuweFinishMetingIsFinish) {
			aantalFinishPassages++;
		}
		if (nieuweFinishMetingIsFinish) {
			lijnvolger.setMotorPowerA(60);
			lijnvolger.setMotorPowerB(40);
		}
	}

	/**
	 * @param r
	 *            meting voor roodwaarde
	 * @param g
	 *            meting voor groenwaarde
	 * @param i
	 *            meting voor intensiteitswaarde
	 * @return true als de waarden overeenkomen met de waarden van de finish.
	 * 
	 *         Er staan magic numbers in de methode. Dit zijn proefondervindelijk
	 *         gevonden waarden. Er is vanwege de leesbaarheid gekozen om geen final
	 *         variabelen aan te maken omdat de waarden uitsluitend hier voorkomen.
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
