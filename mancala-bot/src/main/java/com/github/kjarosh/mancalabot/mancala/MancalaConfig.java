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
    @Builder.Default
    private final int stones = 4;
    @Builder.Default
    private final int pits = 6;
    @Builder.Default
    private final boolean captureCapturingStone = true;
    @Builder.Default
    private final boolean playerStarts = false;
}
