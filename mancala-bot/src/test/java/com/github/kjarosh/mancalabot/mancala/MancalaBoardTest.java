package com.github.kjarosh.mancalabot.mancala;

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
}
