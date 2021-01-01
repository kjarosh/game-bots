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
//        primaryStage.setTitle("Mancala");
//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(event -> System.out.println("Hello World!"));
//        StackPane root = new StackPane();
//        root.getChildren().add(btn);
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();
        new MancalaBoardGui().show();
    }
}
