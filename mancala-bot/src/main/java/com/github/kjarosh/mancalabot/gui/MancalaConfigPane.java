package com.github.kjarosh.mancalabot.gui;

import com.github.kjarosh.mancalabot.bot.MancalaBotConfig;
import com.github.kjarosh.mancalabot.mancala.MancalaConfig;
import com.github.kjarosh.mancalabot.gui.utils.NumberField;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import lombok.SneakyThrows;

import java.net.URL;
import java.time.Duration;

/**
 * @author Kamil Jarosz
 */
public class MancalaConfigPane extends GridPane {
    private final NumberField numberOfPits = new NumberField(6);
    private final NumberField numberOfStones = new NumberField(4);
    private final CheckBox captureCapturingStone = new CheckBox();
    private final CheckBox playerStarts = new CheckBox();

    private final NumberField maxMoveDuration = new NumberField(3);
    private final NumberField threads = new NumberField(4);
    private final ComboBox<Difficulty> difficulty = new ComboBox<>(FXCollections.observableArrayList(
            Difficulty.HARMLESS,
            Difficulty.EASY,
            Difficulty.MEDIUM,
            Difficulty.HARD,
            Difficulty.EXTREME
    ));

    public MancalaConfigPane() {
        captureCapturingStone.setSelected(true);
        difficulty.setValue(Difficulty.EXTREME);

        setVgap(10);
        setHgap(10);

        int i = 0;
        addRow(i++, makeHeader("Game config"));
        addRow(i++, new Text("Number of pits"), numberOfPits);
        addRow(i++, new Text("Number of stones"), numberOfStones);
        addRow(i++, new Text("Capture the capturing stone"), captureCapturingStone);
        addRow(i++, new Text("Player starts"), playerStarts);
        addRow(i++, makeHeader("Bot config"));
        addRow(i++, new Text("Difficulty"), difficulty);
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
                .maxDepth(difficulty.getValue().getMaxDepth())
                .maxMoveDuration(Duration.ofSeconds(maxMoveDuration.getNumber()))
                .threads(threads.getNumber())
                .build();
    }
}
