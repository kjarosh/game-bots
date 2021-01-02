package com.github.kjarosh.mancalabot.cli;

import com.github.kjarosh.mancalabot.gui.MancalaGuiApplication;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author Kamil Jarosz
 */
public class Main {
    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption(null, "nogui", false, "");
        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (!cmd.hasOption("nogui")) {
            MancalaGuiApplication.run();
        } else {
            System.err.println("Error: only GUI is supported");
            new HelpFormatter().printHelp("program", options);
            System.exit(1);
        }
    }
}
