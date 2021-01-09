package com.github.kjarosh.mancalabot.gui;

import com.github.kjarosh.mancalabot.mancala.MancalaBoard;
import com.github.kjarosh.mancalabot.mancala.MancalaConfig;
import com.github.kjarosh.mancalabot.mancala.Move;
import com.github.kjarosh.mancalabot.mancala.Player;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.function.Consumer;

/**
 * @author Kamil Jarosz
 */
public class MancalaBoardGui extends GridPane {
    private final Consumer<Move> onMove;
    private Node[] row1;
    private Node[] row2;

    public MancalaBoardGui(
            ObjectProperty<MancalaBoard> board,
            Consumer<Move> onMove) {
        this.onMove = onMove;
        board.addListener((observable, oldValue, newValue) -> renderBoard(newValue));
        renderBoard(board.get());

        double gap = 25;
        setVgap(gap);
        setHgap(gap);
    }

    private void renderBoard(MancalaBoard board) {
        MancalaConfig config = board.getConfig();
        getChildren().clear();

        row1 = new Node[config.getPits() + 2];
        row1[0] = new MancalaPit(board.getMancala(Player.PLAYER_B));
        for (int i = 1; i < row1.length - 1; ++i) {
            final int pit = row1.length - i - 2;
            int value = board.getPit(Player.PLAYER_B, pit);
            row1[i] = new MancalaPit(value);
        }
        row1[row1.length - 1] = new MancalaPit(board.getMancala(Player.PLAYER_A));

        row2 = new Node[config.getPits() + 2];
        row2[0] = new Text("");
        for (int i = 1; i < row2.length - 1; ++i) {
            final int pit = i - 1;
            int value = board.getPit(Player.PLAYER_A, pit);
            row2[i] = new MancalaPit(value, () -> {
                onMove.accept(new Move(Player.PLAYER_A, pit));
            });
        }
        row2[row2.length - 1] = new Text("");
        addRow(0, row1);
        addRow(1, row2);

        setRowSpan(row1[0], 2);
        setRowSpan(row1[row1.length - 1], 2);
    }

    public void visualizeMove(Move move) {
        MancalaPit pit;
        if (move.getPlayer() == Player.PLAYER_A) {
            pit = (MancalaPit) row2[row2.length - move.getPit() - 2];
        } else {
            pit = (MancalaPit) row1[move.getPit() + 1];
        }

//        pit.focusMode();
    }
}
