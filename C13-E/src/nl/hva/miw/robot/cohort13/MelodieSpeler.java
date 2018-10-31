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
	// Lengte van een zestiende
	final static int ZESTIENDE = 63;
	// Lengte van een triplet
	final static int TRIPLET = 83;
	// Lengte van een achtste noot
	final static int ACHTSTE = 125;
	// lengte dotted eight
	final static int DOTTEIGHT = 187;
	// Lengte van een kwartnoot
	final static int KWART = 250;
	// Lengte van een derde noot
	final static int DERDE = 333;
	// lengte van een halve noot
	final static int HALF = 500;
	// lengte van een hele noot
	final static int HEEL = 1000;
	// Lengte van een break
	final static int BREAK = -1;
	// Default volume = maximaal volume
	final static int VOLUME = 3;
	// thread object dat wordt aangeroepen in de start() method
	private Thread thread;
	private KopLampen koplamp;

	// Melodie 1 voor kleurenspel
	final static int VADERJACOB[][] = { { C5, KWART }, { D5, KWART }, { E5, KWART }, { C5, KWART }, { C5, KWART },
			{ D5, KWART }, { E5, KWART }, { C5, KWART }, { E5, KWART }, { F5, KWART }, { G5, HALF }, { E5, KWART },
			{ F5, KWART }, { G5, HALF }, { G5, ACHTSTE }, { A5, ACHTSTE }, { G5, ACHTSTE }, { F5, ACHTSTE },
			{ E5, KWART }, { C5, KWART }, { G5, ACHTSTE }, { A5, ACHTSTE }, { G5, ACHTSTE }, { F5, ACHTSTE },
			{ E5, KWART }, { C5, KWART }, { C5, HALF }, { G4, HALF }, { C5, HEEL }, { C5, HALF }, { A4, HALF },
			{ C5, HEEL },

	};

	// Melodie 2 voor kleurenspel
	final static int TWINKLETWINKLE[][] = { { C5, KWART }, { C5, KWART }, { G5, KWART }, { G5, KWART }, { A5, KWART },
			{ A5, KWART }, { G5, HALF }, { F5, KWART }, { F5, KWART }, { E5, KWART }, { E5, KWART }, { D5, KWART },
			{ D5, KWART }, { C5, HALF }, { G5, KWART }, { G5, KWART }, { F5, KWART }, { F5, KWART }, { E5, KWART },
			{ E5, KWART }, { D5, HALF }, { G5, KWART }, { G5, KWART }, { F5, KWART }, { F5, KWART }, { E5, KWART },
			{ E5, KWART }, { D5, HALF }, { C5, KWART }, { C5, KWART }, { G5, KWART }, { G5, KWART }, { A5, KWART },
			{ A5, KWART }, { G5, HALF }, { F5, KWART }, { F5, KWART }, { E5, KWART }, { E5, KWART }, { D5, KWART },
			{ D5, KWART }, { C5, HALF }, };

	// startmelodie voor lijnvolger
	final static int STARTMELODIE[][] = { { C4, KWART }, { E5, KWART }, { G5, KWART }, { BREAK, BREAK }, { G5, KWART },
			{ A5, KWART }, { G5, KWART }, { E5, KWART }, { C5, BREAK }, { BREAK, BREAK }, { C5, HALF }, { C5, TRIPLET },
			{ E5, TRIPLET }, { G5, DOTTEIGHT }, { E5, ZESTIENDE }, { G5, HEEL }

	};

	// speel vader jacob
	public void speelVaderJacob() {
		for (int i = 0; i < VADERJACOB.length; i++) {
			Sound.playTone(VADERJACOB[i][0], VADERJACOB[i][1]);
		}
	}

	// speel twinkle twinkle
	public void speelTwinkleTwinkle() {

		koplamp = new KopLampen();

		for (int i = 0; i < TWINKLETWINKLE.length; i++) {
			koplamp.randomConstant();
			Sound.playTone(TWINKLETWINKLE[i][0], TWINKLETWINKLE[i][0]);

		}
	}

	// start method die wordt aangeroepen in main op het threadobject
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	// run method om in thread te gebruiken
	@Override
	public void run() {

		int count = 0;
		koplamp = new KopLampen();
		
		while (count < 2) {
			for (int i = 0; i < STARTMELODIE.length; i++) {
				koplamp.randomConstant();
				Sound.playTone(STARTMELODIE[i][0], STARTMELODIE[i][1]);
			}

			count++;
		}
	}
}
