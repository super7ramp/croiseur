import com.gitlab.super7ramp.crosswords.solver.paulgb.plugin.CrosswordComposerSolver;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;

/**
 * Solver plugin module.
 */
module com.gitlab.super7ramp.crosswords.solver.paulgb.plugin {
    requires com.gitlab.super7ramp.crosswords.solver.paulgb;
    requires transitive com.gitlab.super7ramp.crosswords.spi.solver;
    provides CrosswordSolver with CrosswordComposerSolver;
}