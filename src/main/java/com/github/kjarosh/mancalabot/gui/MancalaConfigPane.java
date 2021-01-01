package com.github.kjarosh.mancalabot.gui;

import com.github.kjarosh.mancalabot.mancala.MancalaConfig;
import com.github.kjarosh.mancalabot.gui.utils.NumberField;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * @author Kamil Jarosz
 */
public class MancalaConfigPane extends GridPane {
    private final NumberField numberOfPits = new NumberField(6);
    private final NumberField numberOfStones = new NumberField(4);
    private final CheckBox captureCapturingStone = new CheckBox();

    public MancalaConfigPane() {
        captureCapturingStone.setSelected(true);

        setPadding(new Insets(10));
        setVgap(10);
        setHgap(10);
        addRow(0, new Text("Number of pits"), numberOfPits);
        addRow(1, new Text("Number of stones"), numberOfStones);
        addRow(2, new Text("Capture the capturing stone"), captureCapturingStone);
    }

    public MancalaConfig toConfig() {
        return MancalaConfig.builder()
                .pits(numberOfPits.getNumber())
                .stones(numberOfStones.getNumber())
                .captureCapturingStone(captureCapturingStone.isSelected())
                .build();
    }
}
