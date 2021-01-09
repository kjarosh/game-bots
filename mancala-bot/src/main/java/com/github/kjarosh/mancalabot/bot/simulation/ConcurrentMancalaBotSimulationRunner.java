package com.github.kjarosh.mancalabot.bot.simulation;

import com.github.kjarosh.mancalabot.bot.MancalaBotConfig;
import com.github.kjarosh.mancalabot.mancala.MancalaBoard;
import com.github.kjarosh.mancalabot.mancala.Move;
import com.github.kjarosh.mancalabot.mcts.MonteCarloTreeSearch;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Kamil Jarosz
 */
@Slf4j
public class ConcurrentMancalaBotSimulationRunner implements MancalaBotSimulationRunner {
    private final MancalaBotConfig config;

    public ConcurrentMancalaBotSimulationRunner(MancalaBotConfig config) {
        this.config = config;
    }

    @Override
    public void run(MonteCarloTreeSearch<MancalaBoard, Move> mcts) {
        log.info("Starting worker threads (" + config.getThreads() + ")");
        SimulationThread[] threads = new SimulationThread[config.getThreads()];
        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new SimulationThread(i, mcts::nextRound);
        }

        for (SimulationThread thread : threads) {
            thread.start();
        }
        log.info("Worker threads started");

        Instant deadline = Instant.now().plus(config.getMaxMoveDuration());

        try {
            while (Instant.now().isBefore(deadline)) {
                Duration tillDeadline = Duration.between(Instant.now(), deadline);

                if (tillDeadline.toMillis() < 1000) {
                    Thread.sleep(tillDeadline.toMillis());
                } else {
                    Thread.sleep(1000);
                    log.info("Remaining seconds: " + tillDeadline.toSeconds());
                }
            }
        } catch (InterruptedException e) {
            log.info("Interrupted");
            Thread.currentThread().interrupt();
        }

        log.info("Finishing worker threads");
        for (SimulationThread thread : threads) {
            thread.interrupt();
        }

        try {
            for (SimulationThread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            log.info("Interrupted");
            Thread.currentThread().interrupt();
        }
    }
}
