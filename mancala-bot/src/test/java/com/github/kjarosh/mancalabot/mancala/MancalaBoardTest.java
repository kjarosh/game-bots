package com.github.kjarosh.mancalabot.mancala;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Kamil Jarosz
 */
class MancalaBoardTest {
    @Test
    void initialBoard() {
        MancalaConfig config = MancalaConfig.builder()
                .pits(5)
                .stones(7)
                .build();
        MancalaBoard board = new MancalaBoardImpl(config);

        assertEquals(0, board.getMancalaA());
        assertEquals(0, board.getMancalaB());

        for (int i = 0; i < 5; ++i) {
            assertEquals(7, board.getPit(Player.PLAYER_A, i));
            assertEquals(7, board.getPit(Player.PLAYER_B, i));
        }

        assertThatThrownBy(() -> board.getPit(Player.PLAYER_A, 5))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
        assertThatThrownBy(() -> board.getPit(Player.PLAYER_B, 5))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    void result() {
        MancalaConfig config = MancalaConfig.builder()
                .pits(4)
                .stones(7)
                .build();
        MancalaBoard board = new MancalaBoardImpl(config);
        board.setMancalaA(10);
        board.setMancalaB(5);

        assertEquals(Result.WIN, board.resultFor(Player.PLAYER_A));
        assertEquals(Result.LOSE, board.resultFor(Player.PLAYER_B));
    }

    @Test
    void resultTie() {
        MancalaConfig config = MancalaConfig.builder()
                .pits(4)
                .stones(7)
                .build();
        MancalaBoard board = new MancalaBoardImpl(config);
        board.setMancalaA(10);
        board.setMancalaB(10);

        assertEquals(Result.TIE, board.resultFor(Player.PLAYER_A));
        assertEquals(Result.TIE, board.resultFor(Player.PLAYER_B));
    }

    @Test
    void possibleMoves() {
        MancalaConfig config = MancalaConfig.builder()
                .pits(4)
                .stones(1)
                .build();
        MancalaBoard board = new MancalaBoardImpl(config);
        board.setPit(Player.PLAYER_A, 0, 2);
        board.setPit(Player.PLAYER_A, 1, 0);
        board.setPit(Player.PLAYER_A, 2, 4);
        board.setPit(Player.PLAYER_A, 3, 2);
        board.setPit(Player.PLAYER_B, 0, 1);
        board.setPit(Player.PLAYER_B, 1, 0);
        board.setPit(Player.PLAYER_B, 2, 1);
        board.setPit(Player.PLAYER_B, 3, 0);

        List<Move> possibleMovesA = board.getPossibleMoves(Player.PLAYER_A);
        assertThat(possibleMovesA).containsExactlyInAnyOrder(
                new Move(Player.PLAYER_A, 0),
                new Move(Player.PLAYER_A, 2),
                new Move(Player.PLAYER_A, 3));

        List<Move> possibleMovesB = board.getPossibleMoves(Player.PLAYER_B);
        assertThat(possibleMovesB).containsExactlyInAnyOrder(
                new Move(Player.PLAYER_B, 0),
                new Move(Player.PLAYER_B, 2));
    }

    @Test
    void noPossibleMoves() {
        MancalaConfig config = MancalaConfig.builder()
                .pits(4)
                .stones(1)
                .build();
        MancalaBoard board = new MancalaBoardImpl(config);
        board.setPit(Player.PLAYER_A, 0, 0);
        board.setPit(Player.PLAYER_A, 1, 0);
        board.setPit(Player.PLAYER_A, 2, 7);
        board.setPit(Player.PLAYER_A, 3, 0);
        board.setPit(Player.PLAYER_B, 0, 0);
        board.setPit(Player.PLAYER_B, 1, 0);
        board.setPit(Player.PLAYER_B, 2, 0);
        board.setPit(Player.PLAYER_B, 3, 0);

        List<Move> possibleMovesA = board.getPossibleMoves(Player.PLAYER_A);
        assertThat(possibleMovesA).containsExactlyInAnyOrder(
                new Move(Player.PLAYER_A, 2));

        List<Move> possibleMovesB = board.getPossibleMoves(Player.PLAYER_B);
        assertThat(possibleMovesB).isEmpty();
    }
}
