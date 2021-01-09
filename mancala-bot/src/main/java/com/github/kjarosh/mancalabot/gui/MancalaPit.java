package com.github.kjarosh.mancalabot.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * @author Kamil Jarosz
 */
public class MancalaPit extends StackPane {
    private final String style = "" +
            "-fx-border-radius: 1em;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 1px";

    private final String textStyle = "" +
            "-fx-font-size: 2em;" +
            "-fx-border-width: 0;";

    public MancalaPit() {
        setStyle(style);
        setPadding(new Insets(10));
    }

    public MancalaPit(int value) {
        this();
        Text text = new Text("" + value);
        text.setStyle(textStyle);
        getChildren().add(text);
    }

    public MancalaPit(int value, Runnable onClick) {
        this();
        Hyperlink text = new Hyperlink("" + value);
        text.setStyle(textStyle);
        text.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> onClick.run());
        getChildren().add(text);
    }
}
