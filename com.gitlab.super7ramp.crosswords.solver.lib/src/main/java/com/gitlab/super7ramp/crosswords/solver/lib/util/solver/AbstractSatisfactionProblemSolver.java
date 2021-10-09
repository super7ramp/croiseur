package com.gitlab.super7ramp.crosswords.solver.lib.util.solver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

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
 * must be changed. This heuristic is performed via the {@link #backtrackFrom(Variable)} method.
 *
 * @param <VariableT> type of variable
 * @param <ValueT> type of value assignable to the variables
 */
public abstract class AbstractSatisfactionProblemSolver<VariableT extends Variable<ValueT>, ValueT> {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(AbstractSatisfactionProblemSolver.class.getName());

    /**
     * The assignment being built, i.e. an assignment of a value for each variable.
     */
    private final Map<VariableT, ValueT> assignment;

    /**
     * Constructor.
     */
    public AbstractSatisfactionProblemSolver() {
        assignment = new HashMap<>();
    }

    /**
     * Try to solve the satisfaction problem.
     *
     * @return an assignment for the given problem, if any found
     * @throws InterruptedException if interrupted while solving
     */
    public final Optional<Map<VariableT, ValueT>> solve() throws InterruptedException {

        final Iterator<VariableT> variables = variables();
        while (!Thread.currentThread().isInterrupted() && variables.hasNext()) {

            final VariableT variable = variables.next();
            final Optional<ValueT> candidate = candidate(variable);

            if (candidate.isPresent()) {
                LOGGER.fine(() -> "Assigning [" + candidate.get() + "] to variable [" + variable + "]");
                assign(variable, candidate.get());
            } else {
                LOGGER.fine(() -> "No candidate for [" + variable + "], backtracking.");
                backtrackFrom(variable).forEach(this::unassign);
            }
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        if (isAssignmentComplete()) {
            return Optional.of(assignment);
        }

        return Optional.empty();
    }

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
     * @return the unassigned variables
     */
    protected abstract Set<VariableT> backtrackFrom(final VariableT variable);

    /**
     * Assign a value to a variable variable.
     *
     * @param variable the variable to assign
     * @param value    the chosen candidate value
     */
    protected abstract void instantiate(VariableT variable, ValueT value);

    /**
     * Assign a value to a variable, i.e. instantiate the variable.
     *
     * @param variable the variable to assign
     * @param value    the chosen candidate value
     */
    private void assign(final VariableT variable, final ValueT value) {
        instantiate(variable, value);
        assignment.put(variable, value);
    }

    /**
     * Un-assign the value of a variable.
     *
     * @param variable the variable to un-assign
     */
    private void unassign(final VariableT variable) {
        assignment.remove(variable);
    }

    /**
     * Check whether the current assignment is complete.
     *
     * @return <code>true</code> if the assignment is complete, <code>false</code> otherwise
     */
    private boolean isAssignmentComplete() {
        // TODO check constraints are respected
        return true;
    }

}
