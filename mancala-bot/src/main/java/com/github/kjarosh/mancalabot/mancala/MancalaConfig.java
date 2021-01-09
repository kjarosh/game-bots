package com.github.kjarosh.mancalabot.mancala;

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
public class MancalaConfig {
    private final int stones;
    private final int pits;
    private final boolean captureCapturingStone;
    private final boolean playerStarts;
}
