/**
 * Crossword application logic.
 * <p>
 * This module is a library providing several high-level crossword puzzle use-cases.
 */
module com.gitlab.super7ramp.crosswords {

    requires com.gitlab.super7ramp.crosswords.dictionary.api;
    requires com.gitlab.super7ramp.crosswords.solver.api;

    // Export only API, keep implementation hidden
    exports com.gitlab.super7ramp.crosswords.api;
    exports com.gitlab.super7ramp.crosswords.api.dictionary;
    exports com.gitlab.super7ramp.crosswords.api.solve;
}