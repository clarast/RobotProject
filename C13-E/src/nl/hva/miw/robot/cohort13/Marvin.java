package nl.hva.miw.robot.cohort13;


import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Marvin {

	Brick brick;

	public Marvin() {
		super();
		brick = LocalEV3.get();
	}

	public static void main(String[] args) {
		Marvin marvin = new Marvin();
		marvin.run();

	}

	private void run() {
		TextLCD display = brick.getTextLCD();
		display.drawString("Push the button!", 0, 4);
		// Wav file afspelen + LCD kleurtjes
		waitForKey(Button.ENTER);
		Drive drive = new Drive();
		drive.moveRobotFwd();
	}

	public void waitForKey(Key key) {
		while (key.isUp()) {
			Delay.msDelay(100);
		}
		while (key.isDown()) {
			Delay.msDelay(100);
		}
	}

	public float[] meetIntensiteit() {
		//initialiseer sensor
		Brick brick = BrickFinder.getDefault();
	    Port s1 = brick.getPort("S1");
		EV3ColorSensor lichtSensor = new EV3ColorSensor(s1);
		//neem meting in RedMode
		SampleProvider redMode = lichtSensor.getRedMode();
		float[] sample = new float[lichtSensor.sampleSize()];
		redMode.fetchSample(sample, 0);
		//sluit sensor af
		lichtSensor.close();
		return sample[0];
	}
}
