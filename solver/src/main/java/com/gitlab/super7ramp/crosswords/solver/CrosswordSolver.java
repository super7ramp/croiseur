package main.java.com.gitlab.super7ramp.crosswords.solver;

import main.java.com.gitlab.super7ramp.crosswords.db.WordDatabase;
import main.java.com.gitlab.super7ramp.crosswords.solver.comparators.Comparators;
import main.java.com.gitlab.super7ramp.crosswords.util.solver.AbstractSatisfactionProblemSolver;

import java.util.Iterator;
import java.util.Optional;

/**
 * Implementation of {@link AbstractSatisfactionProblemSolver} for crosswords.
 */
public final class CrosswordSolver extends AbstractSatisfactionProblemSolver<WordVariable, String> {

    private final Iterator<WordVariable> variables;

    private final CandidateChooser candidateChooser;

    CrosswordSolver(
            final CrosswordProblem grid,
            final WordDatabase dictionary) {
        variables = new WordVariableIterator(grid, assignment, Comparators.byNumberOfCandidates(dictionary));
        candidateChooser = new CandidateChooser(grid, dictionary);
    }


    @Override
    protected Iterator<WordVariable> variables() {
        return variables;
    }

    @Override
    protected Optional<String> candidate(final WordVariable wordVariable) {
        return candidateChooser.find(wordVariable);
    }

    @Override
    protected void backtrackFrom(final WordVariable wordVariable) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
