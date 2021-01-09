package com.github.kjarosh.mancalabot.bot.simulation;

import com.github.kjarosh.mancalabot.mancala.MancalaBoard;
import com.github.kjarosh.mancalabot.mancala.Move;
import com.github.kjarosh.mancalabot.mcts.MonteCarloTreeSearch;

/**
 * @author Kamil Jarosz
 */
public interface MancalaBotSimulationRunner {
    void run(MonteCarloTreeSearch<MancalaBoard, Move> mcts) throws InterruptedException;
}
