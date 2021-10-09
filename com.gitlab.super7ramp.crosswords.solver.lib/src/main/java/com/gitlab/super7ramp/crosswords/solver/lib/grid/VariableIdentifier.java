package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import java.util.Objects;

/**
 * Unique identifier for a variable inside a given problem.
 */
public final class VariableIdentifier {

    /**
     * Just an integer.
     */
    private final int id;

    /**
     * Constructor.
     *
     * @param anId the identifier
     */
    public VariableIdentifier(final int anId) {
        id = anId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VariableIdentifier that = (VariableIdentifier) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
