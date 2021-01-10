package com.github.kjarosh.mancalabot.gui;

/**
 * @author Kamil Jarosz
 */
public enum Difficulty {
    HARMLESS(0, "Harmless"),
    EASY(1, "Easy"),
    MEDIUM(2, "Medium"),
    HARD(3, "Hard"),
    EXTREME(-1, "Extreme"),
    ;

    private final int maxDepth;
    private final String name;

    Difficulty(int maxDepth, String name) {
        this.maxDepth = maxDepth;
        this.name = name;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    @Override
    public String toString() {
        return name;
    }
}
