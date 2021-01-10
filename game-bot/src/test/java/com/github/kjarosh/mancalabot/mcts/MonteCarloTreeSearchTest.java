package com.github.kjarosh.mancalabot.mcts;

import com.github.kjarosh.mancalabot.mcts.strategies.PureRandomSelectionStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kamil Jarosz
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class MonteCarloTreeSearchTest {
    @Mock
    private SelectionStrategy selectionStrategy;

    @Mock
    private MonteCarloTreeSearchHandler<Object, Object> handler;

    @Mock
    private Object state;

    @Test
    void initialNode() {
        MonteCarloTreeSearch<Object, Object> mcts = new MonteCarloTreeSearch<>(selectionStrategy, handler, state);

        assertThat(mcts.root.getParty())
                .isEqualTo(Party.OPPONENT);
        assertThat(mcts.root.getSimulationDepth())
                .isEqualTo(1);
        assertThat(mcts.root.getParent())
                .isEmpty();
        assertThat(mcts.root.getTotal())
                .isEqualTo(0);
    }

    @Test
    void maxDepth() {
        Mockito.when(handler.possibleMoves(Mockito.any(), Mockito.any()))
                .thenReturn(Arrays.asList("a", "b"));
        Mockito.when(handler.simulatePlayout(Mockito.any(), Mockito.any()))
                .thenReturn(Outcome.TIE);

        MonteCarloTreeSearch<Object, Object> mcts = new MonteCarloTreeSearch<>(
                new PureRandomSelectionStrategy(new Random(1)), handler, state);
        mcts.setMaxDepth(3);
        for (int i = 0; i < 100; ++i) {
            mcts.nextRound();
        }
        assertThat(mcts.getSimulationDepth()).isEqualTo(3);
    }
}
