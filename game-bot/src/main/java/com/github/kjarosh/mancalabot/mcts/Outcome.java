package com.github.kjarosh.mancalabot.mcts;

/**
 * @author Kamil Jarosz
 */
public enum Outcome {
    MAIN_WON(Party.MAIN),
    OPPONENT_WON(Party.OPPONENT),
    TIE(null),
    ;

    private final Party winner;

    Outcome(Party winner) {
        this.winner = winner;
    }

    public Party getWinner() {
        return winner;
    }
}
