package com.github.kjarosh.mancalabot.bot;

import com.github.kjarosh.mancalabot.bot.simulation.ConcurrentMancalaBotSimulationRunner;
import com.github.kjarosh.mancalabot.bot.simulation.MancalaBotSimulationRunner;
import com.github.kjarosh.mancalabot.bot.simulation.SequentialMancalaBotSimulationRunner;
import com.github.kjarosh.mancalabot.mancala.MancalaBoard;
import com.github.kjarosh.mancalabot.mancala.Move;
import com.github.kjarosh.mancalabot.mancala.Player;
import com.github.kjarosh.mancalabot.mcts.MonteCarloTreeSearch;
import com.github.kjarosh.mancalabot.mcts.Party;
import com.github.kjarosh.mancalabot.mcts.strategies.UCTSelectionStrategy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Random;

/**
 * @author Kamil Jarosz
 */
@Slf4j
public class MancalaBot {
    private final Random random;
    private final MancalaBotConfig config;
    private final MancalaBotSimulationRunner simulationRunner;

    public MancalaBot(Random random, MancalaBotConfig config) {
        this.random = random;
        this.config = config;
        this.config.validate();
        if (config.isSequentialMode()) {
            this.simulationRunner = new SequentialMancalaBotSimulationRunner(config);
        } else {
            this.simulationRunner = new ConcurrentMancalaBotSimulationRunner(config);
        }
    }

    @SneakyThrows
    public MovePrediction nextMove(MancalaBoard board, Player player) {
        MonteCarloTreeSearch<MancalaBoard, Move> mcts = new MonteCarloTreeSearch<>(
                new UCTSelectionStrategy(random),
                getHandler(player),
                board);

        simulationRunner.run(mcts);

        double prob = -1d;
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

    private MancalaMonteCarloTreeSearchHandler getHandler(Player player) {
        return new MancalaMonteCarloTreeSearchHandler(random, player);
    }
}
