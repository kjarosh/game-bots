package com.github.kjarosh.mancalabot.bot.simulation;

/**
 * @author Kamil Jarosz
 */
public class SimulationThread extends Thread {
    private final Runnable step;

    public SimulationThread(int number, Runnable step) {
        this.step = step;
        setName("simulation-" + number);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            step.run();
        }
    }
}
