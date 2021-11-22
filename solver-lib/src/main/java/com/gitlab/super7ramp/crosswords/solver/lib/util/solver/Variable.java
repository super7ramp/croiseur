package com.gitlab.super7ramp.crosswords.solver.lib.util.solver;

import java.util.Optional;

/**
 * Represents a variable.
 *
 * @param <T> the type assignable to the variable
 */
public interface Variable<T> {

    /**
     * @return the value of the variable, if assigned
     */
    Optional<T> value();

    /**
     * Assign a value to the variable.
     *
     * @param value the value to assign
     */
    void assign(T value);

    /**
     * Clear any assignment on this variable.
     */
    void unassign();
}
