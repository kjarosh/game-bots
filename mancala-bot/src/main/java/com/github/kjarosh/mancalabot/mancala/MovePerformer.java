package com.github.kjarosh.mancalabot.mancala;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Kamil Jarosz
 */
@Slf4j
class MovePerformer {
    private final MancalaBoard board;
    private final MancalaConfig config;

    MovePerformer(MancalaBoard board) {
        this.board = board;
        this.config = board.getConfig();
    }

    void moveInPlace(Move move) {
        log.debug("Performing move {}", move);

        Player player = move.getPlayer();
        int pit = move.getPit();
        if (pit >= config.getPits()) {
            throw new IllegalArgumentException("Wrong move: invalid pit number");
        }

        int stones = board.getPit(player, pit);
        if (stones <= 0) {
            throw new IllegalArgumentException("Illegal move: no stones at pit " + pit);
        }
        board.setPit(player, pit, 0);
        placeStones(player, player, pit + 1, stones);
    }

    private void placeStones(Player player, Player pitsOwner, int pit, int stones) {
        if (stones <= 0) return;

        while (stones > 0 && pit < config.getPits()) {
            --stones;

            board.addPit(pitsOwner, pit, 1);
            ++pit;
        }

        if (stones == 0 &&
                player == pitsOwner &&
                board.getPit(player, pit - 1) == 1) {
            capture(player, pit - 1);
        }

        if (stones > 0 && player == pitsOwner) {
            --stones;
            board.addMancala(pitsOwner, 1);
        }

        placeStones(player, pitsOwner.opponent(), 0, stones);
    }

    private void capture(Player player, int pit) {
        int otherPit = config.getPits() - pit - 1;
        int captured = board.getPit(player.opponent(), otherPit);
        board.setPit(player.opponent(), otherPit, 0);

        if (config.isCaptureCapturingStone()) {
            captured += board.getPit(player, pit);
            board.setPit(player, pit, 0);
        }

        board.addMancala(player, captured);
        log.debug("Captured {} stones from player {} at pit {}",
                captured, player.opponent(), otherPit);
    }
}
