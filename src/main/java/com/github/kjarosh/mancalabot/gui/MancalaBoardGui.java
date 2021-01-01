package com.github.kjarosh.mancalabot.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @author Kamil Jarosz
 */
public class MancalaBoardGui extends Stage {
    public MancalaBoardGui() {
        setTitle("Mancala");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(event -> System.out.println("Hello World!"));
        Pane root = new HBox();
        root.getChildren().add(btn);
        root.getChildren().add(new MancalaConfigPane());
        setScene(new Scene(root));
    }
}
