package nl.hva.miw.robot.cohort13;

import lejos.hardware.Sound;

/**
 * In deze class worden noten gedefinieerd in frequenties en wordt een melodie
 * gecomponeerd. Er wordt een method aangemaakt die de melodie als thread kan
 * laten spelen (in een loop maar ook zonder loop)
 */

public class MelodieSpeler extends Thread {

	/**
	 * Declaratie finals en array
	 */
	final static int C4 = 262;
	final static int C5 = 523;
	final static int C6 = 1047;
	final static int C7 = 2093;
	final static int D5 = 587;
	final static int D6 = 1175;
	final static int E5 = 659;
	final static int E6 = 1319;
	final static int F4 = 370;
	final static int FIS4 = 349;
	final static int F5 = 698;
	final static int FIS5 = 740;
	final static int F6 = 1397;
	final static int FIS6 = 1497;
	final static int A4 = 440;
	final static int A5 = 880;
	final static int A6 = 1865;
	final static int AIS6 = 1865;
	final static int AIS5 = 932;
	final static int AIS4 = 466;
	final static int G4 = 392;
	final static int GIS4 = 415;
	final static int G5 = 784;
	final static int GIS5 = 830;
	final static int G6 = 1568;
	final static int GIS6 = 1661;
	final static int B4 = 494;
	final static int B5 = 988;
	final static int B6 = 1975;
	// Lengte van een kwartnoot
	final static int KWART = 250;
	// lengte van een derde noot
	final static int DERDE = 333;
	// Lengte van een break
	final static int BREAK = -1;
	// Default volume = maximaal volume
	final static int VOLUME = 10;
	// tweedimensionale array die melodie opslaat
	private int[][] melodie = null;
	// thread object dat wordt aangeroepen in de start() method
	private Thread thread;
	private KopLampen koplamp;

	public void setLiedNummer(int liednummer) {
		this.liednummer = liednummer;
	}

	private int liednummer;
	// Melodie 1 voor kleurenspel
	final static int VADERJACOB[][] = { { C5, KWART }, { D5, KWART }, { E5, KWART }, { C5, KWART }, { BREAK, BREAK },
			{ C5, KWART }, { D5, KWART }, { E5, KWART }, { C5, KWART }, { BREAK, BREAK }, { E5, KWART }, { F5, KWART },
			{ G5, 2 * KWART }, { E5, KWART }, { F5, KWART }, { G5, 2 * KWART }, { BREAK, BREAK }, { G5, 1 / 2 * KWART },
			{ A5, 1 / 2 * KWART }, { G5, 1 / 2 * KWART }, { F5, 1 / 2 * KWART }, { E5, KWART }, { C5, KWART },
			{ G5, 1 / 2 * KWART }, { A5, 1 / 2 * KWART }, { G5, 1 / 2 * KWART }, { F5, 1 / 2 * KWART }, { E5, KWART },
			{ C5, KWART }, { BREAK, BREAK }, { C5, 2 * KWART }, { A4, 2 * KWART }, { C5, 2 * KWART }, { BREAK, BREAK },
			{ C5, 2 * KWART }, { A4, 2 * KWART }, { C5, 2 * KWART },

	};

	// Melodie 2

	// Melodie 2 tijdens lijnvolger

	final static int STADIUMTHEME[][] = { { AIS4, 2 * KWART }, { F4, 2 * KWART }, { G4, 2 * KWART }, { A4, 2 * KWART },
			{ BREAK, BREAK }, { AIS4, 2 * KWART }, { F4, 2 * KWART }, { G4, 2 * KWART }, { A4, 2 * KWART },
			{ BREAK, BREAK }, { B4, 2 * KWART }, { FIS4, 2 * KWART }, { GIS4, 2 * KWART }, { AIS4, 2 * KWART },
			{ BREAK, BREAK }, { B4, 2 * KWART }, { FIS4, 2 * KWART }, { GIS4, 2 * KWART }, { AIS4, 2 * KWART },
			{ C5, 2 * KWART }, { BREAK, BREAK }, { G4, 2 * KWART }, { A4, 2 * KWART }, { BREAK, BREAK },
			{ C5, 2 * KWART }, { G4, 2 * KWART }, { A4, 2 * KWART }, { B4, 2 * KWART }, { BREAK, BREAK },
			{ C4, 2 * KWART }, { G4, 2 * KWART }, { A4, 2 * KWART }, { B4, 2 * KWART }, { BREAK, BREAK },
			{ G4, 1 / 2 * KWART }, { C5, 1 / 4 * KWART }, { E5, 1 / 4 * KWART }, { G5, DERDE }, { E5, 1 / 2 * KWART },
			{ G5, 2 * KWART }, };

	// startmelodie voor lijnvolger
	final static int STARTMELODIE[][] = { { C4, KWART }, { E5, KWART }, { G5, KWART }, { BREAK, BREAK }, { G5, KWART },
			{ A5, KWART }, { G5, KWART }, { E5, BREAK }, { C5, BREAK }

	};
	
	//speel vader jacob
	public void speelVaderJacob() {
		for (int i = 0; i < VADERJACOB.length; i++) {
			Sound.playTone(VADERJACOB[i][0], VADERJACOB[i][1]);
		}

	}

	// start method die wordt aangeroepen in main op het threadobject

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}

	}

	// run method
	@Override
	public void run() {

		int noot = A5;
		int nootlengte = KWART;
		int count = 0;
		koplamp = new KopLampen();

		if (liednummer == 1) {
			for (int i = 0; i < VADERJACOB.length; i++) {
				Sound.playTone(VADERJACOB[i][0], VADERJACOB[i][1]);
			}
		} else if (liednummer == 2) {

			while (count < 2) {
				for (int i = 0; i < STARTMELODIE.length; i++) {
					koplamp.kleurenWisselKnipper();
					Sound.playTone(STARTMELODIE[i][0], STARTMELODIE[i][1]);
				}
				count++;
			}
		}
	}

}
