package com.github.kjarosh.mancalabot.mcts;

import java.util.Collection;

/**
 * Defines how to handle various operations required for MCTS to work.
 *
 * @param <S> game state
 * @param <M> game move
 * @author Kamil Jarosz
 */
public interface MonteCarloTreeSearchHandler<S, M> {
    /**
     * Apply the move to the game state. This operation should not modify
     * the given game state.
     *
     * @param state game state before the move
     * @param move  move to apply
     * @return game state after the move
     */
    S applyMove(S state, M move);

    /**
     * Return a collection of possible moves that the given party
     * may perform in the given game state.
     *
     * @param state game state which the move is performed in
     * @param party the party which performs a move
     * @return all possible moves that may be performed by the party
     */
    Collection<M> possibleMoves(S state, Party party);

    /**
     * Randomly simulate a playout, starting from the given state and party.
     *
     * @param state the game state to start from
     * @param party the party which should perform the first move
     * @return the party which won the playout
     */
    Party simulatePlayout(S state, Party party);
}
