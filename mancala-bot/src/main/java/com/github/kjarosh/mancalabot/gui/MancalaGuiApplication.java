package com.github.kjarosh.mancalabot.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Kamil Jarosz
 */
public class MancalaGuiApplication extends Application {
    private final MancalaConfigPane mancalaConfigPane = new MancalaConfigPane();

    public static void run(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mancala — start game");
        Button startButton = new Button();
        startButton.setText("Start game");
        startButton.setOnAction(event -> {
            MancalaGameGui gui = new MancalaGameGui(
                    mancalaConfigPane.toMancalaConfig(),
                    mancalaConfigPane.toBotConfig());
            primaryStage.setScene(new Scene(gui));
        });
        Pane root = new VBox();
        root.setPadding(new Insets(10));
        root.getChildren().add(mancalaConfigPane);
        root.getChildren().add(startButton);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
