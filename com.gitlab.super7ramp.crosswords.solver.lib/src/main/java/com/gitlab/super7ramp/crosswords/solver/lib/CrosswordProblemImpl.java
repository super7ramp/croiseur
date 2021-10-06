package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.grid.VariableIdentifier;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Stores a crossword problem.
 */
public final class CrosswordProblemImpl implements CrosswordProblem {

    /** The variables. */
    private final Map<VariableIdentifier, WordVariable> variables;

    /** The constraints. */
    private final Set<CrosswordConstraint> constraints;

    /**
     * Constructor.
     *
     * @param someVariables variables
     * @param someConstraints constraints
     */
    CrosswordProblemImpl(final Map<VariableIdentifier, WordVariable> someVariables,
                         final Set<CrosswordConstraint> someConstraints) {
        variables = Collections.unmodifiableMap(someVariables);
        constraints = Collections.unmodifiableSet(someConstraints);
    }

    @Override
    public Collection<WordVariable> variables() {
        return variables.values();
    }

    @Override
    public CrosswordProblem assign(final VariableIdentifier varId, final String value) {

        /* 1. Add updated variable. */
        final Map<VariableIdentifier, WordVariable> updatedVariables = new HashMap<>();
        updatedVariables.put(varId, variable(varId).withValue(value));

        /* 2. Add updated connected variable. */
        constraints.stream()
            .filter(constraint -> constraint.isRelatedTo(varId))
            .map(c -> {
                final VariableIdentifier connectedVarId = c.connectedVariableIdentifier(varId);
                final WordVariable updatedConnectedVariable = variable(connectedVarId)
                        .withPart(value.charAt(c.index(varId)), c.index(connectedVarId));
                return Map.entry(connectedVarId, updatedConnectedVariable);
            })
            .forEach(entry -> updatedVariables.put(entry.getKey(), entry.getValue()));

        /* 3. Add other unmodified variables. */
        variables.forEach(updatedVariables::putIfAbsent);

        return new CrosswordProblemImpl(updatedVariables, constraints);
    }

    /**
     * Get variable from variable identifier.
     *
     * @param varId the variable identifier
     * @return the variable
     * @throws IllegalArgumentException if variable id is unknown
     */
    private WordVariable variable(VariableIdentifier varId) {
        final WordVariable result = variables.get(varId);
        if (result != null) {
            return result;
        }
        throw new IllegalArgumentException("Variable identifier " + varId + " is unknown");
    }
}
