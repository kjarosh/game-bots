package com.github.kjarosh.mancalabot.gui;

import com.github.kjarosh.mancalabot.bot.MancalaBot;
import com.github.kjarosh.mancalabot.bot.MancalaBotConfig;
import com.github.kjarosh.mancalabot.bot.MovePrediction;
import com.github.kjarosh.mancalabot.mancala.MancalaBoard;
import com.github.kjarosh.mancalabot.mancala.MancalaConfig;
import com.github.kjarosh.mancalabot.mancala.Move;
import com.github.kjarosh.mancalabot.mancala.Player;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Kamil Jarosz
 */
@Slf4j
public class MancalaGameWindow extends Stage {
    private static final Executor executor = Executors.newFixedThreadPool(1);

    private final SimpleObjectProperty<MancalaBoard> board;
    private final SimpleObjectProperty<MovePrediction> lastPrediction;
    private final SimpleObjectProperty<Player> turn;
    private final MancalaBot bot;

    private final MancalaBoardGui mancalaBoardGui;

    public MancalaGameWindow(MancalaConfig config, MancalaBotConfig botConfig) {
        this.board = new SimpleObjectProperty<>(MancalaBoard.create(config));
        this.lastPrediction = new SimpleObjectProperty<>(null);
        this.bot = new MancalaBot(new Random(), botConfig);

        if (config.isPlayerStarts()) {
            turn = new SimpleObjectProperty<>(Player.PLAYER_A);
        } else {
            turn = new SimpleObjectProperty<>(Player.PLAYER_B);
        }

        GridPane layout = new GridPane();
        layout.setHgap(20);
        layout.setVgap(20);
        layout.setPadding(new Insets(10));
        mancalaBoardGui = new MancalaBoardGui(board, this::onPlayerMove);
        layout.addRow(0, mancalaBoardGui);
        layout.addRow(1, new MancalaGameStatsGui(board, turn));
        layout.addRow(2, new MancalaBotStatsGui(lastPrediction));
        setScene(new Scene(layout));

        executor.execute(this::onBotMove);
    }

    private void onPlayerMove(Move move) {
        MancalaBoard board = this.board.get();

        if (!board.hasMove(move.getPlayer())) {
            log.info("Cannot perform move: game ended");
            return;
        }

        if (turn.get() != Player.PLAYER_A) {
            log.info("Cannot perform move: wrong turn");
            return;
        }

        if (!board.getPossibleMoves(Player.PLAYER_A).contains(move)) {
            log.info("Cannot perform move: illegal move");
            return;
        }

        performMove(move);

        executor.execute(this::onBotMove);
    }

    private void onBotMove() {
        if (turn.get() == Player.PLAYER_B && board.get().hasMove(Player.PLAYER_B)) {
            MovePrediction movePrediction = bot.nextMove(board.get(), Player.PLAYER_B);

            Platform.runLater(() -> {
                lastPrediction.set(movePrediction);
                performMove(movePrediction.getMove());
            });
        }
    }

    private void performMove(Move move) {
        if (move.getPlayer() != turn.get()) {
            throw new IllegalStateException("Wrong player");
        }

        board.set(board.get().move(move));
        Player nextPlayer = turn.get().opponent();
        turn.set(nextPlayer);

        boolean ended = !board.get().hasMove(nextPlayer);
        if (ended) {
            new ResultPopup(board.get().resultFor(Player.PLAYER_A)).showAndWait();
        }
    }
}
