/**
 * @author Kamil Jarosz
 */
module com.github.kjarosh.mancalabot {
    requires static lombok;

    requires com.github.kjarosh.gamebot;
    requires slf4j.api;

    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires com.google.common;
    requires commons.cli;

    opens com.github.kjarosh.mancalabot.mancala;
    opens com.github.kjarosh.mancalabot.bot;
    opens com.github.kjarosh.mancalabot.gui to javafx.graphics;
}
