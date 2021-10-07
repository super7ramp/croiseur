package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.grid.VariableIdentifier;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Stores a crossword problem.
 * <p>
 * TODO allow assignment of a previous probe?
 * TODO share more code between probe and assign
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
        variables = new HashMap<>(someVariables);
        constraints = new HashSet<>(someConstraints);
    }

    @Override
    public Collection<WordVariable> variables() {
        return Collections.unmodifiableCollection(variables.values());
    }

    @Override
    public CrosswordProblem probe(final VariableIdentifier varId, final String value) {

        /* 1. Add updated variable. */
        final Map<VariableIdentifier, WordVariable> updatedVariables = new HashMap<>();
        updatedVariables.put(varId, variable(varId).withValue(value));

        /* 2. Add updated connected variable. */
        constraints.stream()
                .filter(constraint -> constraint.isRelatedTo(varId))
                .map(constraint -> createUpdatedConnectedVariable(varId, value, constraint))
                .forEach(entry -> updatedVariables.put(entry.getKey(), entry.getValue()));

        /* 3. Add other unmodified variables. */
        variables.forEach(updatedVariables::putIfAbsent);

        return new CrosswordProblemImpl(updatedVariables, constraints);
    }

    @Override
    public void assign(final VariableIdentifier variableIdentifier, final String value) {
        variable(variableIdentifier).assign(value);
        constraints.stream()
                .filter(constraint -> constraint.isRelatedTo(variableIdentifier))
                .forEach(constraint -> updateConnectedVariable(variableIdentifier, value, constraint));
    }


    /**
     * On a variable assignment, updates variables connected to this variable by a constraint.
     *
     * @param varId      the updated variable
     * @param value      the new value of the variable
     * @param constraint the constraint that connects the variables
     */
    private void updateConnectedVariable(final VariableIdentifier varId,
                                         final String value,
                                         final CrosswordConstraint constraint) {
        final VariableIdentifier connectedVarId = constraint.connectedVariableIdentifier(varId);
        final char updatedPart = value.charAt(constraint.index(varId));
        final int updatedPartIndex = constraint.index(connectedVarId);

        variable(connectedVarId).setLetter(updatedPartIndex, updatedPart);
    }

    /**
     * On a variable assignment, copies then updates variables connected to this variable by a constraint.
     *
     * @param varId      the updated variable
     * @param value      the new value of the variable
     * @param constraint the constraint that connects the variables
     * @return the updated connected variable copies
     */
    private Map.Entry<VariableIdentifier, WordVariable> createUpdatedConnectedVariable(final VariableIdentifier varId,
                                                                                       final String value,
                                                                                       final CrosswordConstraint constraint) {
        final VariableIdentifier connectedVarId = constraint.connectedVariableIdentifier(varId);
        final char updatedPart = value.charAt(constraint.index(varId));
        final int updatedPartIndex = constraint.index(connectedVarId);

        final WordVariable updatedConnectedVariable = variable(connectedVarId).withPart(updatedPart, updatedPartIndex);

        return Map.entry(connectedVarId, updatedConnectedVariable);
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
