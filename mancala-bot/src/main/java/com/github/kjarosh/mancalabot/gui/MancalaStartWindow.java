package com.github.kjarosh.mancalabot.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Kamil Jarosz
 */
public class MancalaStartWindow extends Stage {
    private final MancalaConfigPane mancalaConfigPane = new MancalaConfigPane();

    public MancalaStartWindow() {
        setTitle("Mancala — start game");
        Button startButton = new Button();
        startButton.setText("Start game");
        startButton.setOnAction(event -> {
            MancalaStartWindow.this.hide();
            Stage gui = new MancalaGameWindow(
                    mancalaConfigPane.toMancalaConfig(),
                    mancalaConfigPane.toBotConfig());
            gui.show();
            gui.requestFocus();
        });
        Pane root = new VBox();
        root.setPadding(new Insets(10));
        root.getChildren().add(mancalaConfigPane);
        root.getChildren().add(startButton);
        setScene(new Scene(root));
    }
}
