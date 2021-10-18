package com.gitlab.super7ramp.crosswords.solver.lib.util.solver;

import java.util.Iterator;
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
public abstract class AbstractSatisfactionProblemSolverEngine<VariableT extends Variable<ValueT>, ValueT> {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(AbstractSatisfactionProblemSolverEngine.class.getName());

    /**
     * Constructor.
     */
    public AbstractSatisfactionProblemSolverEngine() {
        // Nothing to do.
    }

    /**
     * Start the satisfaction problem exploration loop.
     *
     * @throws InterruptedException if interrupted while solving
     */
    public final void solve() throws InterruptedException {

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
            LOGGER.warning("Solver interrupted");
            throw new InterruptedException();
        }

    }

    /**
     * Unassign a variable.
     *
     * @param variable variable to unassign
     */
    private void unassign(VariableT variable) {
        onUnassignment(variable);
        variable.unassign();
    }

    /**
     * Assign a value to a variable.
     *
     * @param variable the variable
     * @param value    the value
     */
    private void assign(VariableT variable, ValueT value) {
        variable.assign(value);
        onAssignment(variable);
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
     * Action to be performed on assignment of a variable.<p>
     * This method is called after assignment, meaning variable has a value when this method is called.
     *
     * @param variable assigned variable (after assignment)
     */
    protected void onAssignment(final VariableT variable) {
        // Do nothing per default.
    }

    /**
     * Action to be performed on unassignment of a variable.
     *
     * @param variable unassigned variable (before unassignment)
     */
    protected void onUnassignment(final VariableT variable) {
        // Do nothing per default.
    }
}
