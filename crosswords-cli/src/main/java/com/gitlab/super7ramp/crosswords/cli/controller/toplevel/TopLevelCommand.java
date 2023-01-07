package com.gitlab.super7ramp.crosswords.cli.controller.toplevel;

import picocli.CommandLine.Command;

import java.util.logging.Logger;

/**
 * The top-level command. Prints help.
 */
@Command(name = "crossword", description = "Top-level command", mixinStandardHelpOptions = true)
public final class TopLevelCommand implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(TopLevelCommand.class.getName());

    @Override
    public void run() {
        // TODO print help/usage
        LOGGER.info("crossword-cli main command launched");
    }
}
