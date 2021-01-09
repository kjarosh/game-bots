package com.github.kjarosh.mancalabot.gui;

import com.github.kjarosh.mancalabot.bot.MancalaBotConfig;
import com.github.kjarosh.mancalabot.mancala.MancalaConfig;
import com.github.kjarosh.mancalabot.gui.utils.NumberField;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.time.Duration;

/**
 * @author Kamil Jarosz
 */
public class MancalaConfigPane extends GridPane {
    private final NumberField numberOfPits = new NumberField(6);
    private final NumberField numberOfStones = new NumberField(4);
    private final CheckBox captureCapturingStone = new CheckBox();
    private final CheckBox playerStarts = new CheckBox();

    private final NumberField maxMoveDuration = new NumberField(10);
    private final NumberField threads = new NumberField(4);

    public MancalaConfigPane() {
        captureCapturingStone.setSelected(true);

        setVgap(10);
        setHgap(10);

        int i = 0;
        addRow(i++, makeHeader("Game config"));
        addRow(i++, new Text("Number of pits"), numberOfPits);
        addRow(i++, new Text("Number of stones"), numberOfStones);
        addRow(i++, new Text("Capture the capturing stone"), captureCapturingStone);
        addRow(i++, new Text("Player starts"), playerStarts);
        addRow(i++, makeHeader("Bot config"));
        addRow(i++, new Text("Maximum move duration (seconds)"), maxMoveDuration);
        addRow(i++, new Text("Threads"), threads);
    }

    private Text makeHeader(String text) {
        Text t = new Text(text);
        t.setStyle("-fx-font-weight: bold");
        return t;
    }

    public MancalaConfig toMancalaConfig() {
        return MancalaConfig.builder()
                .pits(numberOfPits.getNumber())
                .stones(numberOfStones.getNumber())
                .captureCapturingStone(captureCapturingStone.isSelected())
                .playerStarts(playerStarts.isSelected())
                .build();
    }

    public MancalaBotConfig toBotConfig() {
        return MancalaBotConfig.builder()
                .maxMoveDuration(Duration.ofSeconds(maxMoveDuration.getNumber()))
                .threads(threads.getNumber())
                .build();
    }
}
