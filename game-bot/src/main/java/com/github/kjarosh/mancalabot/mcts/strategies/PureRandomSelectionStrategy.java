package com.github.kjarosh.mancalabot.mcts.strategies;

import com.github.kjarosh.mancalabot.mcts.Node;
import com.github.kjarosh.mancalabot.mcts.SelectionStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Chooses nodes at random, not taking into account number of won playouts.
 *
 * @author Kamil Jarosz
 */
public class PureRandomSelectionStrategy implements SelectionStrategy {
    private final Random random;

    public PureRandomSelectionStrategy(Random random) {
        this.random = random;
    }

    @Override
    public <N extends Node> N select(Collection<N> nodes) {
        List<N> nodeList = new ArrayList<>(nodes);
        return nodeList.get(random.nextInt(nodeList.size()));
    }
}
