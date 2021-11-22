/**
 * Acceptance tests for the crossword project.
 */
module com.gitlab.super7ramp.crosswords.tests {
    // Base modules
    requires java.logging;

    // Modules under test
    requires com.gitlab.super7ramp.crosswords.dictionary.api;
    requires com.gitlab.super7ramp.crosswords.solver.api;

    // Test frameworks
    requires org.junit.jupiter.api;
}