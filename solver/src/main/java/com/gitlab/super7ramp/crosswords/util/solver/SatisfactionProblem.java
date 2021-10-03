package main.java.com.gitlab.super7ramp.crosswords.util.solver;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

/**
 * A 2-SAP problem.
 *
 * @param <Variable> type of the variable
 * @param <Candidate> type of the value
 */
public interface SatisfactionProblem<Variable, Candidate> {

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
    Map<Variable, Set<Candidate>> variables();

    /**
     * The constraints to satisfy.
     *
     * @return the constraint to satisfy
     */
    Map<Variable, Collection<Constraint<Candidate>>> constraints();

}
