import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;

/**
 * Crossword application logic.
 * <p>
 * This module is a library providing several high-level crossword puzzle use-cases.
 */
module com.gitlab.super7ramp.crosswords {

    /*
     * Requires plugins.
     *
     * Transitive since plugin implementations can be explicitly passed in factory and hence are
     * visible from client API.
     */
    requires transitive com.gitlab.super7ramp.crosswords.spi.dictionary;
    requires transitive com.gitlab.super7ramp.crosswords.spi.solver;
    requires transitive com.gitlab.super7ramp.crosswords.spi.presenter;

    // Exports only API, keeps implementation hidden.
    exports com.gitlab.super7ramp.crosswords.api;
    exports com.gitlab.super7ramp.crosswords.api.dictionary;
    exports com.gitlab.super7ramp.crosswords.api.solver;

    // Uses plugins since plugin implementations can be implicitly loaded in factory.
    uses DictionaryProvider;
    uses CrosswordSolver;
    uses Presenter;
}