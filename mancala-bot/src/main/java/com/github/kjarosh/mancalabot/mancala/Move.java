package com.github.kjarosh.mancalabot.mancala;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * @author Kamil Jarosz
 */
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class Move {
    private final Player player;
    private final int pit;

    public Move(Player player, int pit) {
        this.player = Objects.requireNonNull(player);
        this.pit = pit;

        if (pit < 0) {
            throw new IllegalArgumentException("pit < 0: " + pit);
        }
    }
}
