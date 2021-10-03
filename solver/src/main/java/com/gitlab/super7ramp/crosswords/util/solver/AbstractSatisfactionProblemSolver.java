package main.java.com.gitlab.super7ramp.crosswords.util.solver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
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
 * {@link Optional<Candidate>} returned by the {@link #candidate(Variable)} method.
 *
 * <h2>Backtracking</h2>
 *
 * Backtracking is the strategy to apply when a dead-end is reached, i.e. when the chosen variable cannot be
 * instantiated. In order to continue the search of a valid solution, one or several already instantiated variables
 * must be changed. This heuristic is performed via the {@link #backtrackFrom(Variable)} method.
 *
 * @param <Variable> variable type
 * @param <Candidate> candidate type
 */
public abstract class AbstractSatisfactionProblemSolver<Variable, Candidate>  {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(AbstractSatisfactionProblemSolver.class.getName());

    /** The assignment being built, i.e. an assignment of a value for each variable.
     * TODO check visibility; currently children need it
     */
    protected final Map<Variable, Candidate> assignment;

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
    public final Optional<Map<Variable, Candidate>> solve() throws InterruptedException {

        final Iterator<Variable> variables = variables();
        while (!Thread.currentThread().isInterrupted() && variables.hasNext()) {

            final Variable variable = variables.next();
            final Optional<Candidate> candidate = candidate(variable);

            if (candidate.isPresent()) {
                LOGGER.fine(() -> "Assigning [" + candidate.get() + "] to variable [" + variable + "]");
                assignVariable(variable, candidate.get());
            } else {
                LOGGER.fine(() -> "No candidate for [" + variable + "], backtracking.");
                backtrackFrom(variable);
            }
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
    protected abstract Iterator<Variable> variables();

    /**
     * The candidate to select for the given variable.
     *
     * @param variable the variable to look candidate for
     * @return the selected value
     */
    protected abstract Optional<Candidate> candidate(final Variable variable);

    /**
     * Apply the backtrack strategy when variable cannot be instantiated.
     *
     * @param variable variable which cannot be instantiated
     */
    protected abstract void backtrackFrom(final Variable variable);

    /**
     * Assign a value to a variable, i.e. instantiate the variable.
     *
     * @param variable the variable to assign
     * @param value the chosen candidate value
     */
    private void assignVariable(final Variable variable, final Candidate value) {
        assignment.put(variable, value);
    }

    /**
     * Check whether the current assignment is complete.
     * @return <code>true</code> if the assignment is complete, <code>false</code> otherwise
     */
    private boolean isAssignmentComplete() {
        // TODO check constraints are respected
        return !Thread.currentThread().isInterrupted();
    }

}
