package com.github.kjarosh.mancalabot.bot.simulation;

import com.github.kjarosh.mancalabot.bot.MancalaBotConfig;
import com.github.kjarosh.mancalabot.mancala.MancalaBoard;
import com.github.kjarosh.mancalabot.mancala.Move;
import com.github.kjarosh.mancalabot.mcts.MonteCarloTreeSearch;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Kamil Jarosz
 */
@Slf4j
public class ConcurrentMancalaBotSimulationRunner implements MancalaBotSimulationRunner {
    private final ExecutorService executor;
    private final MancalaBotConfig config;

    public ConcurrentMancalaBotSimulationRunner(MancalaBotConfig config) {
        this.config = config;
        this.executor = Executors.newFixedThreadPool(config.getThreads(), new SimulationThreadFactory());
        log.info("Created a pool of threads for concurrent simulation (" + config.getThreads() + ")");
    }

    @Override
    public void run(MonteCarloTreeSearch<MancalaBoard, Move> mcts) throws InterruptedException {
        Instant deadline = Instant.now().plus(config.getMaxMoveDuration());

        List<Future<?>> futures = new ArrayList<>(config.getThreads());
        for (int i = 0; i < config.getThreads(); ++i) {
            Future<?> future = executor.submit(() -> runInWorkerThread(mcts));
            futures.add(future);
        }

        try {
            while (Instant.now().isBefore(deadline)) {
                Duration tillDeadline = Duration.between(Instant.now(), deadline);

                if (tillDeadline.toMillis() < 1000) {
                    Thread.sleep(tillDeadline.toMillis());
                } else {
                    Thread.sleep(1000);
                    log.info("Remaining time: " +
                            tillDeadline.toSeconds() + "." +
                            tillDeadline.toMillis() % 1000);
                }
            }
        } catch (InterruptedException e) {
            log.warn("Interrupted while running simulation");
            throw e;
        }

        for (Future<?> future : futures) {
            future.cancel(true);
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                log.warn("Interrupted while waiting for worker threads");
                throw e;
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void runInWorkerThread(MonteCarloTreeSearch<MancalaBoard, Move> mcts) {
        while (!Thread.interrupted()) {
            mcts.nextRound();
        }
    }
}
