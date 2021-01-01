package com.github.kjarosh.mancalabot.bot;

import com.github.kjarosh.mancalabot.mancala.MancalaBoard;
import com.github.kjarosh.mancalabot.mancala.Move;
import com.github.kjarosh.mancalabot.mancala.Player;
import com.github.kjarosh.mancalabot.mcts.MonteCarloTreeSearch;
import com.github.kjarosh.mancalabot.mcts.Party;
import com.github.kjarosh.mancalabot.mcts.strategies.ConstantBiasedSelectionStrategy;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Random;

/**
 * @author Kamil Jarosz
 */
@Slf4j
public class MancalaBot {
    private final Random random;
    private final MancalaBotConfig config;

    public MancalaBot(Random random, MancalaBotConfig config) {
        this.random = random;
        this.config = config;
        this.config.validate();
    }

    public MovePrediction nextMove(MancalaBoard board, Player player) {
        MonteCarloTreeSearch<MancalaBoard, Move> mcts = new MonteCarloTreeSearch<>(
                new ConstantBiasedSelectionStrategy(random, 0.5),
                getHandler(player),
                board);

        runSimulation(mcts);

        double prob = 0;
        Move move = null;
        Map<Move, Double> winProbabilities = mcts.getWinProbabilities(Party.MAIN);
        for (Map.Entry<Move, Double> entry : winProbabilities.entrySet()) {
            double p = entry.getValue();
            if (p > prob) {
                prob = p;
                move = entry.getKey();
            }
        }

        return MovePrediction.builder()
                .move(move)
                .winProbability(mcts.getWinProbability(Party.MAIN))
                .winProbabilityAfterMove(prob)
                .totalSimulations(mcts.getTotalSimulations())
                .treeDepthMax(mcts.getMaxDepth())
                .treeDepthAvg(mcts.getAverageDepth())
                .build();
    }

    private void runSimulation(MonteCarloTreeSearch<MancalaBoard, Move> mcts) {
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

    private MancalaMonteCarloTreeSearchHandler getHandler(Player player) {
        return new MancalaMonteCarloTreeSearchHandler(random, player);
    }
}
