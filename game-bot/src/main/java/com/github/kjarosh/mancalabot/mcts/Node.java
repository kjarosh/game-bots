package com.github.kjarosh.mancalabot.mcts;

import java.util.Optional;

/**
 * The MCTS node.
 *
 * @author Kamil Jarosz
 */
public interface Node {
    /**
     * @return number of playouts won by the party performing the move
     */
    double getWon();

    /**
     * @return number of total simulated playouts
     */
    long getTotal();

    /**
     * @return the party which is performing the move
     */
    Party getParty();

    /**
     * @return the parent node, or {@link Optional#empty()} if this is the root node
     */
    Optional<Node> getParent();
}
