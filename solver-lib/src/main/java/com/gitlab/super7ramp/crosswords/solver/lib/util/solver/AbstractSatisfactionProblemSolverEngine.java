package com.gitlab.super7ramp.crosswords.solver.lib.util.solver;

import java.util.Iterator;
import java.util.Optional;

/**
 * Base abstraction for SAP solving.
 *
 * <p>This class splits the resolution of the satisfaction problem into three sub-problems:
 *
 * <ul>
 *     <li>Variable choice</li>
 *     <li>Variable instantiation</li>
 *     <li>Backtracking</li>
 * </ul>
 *
 * This base abstraction combines the implementation for these three sub-problems to solve the satisfaction problem.
 *
 * <h2>Variable choice</h2>
 *
 * This consists in choosing the next variable to instantiate. It is materialized here by the {@link Iterator}
 * returned by the {@link #variables()} method.
 *
 * <h2>Variable instantiation</h2>
 *
 * When a variable is chosen, it must be instantiated to a valid value. It is materialized here by the
 * {@link Optional<VariableT>} returned by the {@link #candidate(VariableT)} method.
 *
 * <h2>Backtracking</h2>
 *
 * Backtracking is the strategy to apply when a dead-end is reached, i.e. when the chosen variable cannot be
 * instantiated. In order to continue the search of a valid solution, one or several already instantiated variables
 * must be changed. This heuristic is performed via the {@link #backtrackFrom(VariableT)} method.
 *
 * @param <VariableT> type of variable
 * @param <ValueT> type of value assignable to the variables
 */
public abstract class AbstractSatisfactionProblemSolverEngine<VariableT, ValueT> {

    /**
     * Constructor.
     */
    protected AbstractSatisfactionProblemSolverEngine() {
        // Nothing to do.
    }

    /**
     * Start the satisfaction problem exploration loop.
     *
     * @return <code>true</code> iff solver loop has terminated without error
     * @throws InterruptedException if interrupted while solving
     */
    public final boolean solve() throws InterruptedException {

        final Iterator<VariableT> variables = variables();
        boolean backtrackError = false;
        while (!Thread.currentThread().isInterrupted() && !backtrackError && variables.hasNext()) {

            final VariableT variable = variables.next();
            final Optional<ValueT> candidate = candidate(variable);

            if (candidate.isPresent()) {
                assign(variable, candidate.get());
            } else {
                backtrackError = !backtrackFrom(variable);
            }
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Solver interrupted");
        }

        return !backtrackError;
    }

    /**
     * Assign a value to a variable.
     *
     * @param variable the variable
     * @param value    the value
     */
    protected abstract void assign(final VariableT variable, final ValueT value);

    /**
     * An iterator on variables.
     *
     * @return an iterator on variables
     */
    protected abstract Iterator<VariableT> variables();

    /**
     * The candidate to select for the given variable.
     *
     * @param variable the variable to look candidate for
     * @return the selected value
     */
    protected abstract Optional<ValueT> candidate(final VariableT variable);

    /**
     * Apply the backtrack strategy when variable cannot be instantiated.
     *
     * @param variable variable which cannot be instantiated
     * @return <code>true</code> iff backtrack has succeeded
     */
    protected abstract boolean backtrackFrom(final VariableT variable);
}