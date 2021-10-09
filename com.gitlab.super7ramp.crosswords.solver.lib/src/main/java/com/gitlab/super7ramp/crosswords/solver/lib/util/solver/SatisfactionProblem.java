package com.gitlab.super7ramp.crosswords.solver.lib.util.solver;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

/**
 * A 2-SAP problem.
 *
 * @param <T> type of the value
 */
public interface SatisfactionProblem<T> {

    /**
     * A constraint, i.e. a relation which links two variable assignments.
     */
    interface Constraint<Candidate> extends BiPredicate<Candidate, Candidate> {
        // Marker interface
    }

    /**
     * The variables to assign, associated to their respective candidate values.
     *
     * @return the variables to assign and their candidate values
     */
    Map<Variable<T>, Set<T>> variables();

    /**
     * The constraints to satisfy.
     *
     * @return the constraint to satisfy
     */
    Map<Variable<T>, Collection<Constraint<T>>> constraints();

}
