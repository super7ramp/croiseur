import com.gitlab.super7ramp.crosswords.solver.ginsberg.plugin.GinsbergCrosswordSolver;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;

/**
 * Solver plugin module.
 */
module com.gitlab.super7ramp.crosswords.solver.ginsberg.plugin {
    requires com.gitlab.super7ramp.crosswords.solver.ginsberg;
    requires transitive com.gitlab.super7ramp.crosswords.spi.solver;
    provides CrosswordSolver with GinsbergCrosswordSolver;
}