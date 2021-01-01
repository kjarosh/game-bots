package com.github.kjarosh.mancalabot.mcts.strategies;

import com.github.kjarosh.mancalabot.mcts.MonteCarloTreeSearch;
import com.github.kjarosh.mancalabot.mcts.Party;
import com.github.kjarosh.mancalabot.mcts.SelectionStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Kamil Jarosz
 */
public class ConstantBiasedSelectionStrategy implements SelectionStrategy {
    private final Random random;
    private final double bias;

    public ConstantBiasedSelectionStrategy(Random random, double bias) {
        this.random = random;
        this.bias = bias;
    }

    @Override
    public <S, M> MonteCarloTreeSearch<S, M>.Node select(Collection<MonteCarloTreeSearch<S, M>.Node> nodes) {
        Map<MonteCarloTreeSearch<S, M>.Node, Double> shares = new HashMap<>();
        for (MonteCarloTreeSearch<S, M>.Node node : nodes) {
            double winProb = node.getWinProbability(Party.MAIN);
            double share = (Double.isNaN(winProb) ? 0.5 : winProb) + bias;
            shares.put(node, share);
        }

        return chooseByShares(shares);
    }

    private <T> T chooseByShares(Map<T, Double> shares) {
        List<Map.Entry<T, Double>> entries = new ArrayList<>(shares.entrySet());
        double sharesSum = entries.stream()
                .mapToDouble(Map.Entry::getValue)
                .sum();
        List<Double> probabilities = entries.stream()
                .map(e -> e.getValue() / sharesSum)
                .collect(Collectors.toList());
        double r = random.nextDouble();
        for (int i = 0; i < entries.size(); ++i) {
            double prob = probabilities.get(i);
            if (r < prob) {
                return entries.get(i).getKey();
            } else {
                r -= prob;
            }
        }

        throw new AssertionError("" + shares);
    }
}
