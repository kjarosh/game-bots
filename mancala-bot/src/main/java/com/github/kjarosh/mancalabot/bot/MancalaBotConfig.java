package com.github.kjarosh.mancalabot.bot;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;

/**
 * @author Kamil Jarosz
 */
@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class MancalaBotConfig {
    @Builder.Default
    private final int maxDepth = -1;

    @Builder.Default
    private final boolean sequentialMode = false;
    private final long iterations;

    private final Duration maxMoveDuration;
    @Builder.Default
    private final int threads = 1;

    public void validate() {
        if (sequentialMode) {
            Preconditions.checkState(iterations > 0,
                    "iterations <= 0: " + iterations);
        } else {
            Preconditions.checkState(!maxMoveDuration.isNegative(),
                    "maxMoveDuration negative: " + maxMoveDuration);
            Preconditions.checkState(threads > 0,
                    "threads <= 0: " + threads);
        }
    }
}
