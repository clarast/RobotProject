package nl.hva.miw.robot.cohort13;

import java.util.concurrent.TimeUnit;

import lejos.utility.Stopwatch;

public class Tijdswaarneming {
	private Stopwatch stopwatch;
	private int eindtijd;

	public Tijdswaarneming() {
		super();
		this.stopwatch = new Stopwatch();
	}

	public void startStopwatch() {
		stopwatch.reset();
	}

	public void stopStopwatch() {
		this.eindtijd = stopwatch.elapsed();
	}

	@Override
	public String toString() {
		return String.format("%02d min, %02d sec", TimeUnit.MILLISECONDS.toMinutes(this.eindtijd),
				TimeUnit.MILLISECONDS.toSeconds(this.eindtijd)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this.eindtijd)));
	}

}
