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
public class SequentialMancalaBotSimulationRunner implements MancalaBotSimulationRunner {
    private final MancalaBotConfig config;

    public SequentialMancalaBotSimulationRunner(MancalaBotConfig config) {
        this.config = config;
    }

    @Override
    public void run(MonteCarloTreeSearch<MancalaBoard, Move> mcts) {
        Duration duration = config.getMaxMoveDuration();
        Instant deadline = duration != null && !duration.isNegative() ?
                Instant.now().plus(duration) :
                Instant.MAX;
        for (int i = 0; i < config.getIterations(); ++i) {
            mcts.nextRound();

            if (i % 100 == 0 && Instant.now().isAfter(deadline)) {
                break;
            }
        }
    }
}
