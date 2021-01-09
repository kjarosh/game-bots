package com.github.kjarosh.mancalabot.mcts;

import java.util.Collection;

/**
 * @author Kamil Jarosz
 */
public interface MonteCarloTreeSearchHandler<S, M> {
    S applyMove(S state, M move);

    Collection<M> possibleMoves(S state, Party party);

    Party simulatePlayout(S state, Party party);
}
