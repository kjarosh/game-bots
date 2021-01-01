package com.github.kjarosh.mancalabot.mcts;

import java.util.Collection;

/**
 * @author Kamil Jarosz
 */
public interface SelectionStrategy {
    <S, M> MonteCarloTreeSearch<S, M>.Node select(Collection<MonteCarloTreeSearch<S, M>.Node> nodes);
}
