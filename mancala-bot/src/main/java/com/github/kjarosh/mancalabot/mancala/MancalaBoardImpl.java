package com.github.kjarosh.mancalabot.mancala;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Kamil Jarosz
 */
class MancalaBoardImpl implements MancalaBoard {
    private final MancalaConfig config;

    private final int[] pitsA;
    private final int[] pitsB;

    @Getter
    @Setter
    private int mancalaA = 0;
    @Getter
    @Setter
    private int mancalaB = 0;

    MancalaBoardImpl(MancalaConfig config) {
        this.config = config;
        this.pitsA = new int[config.getPits()];
        this.pitsB = new int[config.getPits()];

        setAllPits(config.getStones());
    }

    MancalaBoardImpl(MancalaBoardImpl other) {
        this.config = other.config;
        this.mancalaA = other.mancalaA;
        this.mancalaB = other.mancalaB;
        this.pitsA = Arrays.copyOf(other.pitsA, other.pitsA.length);
        this.pitsB = Arrays.copyOf(other.pitsB, other.pitsB.length);
    }

    @Override
    public int getPitA(int pit) {
        return pitsA[pit];
    }

    @Override
    public int getPitB(int pit) {
        return pitsB[pit];
    }

    @Override
    public void setPitA(int pit, int value) {
        pitsA[pit] = value;
    }

    @Override
    public void setPitB(int pit, int value) {
        pitsB[pit] = value;
    }

    @Override
    public MancalaConfig getConfig() {
        return config;
    }

    @Override
    public void setAllPits(int value) {
        Arrays.fill(pitsA, value);
        Arrays.fill(pitsB, value);
    }

    @Override
    public int getMancala(Player player) {
        return player.getFor(
                this::getMancalaA,
                this::getMancalaB);
    }

    @Override
    public void setMancala(Player player, int value) {
        player.runFor(
                () -> setMancalaA(value),
                () -> setMancalaB(value));
    }

    @Override
    public void addMancala(Player player, int value) {
        setMancala(player, getMancala(player) + value);
    }

    @Override
    public int getPit(Player player, int pit) {
        return player.getFor(
                () -> getPitA(pit),
                () -> getPitB(pit));
    }

    @Override
    public void setPit(Player player, int pit, int value) {
        player.runFor(
                () -> setPitA(pit, value),
                () -> setPitB(pit, value));
    }

    @Override
    public void addPit(Player player, int pit, int value) {
        setPit(player, pit, getPit(player, pit) + value);
    }

    @Override
    public MancalaBoard copy() {
        return new MancalaBoardImpl(this);
    }

    @Override
    public MancalaBoard move(Player player, int pit) {
        return move(new Move(player, pit));
    }

    @Override
    public MancalaBoard move(Move move) {
        if (isEnded()) {
            throw new IllegalStateException("Ended game");
        }

        MancalaBoard copy = copy();
        copy.moveInPlace(move);
        return copy;
    }

    @Override
    public void moveInPlace(Move move) {
        new MovePerformer(this).moveInPlace(move);
    }

    @Override
    public boolean isEnded() {
        return Arrays.stream(pitsA).allMatch(v -> v == 0)
                || Arrays.stream(pitsB).allMatch(v -> v == 0);
    }

    @Override
    public Result resultFor(Player player) {
        if (player == Player.PLAYER_A) {
            return Result.ofPoints(getMancalaA() - getMancalaB());
        } else {
            return Result.ofPoints(getMancalaB() - getMancalaA());
        }
    }

    @Override
    public List<Move> getPossibleMoves(Player player) {
        if (isEnded()) {
            return Collections.emptyList();
        }

        List<Move> possibleMoves = new ArrayList<>();
        for (int i = 0; i < config.getPits(); ++i) {
            if (getPit(player, i) > 0) {
                possibleMoves.add(new Move(player, i));
            }
        }
        return possibleMoves;
    }

    @Override
    public Move randomMove(Player player, Random random) {
        List<Move> possibleMoves = getPossibleMoves(player);
        return possibleMoves.get(random.nextInt(possibleMoves.size()));
    }
}
