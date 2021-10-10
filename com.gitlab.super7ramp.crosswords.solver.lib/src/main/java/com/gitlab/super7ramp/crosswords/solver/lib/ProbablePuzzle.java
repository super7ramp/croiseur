package com.gitlab.super7ramp.crosswords.solver.lib;

/**
 * Extension to the {@link Puzzle} model for probing (un)assignment without actual modification.
 */
public interface ProbablePuzzle extends Puzzle {

    /**
     * Returns a copy of this {@link ProbablePuzzle} with given {@link Assignment} applied.
     *
     * @param assignment the assignment to probe
     * @return a copy of this {@link ProbablePuzzle} with given {@link Assignment} applied
     */
    ProbablePuzzle probe(final Assignment assignment);
}
