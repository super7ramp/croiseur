package com.gitlab.super7ramp.crosswords.grid;

import java.util.Objects;

/**
 * Unique identifier for a variable inside a given problem.
 */
public class VariableIdentifier {

    private final int id;

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
