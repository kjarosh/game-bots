package com.github.kjarosh.mancalabot.mancala;

import java.util.List;
import java.util.Random;

/**
 * @author Kamil Jarosz
 */
public interface MancalaBoard {
    MancalaConfig getConfig();

    void setAllPits(int value);

    int getMancala(Player player);

    void setMancala(Player player, int value);

    void addMancala(Player player, int value);

    int getPitA(int pit);

    int getPitB(int pit);

    void setPitA(int pit, int value);

    void setPitB(int pit, int value);

    int getPit(Player player, int pit);

    void setPit(Player player, int pit, int value);

    void addPit(Player player, int pit, int value);

    MancalaBoard copy();

    MancalaBoard move(Player player, int pit);

    MancalaBoard move(Move move);

    void moveInPlace(Move move);

    boolean isFinished();

    boolean hasMove(Player player);

    Result resultFor(Player player);

    List<Move> getPossibleMoves(Player player);

    Move randomMove(Player player, Random random);

    int getMancalaA();

    void setMancalaA(int mancalaA);

    int getMancalaB();

    void setMancalaB(int mancalaB);

    static MancalaBoard create(MancalaConfig config){
        return new MancalaBoardImpl(config);
    }
}
