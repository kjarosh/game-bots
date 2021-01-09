package com.github.kjarosh.mancalabot.mcts;

/**
 * The MCTS node.
 *
 * @author Kamil Jarosz
 */
public interface Node {
    /**
     * @return number of playouts won by the party performing the move
     */
    long getWon();

    /**
     * @return number of total simulated playouts
     */
    long getTotal();

    /**
     * @return the party which is performing the move
     */
    Party getParty();
}
