package com.gitlab.super7ramp.crosswords.cli;

import picocli.CommandLine.Command;

import java.util.logging.Logger;

@Command(
        name = "crossword",
        description = "Top-level command",
        mixinStandardHelpOptions = true
)
final class TopLevelCommand implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(TopLevelCommand.class.getName());

    @Override
    public void run() {
        LOGGER.info("crossword-cli main command launched");
    }
}