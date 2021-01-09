package com.github.kjarosh.mancalabot.bot.simulation;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kamil Jarosz
 */
class SimulationThreadFactory implements ThreadFactory {
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName("simulation-" + counter.incrementAndGet());
        thread.setDaemon(true);
        return thread;
    }
}
