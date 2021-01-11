package com.github.kjarosh.mancalabot.gui;

import com.github.kjarosh.mancalabot.bot.MancalaBot;
import com.github.kjarosh.mancalabot.bot.MancalaBotConfig;
import com.github.kjarosh.mancalabot.bot.MovePrediction;
import com.github.kjarosh.mancalabot.gui.history.HistoryEntry;
import com.github.kjarosh.mancalabot.mancala.MancalaBoard;
import com.github.kjarosh.mancalabot.mancala.MancalaConfig;
import com.github.kjarosh.mancalabot.mancala.Move;
import com.github.kjarosh.mancalabot.mancala.Player;
import com.github.kjarosh.mancalabot.mancala.Result;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Kamil Jarosz
 */
@Slf4j
public class MancalaGameGui extends GridPane {
    private final Executor executor = Executors.newFixedThreadPool(1, r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    private final ObservableList<HistoryEntry> history = FXCollections.observableArrayList();
    private final SimpleObjectProperty<MancalaBoard> board;
    private final SimpleObjectProperty<MovePrediction> lastPrediction;
    private final SimpleObjectProperty<Player> turn;
    private final MancalaBot bot;

    private final MancalaBoardGui mancalaBoardGui;
    private final Text headerText;

    public MancalaGameGui(MancalaConfig config, MancalaBotConfig botConfig) {
        this.board = new SimpleObjectProperty<>(MancalaBoard.create(config));
        this.lastPrediction = new SimpleObjectProperty<>(null);
        this.bot = new MancalaBot(new Random(), botConfig);

        if (config.isPlayerStarts()) {
            turn = new SimpleObjectProperty<>(Player.PLAYER_A);
        } else {
            turn = new SimpleObjectProperty<>(Player.PLAYER_B);
        }

        setHgap(20);
        setVgap(20);
        setPadding(new Insets(10));

        headerText = new Text();
        headerText.setStyle("-fx-font-size: 2em; -fx-font-weight: bold");
        turn.addListener((observable, oldValue, newValue) -> updateHeaderText());
        updateHeaderText();
        addRow(0, headerText);

        mancalaBoardGui = new MancalaBoardGui(board, this::onPlayerMove);
        addRow(1, mancalaBoardGui);

        MancalaBotStatsGui mancalaBotStatsGui = new MancalaBotStatsGui();
        mancalaBotStatsGui.lastPredictionProperty().bind(lastPrediction);
        TitledPane mancalaBotStatsGuiPane = new TitledPane();
        mancalaBotStatsGuiPane.setContent(mancalaBotStatsGui);
        mancalaBotStatsGuiPane.setText("Statistics");
        mancalaBotStatsGuiPane.setExpanded(false);
        addRow(2, mancalaBotStatsGuiPane);

        executor.execute(this::onBotMove);
    }

    private void updateHeaderText() {
        if (board.get().isFinished()) {
            Result result = board.get().resultFor(Player.PLAYER_A);
            headerText.setText(switch (result) {
                case WIN -> "You won!";
                case LOSE -> "You lost";
                case TIE -> "A tie";
            });
        } else if (turn.get() == Player.PLAYER_A) {
            headerText.setText("Your turn");
        } else {
            headerText.setText("Bot's turn");
        }
    }

    private void onPlayerMove(Move move) {
        MancalaBoard board = this.board.get();

        if (board.isFinished()) {
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
        if (turn.get() == Player.PLAYER_B && !board.get().isFinished()) {
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

        log.debug("Performing move {}", move);

        board.set(board.get().move(move));
        Player nextPlayer = turn.get().opponent();
        turn.set(nextPlayer);

        history.add(HistoryEntry.builder()
                .move(move)
                .boardAfterMove(board.get())
                .build());
        log.debug("Board after move {}", board.get());

        boolean ended = board.get().isFinished();
        if (ended) {
            new ResultPopup(board.get().resultFor(Player.PLAYER_A)).showAndWait();
        }
    }
}
