package com.gitlab.super7ramp.crosswords.spi.solver;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Dictionary interface (to be provided).
 */
public interface Dictionary {

    /**
     * Search for words matching the given {@link Predicate}.
     *
     * @param predicate the predicate to satisfy
     * @return a set of words matching the given pattern
     */
    Set<String> lookup(final Predicate<String> predicate);
}
