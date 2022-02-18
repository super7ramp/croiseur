import com.gitlab.super7ramp.crosswords.solver.ginsberg.CrosswordSolverProviderImpl;
import com.gitlab.super7ramp.crosswords.solver.spi.CrosswordSolverProvider;

/*
 * Solver module.
 */
module com.gitlab.super7ramp.crosswords.solver.ginsberg {

    // Base modules
    requires java.logging;

    // Implemented SPIs.
    requires com.gitlab.super7ramp.crosswords.solver.api;
    provides CrosswordSolverProvider with CrosswordSolverProviderImpl;
}