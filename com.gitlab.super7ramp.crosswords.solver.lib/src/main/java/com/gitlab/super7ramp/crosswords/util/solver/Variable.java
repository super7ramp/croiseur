package com.gitlab.super7ramp.crosswords.util.solver;

import java.util.Optional;

/**
 * Represents a variable. Immutable.
 *
 * @param <T> the type assignable to the variable
 */
public interface Variable<T> {

    /**
     * @return the value of the variable, if assigned
     */
    Optional<T> value();
}
