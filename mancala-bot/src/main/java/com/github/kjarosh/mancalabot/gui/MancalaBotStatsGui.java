package com.github.kjarosh.mancalabot.gui;

import com.github.kjarosh.mancalabot.bot.MovePrediction;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * @author Kamil Jarosz
 */
public class MancalaBotStatsGui extends GridPane {
    private static final String NO_VALUE = "N/A";

    private final Text winProbability = new Text(NO_VALUE);
    private final Text winProbabilityAfterMove = new Text(NO_VALUE);
    private final Text simulations = new Text(NO_VALUE);
    private final Text depth = new Text(NO_VALUE);

    private final SimpleObjectProperty<MovePrediction> lastPrediction = new SimpleObjectProperty<>(null);

    public MancalaBotStatsGui() {
        lastPrediction.addListener((observable, oldValue, newValue) -> render(newValue));
        render(lastPrediction.get());

        setVgap(10);
        setHgap(10);
        setPadding(new Insets(10));
        addRow(0, new Text("Win probability:"), winProbability);
        addRow(1, new Text("Win probability after move:"), winProbabilityAfterMove);
        addRow(2, new Text("Simulations:"), simulations);
        addRow(3, new Text("Depth:"), depth);
    }

    private void render(MovePrediction prediction) {
        if (prediction == null) {
            winProbability.setText(NO_VALUE);
            winProbabilityAfterMove.setText(NO_VALUE);
            simulations.setText(NO_VALUE);
            depth.setText(NO_VALUE);
        } else {
            winProbability.setText(formatDouble(prediction.getWinProbability()));
            winProbabilityAfterMove.setText(formatDouble(prediction.getWinProbabilityAfterMove()));
            simulations.setText(String.valueOf(prediction.getTotalSimulations()));
            depth.setText(String.valueOf(prediction.getTreeDepth()));
        }
    }

    private String formatDouble(double d) {
        return String.format("%.2f", d);
    }

    public SimpleObjectProperty<MovePrediction> lastPredictionProperty() {
        return lastPrediction;
    }
}
