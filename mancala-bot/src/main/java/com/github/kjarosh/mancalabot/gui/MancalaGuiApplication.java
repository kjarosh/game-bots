package com.github.kjarosh.mancalabot.gui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Kamil Jarosz
 */
public class MancalaGuiApplication extends Application {
    public static void run() {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) {
        new MancalaStartWindow().showAndWait();
        primaryStage.hide();
    }
}
