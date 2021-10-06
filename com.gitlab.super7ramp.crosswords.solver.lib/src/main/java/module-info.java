import com.gitlab.super7ramp.crosswords.db.api.spi.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.solver.api.spi.CrosswordSolverProvider;
import com.gitlab.super7ramp.crosswords.solver.lib.CrosswordSolverProviderImpl;

/*
 * Solver module.
 */
module com.gitlab.super7ramp.crosswords.solver.lib {
    // Base modules
    requires java.logging;

    // Required SPIs.
    requires com.gitlab.super7ramp.crosswords.db.api;
    uses DictionaryProvider;

    // Implemented SPIs.
    requires com.gitlab.super7ramp.crosswords.solver.api;
    provides CrosswordSolverProvider
            with CrosswordSolverProviderImpl;
}