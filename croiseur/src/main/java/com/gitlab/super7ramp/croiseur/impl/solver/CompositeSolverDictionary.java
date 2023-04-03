/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.solver;

import com.gitlab.super7ramp.croiseur.spi.solver.Dictionary;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;

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
    public Set<String> words() {
        return dictionaries.stream()
                           .flatMap(dictionary -> dictionary.words().stream())
                           .collect(toCollection(LinkedHashSet::new));
    }
}
