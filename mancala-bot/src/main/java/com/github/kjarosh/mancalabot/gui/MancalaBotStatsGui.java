package com.github.kjarosh.mancalabot.gui;

import com.github.kjarosh.mancalabot.bot.MovePrediction;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * @author Kamil Jarosz
 */
public class MancalaBotStatsGui extends GridPane {
    public MancalaBotStatsGui(SimpleObjectProperty<MovePrediction> lastPrediction) {
        lastPrediction.addListener((observable, oldValue, newValue) -> render(newValue));
        render(lastPrediction.get());
    }

    private void render(MovePrediction prediction) {
        getChildren().clear();

        if (prediction == null) {
            addRow(0, new Text("No predictions"));
            return;
        }

        addRow(0, new Text("Win probability:"), new Text(formatDouble(prediction.getWinProbability())));
        addRow(1, new Text("Win probability after move:"), new Text(formatDouble(prediction.getWinProbabilityAfterMove())));
        addRow(2, new Text("Simulations:"), new Text("" + prediction.getTotalSimulations()));
        addRow(3, new Text("Depth:"), new Text("" + prediction.getTreeDepth()));
    }

    private String formatDouble(double d) {
        return String.format("%.2f", d);
    }
}
