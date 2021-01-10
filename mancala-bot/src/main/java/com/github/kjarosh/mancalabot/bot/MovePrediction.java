package com.github.kjarosh.mancalabot.bot;

import com.github.kjarosh.mancalabot.mancala.Move;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Kamil Jarosz
 */
@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class MovePrediction {
    private final Move move;
    private final double winProbability;
    private final double winProbabilityAfterMove;
    private final long totalSimulations;
    private final int treeDepth;
}
