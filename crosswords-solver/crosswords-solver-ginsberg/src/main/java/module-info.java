import com.gitlab.super7ramp.crosswords.solver.ginsberg.CrosswordSolverImpl;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;

/**
 * Solver module.
 */
module com.gitlab.super7ramp.crosswords.solver.ginsberg {

    requires java.logging;
    requires com.gitlab.super7ramp.crosswords.spi.solver;

    provides CrosswordSolver with CrosswordSolverImpl;
}