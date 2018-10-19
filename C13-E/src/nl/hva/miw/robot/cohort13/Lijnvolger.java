package nl.hva.miw.robot.cohort13;

import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.utility.Delay;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;

public class Lijnvolger {

	private int motorPowerA;
	private int motorPowerB;
	private int kleurXpassage = 0;
	private int oudeKleurMeting;
	private int nieuweKleurMeting = 2;
	private final double INTENSITEIT_LAAG = 0.2;
	private final double RICHT_INTENSITEIT = 0.5;
	private final double INTENSITEIT_HOOG = 0.8;
	private LichtsensorMeting meting = new LichtsensorMeting();
	private Tijdswaarneming tijdswaarneming = new Tijdswaarneming();

	GraphicsLCD LCD = BrickFinder.getDefault().getGraphicsLCD();
	static UnregulatedMotor MotorA = new UnregulatedMotor(MotorPort.A);
	static UnregulatedMotor MotorB = new UnregulatedMotor(MotorPort.B);

	void moveRobotFwd() {

		int startStopwatch = 0;
		while (kleurXpassage < 2) {
//			this.setKleurXpassage();
			meting.meetIntensiteit();
			bepaalTypeBocht();
			System.out.println(meting.getIntensiteit());
			rijden();
			if (kleurXpassage == 1 && startStopwatch == 0) {
				tijdswaarneming.startStopwatch();
				startStopwatch++;
			}
		}
		tijdswaarneming.stopStopwatch();
		MotorA.stop();
		MotorB.stop();
		meting.lichtSensor.close();
		LCD.clear();
		LCD.drawString(tijdswaarneming.toString(), 100, 20, 20);
		Delay.msDelay(10000);
		// toevoegen SFX
	}

	/**
	 * Deze methode hoogt de aantal passages op als een niet zwarte haakse lijn
	 * wordt gepasseerd. Methode finishkleur wordt aangeroepen om te bepalen of een
	 * gemeten kleur een meting is die niet zwart, wit of undefined is. van de float
	 * die terugkomt van de kleurmeting is een int gemaakt via casting.
	 */
	private void setKleurXpassage() {
		this.oudeKleurMeting = this.nieuweKleurMeting;
		meting.meetKleur();
		this.nieuweKleurMeting = (int) meting.getKleur();
		boolean oudeKleurMetingFinish = this.finishkleur(oudeKleurMeting);
		boolean nieuweKleurMetingFinish = this.finishkleur(nieuweKleurMeting);

		if (oudeKleurMetingFinish && !nieuweKleurMetingFinish) {
			kleurXpassage++;
			System.out.println("kleurpassage");
			Delay.msDelay(5000);
			LCD.clear();
		}
	}

	private boolean finishkleur(int kleur) {
		if (kleur == 2 || kleur == 6 || kleur == 7) {
			return false;
		} else
			return true;

		// Meetresultaten kleurcoderingen:
		// zwart 7
		// rood 0
		// wit 6
		// grens zwart/wit 2
	}

	private void bepaalTypeBocht() {
		
		// scherpe bocht naar links / rechts
		if (meting.getIntensiteit() > INTENSITEIT_HOOG) {
			draaiOpDePlek();
		} else if (meting.getIntensiteit() < INTENSITEIT_LAAG) {
			draaiOpDePlek();
			
			// flauwe bocht naar links / rechts (methode voor maken!)
		} else if (meting.getIntensiteit() > this.RICHT_INTENSITEIT) {
			flauweBocht();
		}
		else if (meting.getIntensiteit() < this.RICHT_INTENSITEIT) {
			flauweBocht();
		}
	}

	public void rijden() {
		MotorA.setPower(motorPowerA);
		MotorB.setPower(motorPowerB);
		MotorA.backward();
		MotorB.backward();
		Delay.msDelay(100);
	}

	public void draaiOpDePlek (){
		if (meting.getIntensiteit() > INTENSITEIT_HOOG) {
			MotorA.setPower(50);
			MotorB.setPower(50);
			MotorA.forward();
			MotorB.backward();
	//		moveRobotFwd();
		} else {
			MotorA.setPower(50);
			MotorB.setPower(50);
			MotorA.backward();
			MotorB.forward();
		//	moveRobotFwd();
		}
	}
	
	public void flauweBocht() {
		if (meting.getIntensiteit() > RICHT_INTENSITEIT) {
			this.motorPowerA = 30;
			this.motorPowerB = 60;
		}
		else {
			this.motorPowerA = 60;
			this.motorPowerB = 30;
		}
	}
}
