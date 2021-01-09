package com.github.kjarosh.mancalabot.mancala;

/**
 * @author Kamil Jarosz
 */
public enum Result {
    WIN,
    LOSE,
    TIE,
    ;

    public static Result ofPoints(int points) {
        if (points > 0) {
            return WIN;
        } else if (points == 0) {
            return TIE;
        } else {
            return LOSE;
        }
    }
}
