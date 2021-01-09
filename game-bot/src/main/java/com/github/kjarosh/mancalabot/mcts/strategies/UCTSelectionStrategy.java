package com.github.kjarosh.mancalabot.mcts.strategies;

import com.github.kjarosh.mancalabot.mcts.Node;
import com.github.kjarosh.mancalabot.mcts.SelectionStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Selection strategy based on the UCT algorithm from
 * <em>Levente Kocsis, Csaba Szepesvári "Bandit based Monte-Carlo Planning" (2006)</em>.
 *
 * @author Kamil Jarosz
 */
public class UCTSelectionStrategy implements SelectionStrategy {
    private final Random random;
    private final double explorationFactor;

    public UCTSelectionStrategy(Random random) {
        this(random, Math.sqrt(2));
    }

    public UCTSelectionStrategy(Random random, double explorationFactor) {
        this.random = random;
        this.explorationFactor = explorationFactor;

        if (explorationFactor < 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public <N extends Node> N select(Collection<N> nodes) {
        List<N> results = new ArrayList<>();
        double max = -1d;
        for (N node : nodes) {
            double value = evaluate(node);

            if (!(value >= 0)) {
                throw new AssertionError();
            }

            if (value == max) {
                results.add(node);
            } else if (value > max) {
                max = value;
                results.clear();
                results.add(node);
            }
        }
        return results.get(random.nextInt(results.size()));
    }

    private <N extends Node> double evaluate(N node) {
        double w = node.getWon();
        double n = node.getTotal();
        double N = node.getParent()
                .map(Node::getTotal)
                .map(i -> Math.max(0, i))
                .orElse(1L);

        if (n == 0) {
            return 1;
        }

        double exploitation = w / n;
        double exploration = explorationFactor * Math.sqrt(Math.log(N) / n);

        return exploitation + exploration;
    }
}
