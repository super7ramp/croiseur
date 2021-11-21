/**
 * Acceptance tests for the crossword project.
 */
module com.gitlab.super7ramp.crosswords.cli {
    // Base modules
    requires java.logging;

    // Crossword libraries
    requires com.gitlab.super7ramp.crosswords.dictionary.api;
    requires com.gitlab.super7ramp.crosswords.solver.api;
}