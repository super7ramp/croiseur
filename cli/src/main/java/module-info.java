/**
 * CLI for crossword solving.
 */
module com.gitlab.super7ramp.crosswords.cli {

    // Base modules
    requires java.logging;

    // Crossword libraries dependencies
    requires com.gitlab.super7ramp.crosswords.dictionary.api;
    requires com.gitlab.super7ramp.crosswords.solver.api;

    // Crossword libraries runtime dependencies
    uses com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryProvider;
    uses com.gitlab.super7ramp.crosswords.solver.spi.CrosswordSolverProvider;

    // CLI framework dependencies
    requires info.picocli;

    // Open for reflection to CLI framework
    opens com.gitlab.super7ramp.crosswords.cli to info.picocli;
}