package com.github.kjarosh.mancalabot.mcts;

/**
 * Party of the game. Either {@link #MAIN} or {@link #OPPONENT}.
 *
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
