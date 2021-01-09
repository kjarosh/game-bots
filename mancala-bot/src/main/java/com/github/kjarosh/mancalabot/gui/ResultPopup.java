package com.github.kjarosh.mancalabot.gui;

import com.github.kjarosh.mancalabot.mancala.Result;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * @author Kamil Jarosz
 */
public class ResultPopup extends Alert {
    public ResultPopup(Result result) {
        super(AlertType.INFORMATION, null, ButtonType.OK);
        setHeaderText(switch (result) {
            case WIN -> "You won! Congratulations";
            case LOSE -> "You lost :(";
            case TIE -> "There was a tie";
        });
    }
}
