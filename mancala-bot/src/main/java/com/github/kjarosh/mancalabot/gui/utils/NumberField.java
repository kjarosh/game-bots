package com.github.kjarosh.mancalabot.gui.utils;

import javafx.scene.control.TextField;

/**
 * @author Kamil Jarosz
 */
public class NumberField extends TextField {
    public NumberField() {
        textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public NumberField(int number) {
        this();
        setText(String.valueOf(number));
    }

    public int getNumber() {
        return Integer.parseInt(getText());
    }
}
