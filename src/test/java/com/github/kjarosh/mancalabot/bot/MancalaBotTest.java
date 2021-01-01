package com.github.kjarosh.mancalabot.bot;

import com.github.kjarosh.mancalabot.mancala.MancalaBoard;
import com.github.kjarosh.mancalabot.mancala.MancalaConfig;
import com.github.kjarosh.mancalabot.mancala.Move;
import com.github.kjarosh.mancalabot.mancala.Player;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Random;

import static com.github.kjarosh.mancalabot.mancala.Player.PLAYER_A;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kamil Jarosz
 */
class MancalaBotTest {
    @Test
    void instantWin() {
        MancalaConfig config = MancalaConfig.builder()
                .pits(6)
                .stones(4)
                .captureCapturingStone(true)
                .build();
        MancalaBoard board = new MancalaBoard(config);

        board.setPit(PLAYER_A, 0, 1);
        board.setPit(PLAYER_A, 1, 1);
        board.setPit(PLAYER_A, 2, 0);
        board.setPit(PLAYER_A, 3, 1);
        board.setPit(PLAYER_A, 4, 1);
        board.setPit(PLAYER_A, 5, 0);

        board.setPit(Player.PLAYER_B, 0, 0);
        board.setPit(Player.PLAYER_B, 1, 0);
        board.setPit(Player.PLAYER_B, 2, 0);
        board.setPit(Player.PLAYER_B, 3, 20);
        board.setPit(Player.PLAYER_B, 4, 0);
        board.setPit(Player.PLAYER_B, 5, 0);

        MancalaBotConfig botConfig = MancalaBotConfig.builder()
                .maxMoveDuration(Duration.ofMillis(100))
                .build();
        MancalaBot bot = new MancalaBot(new Random(), botConfig);

        MovePrediction prediction = bot.nextMove(board, PLAYER_A);
        System.out.println(prediction);
        assertEquals(new Move(PLAYER_A, 1), prediction.getMove());
        assertEquals(1.0, prediction.getWinProbabilityAfterMove());
    }

    @Test
    void firstMove() {
        MancalaConfig config = MancalaConfig.builder()
                .pits(6)
                .stones(4)
                .captureCapturingStone(true)
                .build();
        MancalaBoard board = new MancalaBoard(config);

        board.setPit(PLAYER_A, 0, 4);
        board.setPit(PLAYER_A, 1, 4);
        board.setPit(PLAYER_A, 2, 4);
        board.setPit(PLAYER_A, 3, 4);
        board.setPit(PLAYER_A, 4, 4);
        board.setPit(PLAYER_A, 5, 4);

        board.setPit(Player.PLAYER_B, 0, 4);
        board.setPit(Player.PLAYER_B, 1, 4);
        board.setPit(Player.PLAYER_B, 2, 4);
        board.setPit(Player.PLAYER_B, 3, 4);
        board.setPit(Player.PLAYER_B, 4, 4);
        board.setPit(Player.PLAYER_B, 5, 4);

        MancalaBotConfig botConfig = MancalaBotConfig.builder()
                .maxMoveDuration(Duration.ofMillis(100))
                .build();
        MancalaBot bot = new MancalaBot(new Random(), botConfig);

        MovePrediction prediction = bot.nextMove(board, PLAYER_A);
        System.out.println(prediction);
        assertEquals(new Move(PLAYER_A, 5), prediction.getMove());
    }
}
