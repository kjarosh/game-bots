package com.github.kjarosh.mancalabot.gui;

import com.github.kjarosh.mancalabot.mancala.MancalaBoard;
import com.github.kjarosh.mancalabot.mancala.Player;
import com.github.kjarosh.mancalabot.mancala.Result;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * @author Kamil Jarosz
 */
public class MancalaGameStatsGui extends GridPane {
    private final SimpleObjectProperty<MancalaBoard> board;
    private final SimpleObjectProperty<Player> turn;

    public MancalaGameStatsGui(SimpleObjectProperty<MancalaBoard> board, SimpleObjectProperty<Player> turn) {
        this.board = board;
        this.turn = turn;
        board.addListener((observable, oldValue, newValue) -> render());
        turn.addListener((observable, oldValue, newValue) -> render());
        render();
    }

    private void render() {
        MancalaBoard board = this.board.get();
        getChildren().clear();

        boolean ended = board.isEnded();
        Result result = board.resultFor(Player.PLAYER_A);
        addRow(0, new Text("Game result:"), new Text(!ended ? "in progress" : resultToString(result)));
        addRow(1, new Text("Turn:"), new Text(turn.get() == Player.PLAYER_A ? "You" : "Bot"));
    }

    private String resultToString(Result result) {
        return switch (result) {
            case WIN -> "Won";
            case LOSE -> "Lost";
            case TIE -> "Tie";
        };
    }
}
