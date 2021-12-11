/**
 * CLI for crossword solving.
 */
module com.gitlab.super7ramp.crosswords.cli {
    // Base modules
    requires java.logging;

    // Crossword libraries (APIs)
    requires com.gitlab.super7ramp.crosswords.dictionary.api;
    requires com.gitlab.super7ramp.crosswords.solver.api;

    // CLI framework
    requires info.picocli;
}