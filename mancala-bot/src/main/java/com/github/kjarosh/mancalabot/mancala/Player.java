package com.github.kjarosh.mancalabot.mancala;

import java.util.function.Supplier;

/**
 * @author Kamil Jarosz
 */
public enum Player {
    PLAYER_A() {
        @Override
        public void runFor(Runnable playerA, Runnable playerB) {
            playerA.run();
        }

        @Override
        public <T> T getFor(Supplier<T> playerA, Supplier<T> playerB) {
            return playerA.get();
        }

        @Override
        public Player opponent() {
            return PLAYER_B;
        }
    },
    PLAYER_B() {
        @Override
        public void runFor(Runnable playerA, Runnable playerB) {
            playerB.run();
        }

        @Override
        public <T> T getFor(Supplier<T> playerA, Supplier<T> playerB) {
            return playerB.get();
        }

        @Override
        public Player opponent() {
            return PLAYER_A;
        }
    },
    ;

    public abstract void runFor(Runnable playerA, Runnable playerB);

    public abstract <T> T getFor(Supplier<T> playerA, Supplier<T> playerB);

    public abstract Player opponent();
}
