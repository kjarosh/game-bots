package com.github.kjarosh.mancalabot.mcts;

/**
 * @author Kamil Jarosz
 */
public enum Party {
    MAIN,
    OPPONENT,
    ;

    public Party opponent() {
        if (this == MAIN) {
            return OPPONENT;
        } else {
            return MAIN;
        }
    }
}
