/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.impl.solver;

import com.gitlab.super7ramp.crosswords.spi.solver.Dictionary;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toSet;

/**
 * A dictionary collecting results of several dictionaries.
 */
final class CompositeSolverDictionary implements Dictionary {

    /** The dictionaries. */
    private final Collection<Dictionary> dictionaries;

    /**
     * Constructs an instance.
     *
     * @param dictionariesArg the dictionaries
     */
    CompositeSolverDictionary(final Collection<Dictionary> dictionariesArg) {
        dictionaries = dictionariesArg;
    }

    @Override
    public Set<String> lookup(final Predicate<String> predicate) {
        return dictionaries.stream()
                           .flatMap(dictionary -> dictionary.lookup(predicate).stream())
                           .collect(toSet());
    }
}
