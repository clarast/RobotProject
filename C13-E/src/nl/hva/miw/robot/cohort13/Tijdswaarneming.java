package nl.hva.miw.robot.cohort13;

import java.util.concurrent.TimeUnit;

import lejos.utility.Stopwatch;

public class Tijdswaarneming {
	private Stopwatch stopwatch;

	public Tijdswaarneming() {
		super();
		this.stopwatch = new Stopwatch();
	}


	public Stopwatch getStopwatch() {
		return stopwatch;
	}


	@Override
	public String toString() {
		int milliseconds = (int) (stopwatch.elapsed() % 1000) % 60000 / 10;
		int seconds = (int) (stopwatch.elapsed() / 1000) % 60;
		int minutes = (int) ((stopwatch.elapsed() / (1000 * 60)) % 60);

		return String.format("%d: %d: %d", minutes, seconds, milliseconds);
	}

}
