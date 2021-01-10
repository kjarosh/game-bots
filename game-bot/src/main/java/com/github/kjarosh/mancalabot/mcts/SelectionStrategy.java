package com.github.kjarosh.mancalabot.mcts;

import java.util.Collection;

/**
 * The selection strategy to apply when choosing MCTS nodes to simulate playouts.
 *
 * @author Kamil Jarosz
 */
public interface SelectionStrategy {
    /**
     * Choose one of the given nodes by some criteria.
     *
     * @param nodes nodes to choose from
     * @param <N>   concrete node type
     * @return the chosen node
     */
    <N extends Node> N select(Collection<N> nodes);

    /**
     * Evaluate top-level nodes to select the answer.
     *
     * @param node node to evaluate
     * @return evaluated number, grater is better
     */
    default double evaluateFinal(Node node) {
        return node.getTotal();
    }
}
