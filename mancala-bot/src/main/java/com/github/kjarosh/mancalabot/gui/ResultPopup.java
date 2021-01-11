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
        switch (result) {
            case WIN:
                setHeaderText("You won! Congratulations");
                break;
            case LOSE:
                setHeaderText("You lost :(");
                break;
            case TIE:
                setHeaderText("There was a tie");
                break;
        }
    }
}
