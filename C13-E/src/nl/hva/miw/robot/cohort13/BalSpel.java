package nl.hva.miw.robot.cohort13;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class BalSpel {

    private UnregulatedMotor motorA;
    private UnregulatedMotor motorB;
    private EV3IRSensor infraroodSensor;
    private EV3TouchSensor touchSensor;
    private Hardware hardware;
    private Scherm scherm;
    private SensorMode seek;
    private float[] sample;
    private int direction;
    private int distance;

    public BalSpel(Hardware hardware) {
        super();
        this.hardware = hardware;
        this.motorA = hardware.maakMotorA();
        this.motorB = hardware.maakMotorB();
        this.infraroodSensor = hardware.maakInfraroodSensor();
        this.touchSensor = hardware.maakTouchSensor();
        this.scherm = hardware.maakScherm();
        this.seek = infraroodSensor.getSeekMode();
        this.sample = new float[seek.sampleSize()];
    }

    public void findBall() {
        initiateBalspel();
        drawLCD();
        while (Button.ESCAPE.isUp()) {
            seek.fetchSample(sample, 0);
            direction = (int) sample[0];
            distance = (int) sample[1];
            System.out.println(direction+"R");
            motorA.setPower(100);
            motorB.setPower(100);
            

            if (direction > 5) {
                motorA.forward();
                motorB.stop();
            } else if (direction < -5) {
                motorB.forward();
                motorA.stop();
            } else {
                if (distance < Integer.MAX_VALUE) {
                    motorA.forward();
                    motorB.forward();
                } else {
                    motorA.stop();
                    motorB.stop();
                }
            }
        }
    }

    private void initiateBalspel() {
        Sound.beepSequence(); // make sound when ready.
        scherm.printTekst("Druk op de knop!");
        Button.waitForAnyPress();
        if(Button.ESCAPE.isDown()) System.exit(0);
    }

    private void drawLCD() {
        // LCD.clear();
        Delay.msDelay(200);
        scherm.printOgen();
    }
}