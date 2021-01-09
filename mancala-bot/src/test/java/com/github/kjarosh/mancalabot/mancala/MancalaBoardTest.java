package com.github.kjarosh.mancalabot.mancala;

import com.github.kjarosh.mancalabot.mancala.MancalaBoard;
import com.github.kjarosh.mancalabot.mancala.MancalaConfig;
import com.github.kjarosh.mancalabot.mancala.Player;
import com.github.kjarosh.mancalabot.mancala.Result;
import org.junit.jupiter.api.Test;

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
        MancalaBoard board = new MancalaBoard(config);

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
    void simpleMove() {
        MancalaConfig config = MancalaConfig.builder()
                .pits(5)
                .stones(2)
                .build();
        MancalaBoard board = new MancalaBoard(config);

        MancalaBoard afterMove = board.move(Player.PLAYER_A, 2);

        // B 0 2 2 2 2 2
        // A   2 2 0 3 3 0

        assertEquals(2, afterMove.getPit(Player.PLAYER_A, 0));
        assertEquals(2, afterMove.getPit(Player.PLAYER_A, 1));
        assertEquals(0, afterMove.getPit(Player.PLAYER_A, 2));
        assertEquals(3, afterMove.getPit(Player.PLAYER_A, 3));
        assertEquals(3, afterMove.getPit(Player.PLAYER_A, 4));
        assertEquals(0, afterMove.getMancalaA());
        assertEquals(2, afterMove.getPit(Player.PLAYER_B, 0));
        assertEquals(2, afterMove.getPit(Player.PLAYER_B, 1));
        assertEquals(2, afterMove.getPit(Player.PLAYER_B, 2));
        assertEquals(2, afterMove.getPit(Player.PLAYER_B, 3));
        assertEquals(2, afterMove.getPit(Player.PLAYER_B, 4));
        assertEquals(0, afterMove.getMancalaB());
    }

    @Test
    void moveWithLap() {
        MancalaConfig config = MancalaConfig.builder()
                .pits(5)
                .stones(7)
                .build();
        MancalaBoard board = new MancalaBoard(config);

        MancalaBoard afterMove = board.move(Player.PLAYER_A, 1);

        // B 0 7 7 8 8 8
        // A   7 0 8 8 8 1

        assertEquals(7, afterMove.getPit(Player.PLAYER_A, 0));
        assertEquals(0, afterMove.getPit(Player.PLAYER_A, 1));
        assertEquals(8, afterMove.getPit(Player.PLAYER_A, 2));
        assertEquals(8, afterMove.getPit(Player.PLAYER_A, 3));
        assertEquals(8, afterMove.getPit(Player.PLAYER_A, 4));
        assertEquals(1, afterMove.getMancalaA());
        assertEquals(8, afterMove.getPit(Player.PLAYER_B, 0));
        assertEquals(8, afterMove.getPit(Player.PLAYER_B, 1));
        assertEquals(8, afterMove.getPit(Player.PLAYER_B, 2));
        assertEquals(7, afterMove.getPit(Player.PLAYER_B, 3));
        assertEquals(7, afterMove.getPit(Player.PLAYER_B, 4));
        assertEquals(0, afterMove.getMancalaB());
    }

    @Test
    void moveWith2Lap() {
        MancalaConfig config = MancalaConfig.builder()
                .pits(5)
                .stones(14)
                .build();
        MancalaBoard board = new MancalaBoard(config);

        MancalaBoard afterMove = board.move(Player.PLAYER_A, 1);

        // B 0 15 15 15 15 15
        // A   15  1 16 16 16 1

        assertEquals(15, afterMove.getPit(Player.PLAYER_A, 0));
        assertEquals(1, afterMove.getPit(Player.PLAYER_A, 1));
        assertEquals(16, afterMove.getPit(Player.PLAYER_A, 2));
        assertEquals(16, afterMove.getPit(Player.PLAYER_A, 3));
        assertEquals(16, afterMove.getPit(Player.PLAYER_A, 4));
        assertEquals(1, afterMove.getMancalaA());
        assertEquals(15, afterMove.getPit(Player.PLAYER_B, 0));
        assertEquals(15, afterMove.getPit(Player.PLAYER_B, 1));
        assertEquals(15, afterMove.getPit(Player.PLAYER_B, 2));
        assertEquals(15, afterMove.getPit(Player.PLAYER_B, 3));
        assertEquals(15, afterMove.getPit(Player.PLAYER_B, 4));
        assertEquals(0, afterMove.getMancalaB());
    }

    @Test
    void moveWithMultipleLaps() {
        MancalaConfig config = MancalaConfig.builder()
                .pits(5)
                .stones(23)
                .build();
        MancalaBoard board = new MancalaBoard(config);

        MancalaBoard afterMove = board.move(Player.PLAYER_A, 3);

        // B 0 25 25 25 25 25
        // A   25 25 25  2 26 2

        assertEquals(25, afterMove.getPit(Player.PLAYER_A, 0));
        assertEquals(25, afterMove.getPit(Player.PLAYER_A, 1));
        assertEquals(25, afterMove.getPit(Player.PLAYER_A, 2));
        assertEquals(2, afterMove.getPit(Player.PLAYER_A, 3));
        assertEquals(26, afterMove.getPit(Player.PLAYER_A, 4));
        assertEquals(2, afterMove.getMancalaA());
        assertEquals(25, afterMove.getPit(Player.PLAYER_B, 0));
        assertEquals(25, afterMove.getPit(Player.PLAYER_B, 1));
        assertEquals(25, afterMove.getPit(Player.PLAYER_B, 2));
        assertEquals(25, afterMove.getPit(Player.PLAYER_B, 3));
        assertEquals(25, afterMove.getPit(Player.PLAYER_B, 4));
        assertEquals(0, afterMove.getMancalaB());
    }

    @Test
    void capture() {
        MancalaConfig config = MancalaConfig.builder()
                .pits(4)
                .stones(2)
                .captureCapturingStone(false)
                .build();
        MancalaBoard board = new MancalaBoard(config);
        board.setPit(Player.PLAYER_A, 2, 0);

        // B 0 2 2 2 2
        // A   2 2 0 2 0

        MancalaBoard afterMove = board.move(Player.PLAYER_A, 0);

        // B 0 2 2 0 2
        // A   0 3 1 2 2

        assertEquals(0, afterMove.getPit(Player.PLAYER_A, 0));
        assertEquals(3, afterMove.getPit(Player.PLAYER_A, 1));
        assertEquals(1, afterMove.getPit(Player.PLAYER_A, 2));
        assertEquals(2, afterMove.getPit(Player.PLAYER_A, 3));
        assertEquals(2, afterMove.getMancalaA());
        assertEquals(2, afterMove.getPit(Player.PLAYER_B, 0));
        assertEquals(0, afterMove.getPit(Player.PLAYER_B, 1));
        assertEquals(2, afterMove.getPit(Player.PLAYER_B, 2));
        assertEquals(2, afterMove.getPit(Player.PLAYER_B, 3));
        assertEquals(0, afterMove.getMancalaB());
    }

    @Test
    void capture2() {
        MancalaConfig config = MancalaConfig.builder()
                .pits(4)
                .stones(2)
                .captureCapturingStone(true)
                .build();
        MancalaBoard board = new MancalaBoard(config);
        board.setPit(Player.PLAYER_A, 2, 0);
        board.setPit(Player.PLAYER_B, 1, 7);

        // B 0 2 2 7 2
        // A   2 2 0 2 0

        MancalaBoard afterMove = board.move(Player.PLAYER_A, 0);

        // B 0 2 2 0 2
        // A   0 3 0 2 8

        assertEquals(0, afterMove.getPit(Player.PLAYER_A, 0));
        assertEquals(3, afterMove.getPit(Player.PLAYER_A, 1));
        assertEquals(0, afterMove.getPit(Player.PLAYER_A, 2));
        assertEquals(2, afterMove.getPit(Player.PLAYER_A, 3));
        assertEquals(8, afterMove.getMancalaA());
        assertEquals(2, afterMove.getPit(Player.PLAYER_B, 0));
        assertEquals(0, afterMove.getPit(Player.PLAYER_B, 1));
        assertEquals(2, afterMove.getPit(Player.PLAYER_B, 2));
        assertEquals(2, afterMove.getPit(Player.PLAYER_B, 3));
        assertEquals(0, afterMove.getMancalaB());
    }

    @Test
    void captureOwn() {
        MancalaConfig config = MancalaConfig.builder()
                .pits(4)
                .stones(7)
                .captureCapturingStone(false)
                .build();
        MancalaBoard board = new MancalaBoard(config);
        board.setPit(Player.PLAYER_B, 2, 0);

        // B 0 7 0 7 7
        // A   7 7 7 7 0

        MancalaBoard afterMove = board.move(Player.PLAYER_A, 0);

        // B 0 7 1 8 8
        // A   0 8 8 8 1

        assertEquals(0, afterMove.getPit(Player.PLAYER_A, 0));
        assertEquals(8, afterMove.getPit(Player.PLAYER_A, 1));
        assertEquals(8, afterMove.getPit(Player.PLAYER_A, 2));
        assertEquals(8, afterMove.getPit(Player.PLAYER_A, 3));
        assertEquals(1, afterMove.getMancalaA());
        assertEquals(8, afterMove.getPit(Player.PLAYER_B, 0));
        assertEquals(8, afterMove.getPit(Player.PLAYER_B, 1));
        assertEquals(1, afterMove.getPit(Player.PLAYER_B, 2));
        assertEquals(7, afterMove.getPit(Player.PLAYER_B, 3));
        assertEquals(0, afterMove.getMancalaB());
    }

    @Test
    void captureWithLap() {
        MancalaConfig config = MancalaConfig.builder()
                .pits(4)
                .stones(9)
                .captureCapturingStone(true)
                .build();
        MancalaBoard board = new MancalaBoard(config);

        // B 0 9 9 9 9
        // A   9 9 9 9 0

        MancalaBoard afterMove = board.move(Player.PLAYER_A, 0);

        // B 0 0 10 10 10
        // A   0 10 10 10 12

        assertEquals(0, afterMove.getPit(Player.PLAYER_A, 0));
        assertEquals(10, afterMove.getPit(Player.PLAYER_A, 1));
        assertEquals(10, afterMove.getPit(Player.PLAYER_A, 2));
        assertEquals(10, afterMove.getPit(Player.PLAYER_A, 3));
        assertEquals(12, afterMove.getMancalaA());
        assertEquals(10, afterMove.getPit(Player.PLAYER_B, 0));
        assertEquals(10, afterMove.getPit(Player.PLAYER_B, 1));
        assertEquals(10, afterMove.getPit(Player.PLAYER_B, 2));
        assertEquals(0, afterMove.getPit(Player.PLAYER_B, 3));
        assertEquals(0, afterMove.getMancalaB());
    }

    @Test
    void result() {
        MancalaConfig config = MancalaConfig.builder()
                .pits(4)
                .stones(7)
                .build();
        MancalaBoard board = new MancalaBoard(config);
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
        MancalaBoard board = new MancalaBoard(config);
        board.setMancalaA(10);
        board.setMancalaB(10);

        assertEquals(Result.TIE, board.resultFor(Player.PLAYER_A));
        assertEquals(Result.TIE, board.resultFor(Player.PLAYER_B));
    }
}
