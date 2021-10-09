package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.lib.comparators.Comparators;
import com.gitlab.super7ramp.crosswords.solver.lib.db.WordDatabase;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.VariableIdentifier;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link Backtracker}.
 */
final class BacktrackerImpl implements Backtracker {

    /**
     * Access to the grid.
     */
    private final CrosswordProblem grid;

    /**
     * Dictionary.
     */
    private final WordDatabase dictionary;

    /**
     * Constructor.
     *
     * @param aGrid access to the grid
     */
    BacktrackerImpl(final CrosswordProblem aGrid, final WordDatabase aDictionary) {
        grid = aGrid;
        dictionary = aDictionary;
    }

    @Override
    public Set<WordVariable> backtrackFrom(final WordVariable variable) {
        final WordVariable mostProblematicConnectedVariable = grid.constraints().stream()
                .filter(c -> c.isRelatedTo(variable.uid()))
                .map(c -> {
                    final VariableIdentifier connectedVarId = c.connectedVariableIdentifier(variable.uid());
                    final WordVariable connectedVar = grid.variable(connectedVarId);
                    return Map.entry(c, connectedVar);
                })
                .filter(entry -> entry.getValue().value().isPresent())
                .map(entry -> {
                    final CrosswordConstraint constraint = entry.getKey();
                    final WordVariable connectedVar = entry.getValue();
                    return connectedVar.withoutPart(constraint.index(connectedVar.uid()));
                })
                .min(Comparators.byNumberOfCandidates(dictionary))
                .orElseThrow(() -> new IllegalStateException("Failed to backtrack, aborting"));

        dictionary.resetBlacklist();
        dictionary.blacklist(mostProblematicConnectedVariable,
                grid.variable(mostProblematicConnectedVariable.uid()).value().orElseThrow(IllegalStateException::new));
        grid.unassign(mostProblematicConnectedVariable.uid());

        return Collections.singleton(mostProblematicConnectedVariable);
    }
}
