package com.gitlab.super7ramp.crosswords.solver.lib.core;

import java.util.Objects;

/**
 * Unique identifier for a variable inside a given problem.
 */
public final class SlotIdentifier {

    /** Just an integer. */
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
    public String toString() {
        return "SlotIdentifier{" +
                "id=" + id +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * The identifier as an integer.
     *
     * @return the identifier as an integer
     */
    public int id() {
        return id;
    }
}
