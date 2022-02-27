/**
 * CLI for crossword solving.
 */
module com.gitlab.super7ramp.crosswords.cli {

    // Base modules
    requires java.logging;

    // CLI framework
    requires info.picocli;

    // Core library
    requires com.gitlab.super7ramp.crosswords;

    // Core library runtime dependencies
    uses com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryProvider;
    uses com.gitlab.super7ramp.crosswords.solver.spi.CrosswordSolverProvider;

    // Open for reflection to CLI framework
    opens com.gitlab.super7ramp.crosswords.cli to info.picocli;
    opens com.gitlab.super7ramp.crosswords.cli.dictionary to info.picocli;
    opens com.gitlab.super7ramp.crosswords.cli.solve to info.picocli;
    opens com.gitlab.super7ramp.crosswords.cli.toplevel to info.picocli;
}