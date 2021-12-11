package com.gitlab.super7ramp.crosswords.cli;

/**
 * Main.
 */
public final class Main {

    /**
     * Private constructor.
     */
    private Main() {
        // Nothing to do.
    }

    /**
     * Program entry point.
     *
     * @param args command-line arguments
     */
    public static void main(final String[] args) {
        final CrosswordCliApplication app = new CrosswordCliApplication();
        final int exitCode = app.run(args);
        System.exit(exitCode);
    }
}
