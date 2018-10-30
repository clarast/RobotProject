package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;
import lejos.utility.Delay;

public class KopLampen extends Thread {

	private Thread thread;
	private int delay;

	@Override
	public void run() {
		groenKnipper();
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * Deze methode wordt niet gebruikt in het programma, maar kan worden
	 * aangeroepen om alle kleurfuncties op een rijtje te zien om er bijvoorbeeld
	 * een uit te kiezen.
	 */
	public void kleurenDemonstratie() {		
		System.out.println("random kleurshow");
		randomLichtshow();
		System.out.println("kleurenwisselkort");
		kleurenWisselKort();
		System.out.println("kleurenwisselmedium");
		kleurenWisselMedium();
		System.out.println("kleurenwissellang");
		kleurenWisselLang();

		System.out.println("knipper kleurenwisselkort");
		kleurenWisselKortKnipper();
		System.out.println("knipper kleurenwisselmedium");
		kleurenWisselMediumKnipper();
		System.out.println("knipper kleurenwissellang");
		kleurenWisselLangKnipper();

		System.out.println("steedskorter");
		steedsKorterGroen();
	}

	public void zetLampUit() {
		Button.LEDPattern(0);
	}
	
	public void groenConstant() {
		Button.LEDPattern(1);
	}

	public void roodConstant() {
		Button.LEDPattern(2);
	}

	public void oranjeConstant() {
		Button.LEDPattern(3);
	}

	public void groenKnipper() {
		Button.LEDPattern(4);
	}

	public void roodKnipper() {
		Button.LEDPattern(5);
	}

	public void oranjeKnipper() {
		Button.LEDPattern(6);
	}

	public void groenPulse() {
		Button.LEDPattern(7);
	}

	public void roodPulse() {
		Button.LEDPattern(8);
	}

	public void oranjePulse() {
		Button.LEDPattern(9);
	}

	/**
	 * @return genereert random int van 1-9 om als random patroonnummer te gebruiken
	 *         voor een Delay
	 */
	public int genereerRandomPatroonnummer() {
		return (int) (Math.random() * 9) + 1;
	}

	/**
	 * @return genereert random int van 1-2999 seconden om als random duur te
	 *         gebruiken voor een Delay
	 */
	public int genereerRandomDuur() {
		return (int) (Math.random() * 3000);
	}

	public void randomLichtshow() {
		for (int i = 0; i < 10; i++) {
			Button.LEDPattern(genereerRandomPatroonnummer());
			Delay.msDelay(genereerRandomDuur());
		}
	}

	public void kleurenWisselKort() {
		setDelay(50);
		kleurenWissel();
	}

	public void kleurenWisselMedium() {
		setDelay(200);
		kleurenWissel();
	}

	public void kleurenWisselLang() {
		setDelay(500);
		kleurenWissel();
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public void kleurenWissel() {
		for (int i = 0; i < 10; i++) {
			groenConstant();
			Delay.msDelay(this.delay);
			oranjeConstant();
			Delay.msDelay(this.delay);
			groenConstant();
			Delay.msDelay(this.delay);
			roodConstant();
			Delay.msDelay(this.delay);
		}
	}

	public void kleurenWisselKnipper() {
		for (int i = 0; i < 10; i++) {
			groenPulse();
			Delay.msDelay(this.delay);
			oranjePulse();
			Delay.msDelay(this.delay);
			groenPulse();
			Delay.msDelay(this.delay);
			roodPulse();
			Delay.msDelay(this.delay);
		}
	}

	public void kleurenWisselKortKnipper() {
		setDelay(50);
		kleurenWisselKnipper();
	}

	public void kleurenWisselMediumKnipper() {
		setDelay(200);
		kleurenWisselKnipper();
	}

	public void kleurenWisselLangKnipper() {
		setDelay(500);
		kleurenWisselKnipper();
	}

	public void steedsKorterGroen() {
		this.delay = 2000;
		for (int i = 0; i < 10; i++) {
			groenConstant();
			Delay.msDelay(delay);
			setDelay((int) (this.delay / 1.5));
			Button.LEDPattern(0);
			Delay.msDelay(150);
		}
	}

}
