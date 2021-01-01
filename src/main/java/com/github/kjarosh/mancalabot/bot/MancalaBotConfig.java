package com.github.kjarosh.mancalabot.bot;

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
    private final Duration maxMoveDuration;

    @Builder.Default
    private final int threads = 1;

    public void validate() {
        if (threads < 1) {
            throw new IllegalArgumentException("threads < 1: " + threads);
        }

        if (maxMoveDuration.isNegative()) {
            throw new IllegalArgumentException("maxMoveDuration negative: " + maxMoveDuration);
        }
    }
}
