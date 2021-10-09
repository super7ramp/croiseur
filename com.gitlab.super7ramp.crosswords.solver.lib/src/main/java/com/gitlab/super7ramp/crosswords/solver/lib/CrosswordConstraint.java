package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.lib.grid.VariableIdentifier;
import com.gitlab.super7ramp.crosswords.solver.lib.util.solver.SatisfactionProblem;

import java.util.Objects;

/**
 * Implementation of {@link SatisfactionProblem.Constraint}.
 */
public final class CrosswordConstraint implements SatisfactionProblem.Constraint<String> {

    /** The first variable identifier. */
    private final VariableIdentifier firstVarId;

    /** Part identifier of first variable on which this constraint applies. */
    private final int firstVarPartIndex;

    /** The second variable identifier. */
    private final VariableIdentifier secondVarId;

    /** Part identifier of second variable on which this constraint applies. */
    private final int secondVarPartIndex;

    /**
     * Constructor.
     *
     * @param aFirstVarId the first variable identifier
     * @param aFirstVarPartIndex the index of the part of the first variable on which this constraint applies
     * @param aSecondVarId the second variable identifier
     * @param aSecondVarPartIndex the index of the part of the second variable on which this constraint applies
     */
    public CrosswordConstraint(final VariableIdentifier aFirstVarId, final int aFirstVarPartIndex,
                               final VariableIdentifier aSecondVarId, final int aSecondVarPartIndex) {
        firstVarId = Objects.requireNonNull(aFirstVarId);
        firstVarPartIndex = aFirstVarPartIndex;
        secondVarId = Objects.requireNonNull(aSecondVarId);
        secondVarPartIndex = aSecondVarPartIndex;
    }

    /**
     * Return the variable connected by this constraint to the given variable.
     *
     * @param id the variable of one of the ID of this constraint
     * @return the variable connected by this constraint to the given variable.
     * @throws IllegalArgumentException if none of the variables referenced by this constraint has the given ID
     */
    public VariableIdentifier connectedVariableIdentifier(final VariableIdentifier id) {
        if (firstVarId.equals(id)) {
            return secondVarId;
        }
        if (secondVarId.equals(id)) {
            return firstVarId;
        }
        throw new IllegalArgumentException("This constraint does not refer to variable with ID " + id);
    }

    /**
     * The index of the part of the given variable this constraint applies on.
     *
     * @param id the variable index
     * @return the index of the part of the given variable this constraint applies on.
     */
    public int index(final VariableIdentifier id) {
        if (firstVarId.equals(id)) {
            return firstVarPartIndex;
        }
        if (secondVarId.equals(id)) {
            return secondVarPartIndex;
        }
        throw new IllegalArgumentException("This constraint does not refer to variable with ID " + id);
    }

    /**
     * Returns <code>true</code> if this constraint applies on the variable with the given ID.
     *
     * @param id the variable ID
     * @return <code>true</code> if this constraint applies on the variable with the given ID.
     */
    public boolean isRelatedTo(final VariableIdentifier id) {
        return firstVarId.equals(id) || secondVarId.equals(id);
    }

    @Override
    public boolean test(final String value1, final String value2) {
        return value1.charAt(firstVarPartIndex) == value2.charAt(secondVarPartIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CrosswordConstraint that = (CrosswordConstraint) o;
        return firstVarPartIndex == that.firstVarPartIndex && secondVarPartIndex == that.secondVarPartIndex && Objects.equals(firstVarId, that.firstVarId) && Objects.equals(secondVarId, that.secondVarId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstVarId, firstVarPartIndex, secondVarId, secondVarPartIndex);
    }
}
