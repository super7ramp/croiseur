package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import java.util.Objects;

/**
 * Unique identifier for a variable inside a given problem.
 * <p>
 * TODO check visibility and usage (not sure yet whether it should be used outside of implementation of this package)
 */
public final class SlotIdentifier {

    /**
     * Just an integer.
     */
    private final int id;

    /**
     * Constructor.
     *
     * @param anId the identifier
     */
    public SlotIdentifier(final int anId) {
        id = anId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SlotIdentifier that = (SlotIdentifier) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
