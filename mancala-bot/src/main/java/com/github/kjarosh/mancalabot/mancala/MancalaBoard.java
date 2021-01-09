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
public class MancalaBoard {
    private final MancalaConfig config;

    private final int[] pitsA;
    private final int[] pitsB;

    @Getter
    @Setter
    private int mancalaA = 0;
    @Getter
    @Setter
    private int mancalaB = 0;

    public MancalaBoard(MancalaConfig config) {
        this.config = config;
        this.pitsA = new int[config.getPits()];
        this.pitsB = new int[config.getPits()];

        setAllPits(config.getStones());
    }

    private MancalaBoard(MancalaBoard other) {
        this.config = other.config;
        this.mancalaA = other.mancalaA;
        this.mancalaB = other.mancalaB;
        this.pitsA = Arrays.copyOf(other.pitsA, other.pitsA.length);
        this.pitsB = Arrays.copyOf(other.pitsB, other.pitsB.length);
    }

    public void setAllPits(int value) {
        Arrays.fill(pitsA, value);
        Arrays.fill(pitsB, value);
    }

    public int getMancala(Player player) {
        return player.getFor(
                this::getMancalaA,
                this::getMancalaB);
    }

    public void setMancala(Player player, int value) {
        player.runFor(
                () -> setMancalaA(value),
                () -> setMancalaB(value));
    }

    public void addMancala(Player player, int value) {
        setMancala(player, getMancala(player) + value);
    }

    private int getPitA(int pit) {
        return pitsA[pit];
    }

    private int getPitB(int pit) {
        return pitsB[pit];
    }

    private void setPitA(int pit, int value) {
        pitsA[pit] = value;
    }

    private void setPitB(int pit, int value) {
        pitsB[pit] = value;
    }

    public int getPit(Player player, int pit) {
        return player.getFor(
                () -> getPitA(pit),
                () -> getPitB(pit));
    }

    public void setPit(Player player, int pit, int value) {
        player.runFor(
                () -> setPitA(pit, value),
                () -> setPitB(pit, value));
    }

    public void addPit(Player player, int pit, int value) {
        setPit(player, pit, getPit(player, pit) + value);
    }

    public MancalaBoard copy() {
        return new MancalaBoard(this);
    }

    public MancalaBoard move(Player player, int pit) {
        return move(new Move(player, pit));
    }

    public MancalaBoard move(Move move) {
        if (isEnded()) {
            throw new IllegalStateException("Ended game");
        }

        MancalaBoard copy = copy();
        copy.moveInPlace(move);
        return copy;
    }

    public void moveInPlace(Move move) {
        Player player = move.getPlayer();
        int pit = move.getPit();
        if (pit >= config.getPits()) {
            throw new IllegalArgumentException();
        }

        int stones = getPit(player, pit);
        setPit(player, pit, 0);
        placeStones(player, player, pit + 1, stones);
    }

    private void placeStones(Player player, Player pitsOwner, int pit, int stones) {
        if (stones <= 0) return;

        while (stones > 0 && pit < config.getPits()) {
            --stones;

            addPit(pitsOwner, pit, 1);
            ++pit;
        }

        if (stones == 0 &&
                player == pitsOwner &&
                getPit(player, pit - 1) == 1) {
            capture0(player, pit - 1);
        }

        if (stones > 0 && player == pitsOwner) {
            --stones;
            addMancala(pitsOwner, 1);
        }

        placeStones(player, pitsOwner.opponent(), 0, stones);
    }

    private void capture0(Player player, int pit) {
        int otherPit = config.getPits() - pit - 1;
        int captured = getPit(player.opponent(), otherPit);
        setPit(player.opponent(), otherPit, 0);

        if (config.isCaptureCapturingStone()) {
            captured += getPit(player, pit);
            setPit(player, pit, 0);
        }

        addMancala(player, captured);
    }

    public boolean isEnded() {
        return Arrays.stream(pitsA).allMatch(v -> v == 0)
                || Arrays.stream(pitsB).allMatch(v -> v == 0);
    }

    public Result resultFor(Player player) {
        if (player == Player.PLAYER_A) {
            return Result.ofPoints(getMancalaA() - getMancalaB());
        } else {
            return Result.ofPoints(getMancalaB() - getMancalaA());
        }
    }

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

    public Move randomMove(Player player, Random random) {
        List<Move> possibleMoves = getPossibleMoves(player);
        return possibleMoves.get(random.nextInt(possibleMoves.size()));
    }

    public MancalaConfig getConfig() {
        return config;
    }
}
