/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.solver.prerun;

import static java.util.stream.Collectors.toCollection;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import re.belv.croiseur.spi.solver.Dictionary;

/** A dictionary collecting results of several dictionaries. */
final class CompositeSolverDictionary implements Dictionary {

    /** The words of the dictionary. */
    private final Collection<String> words;

    /**
     * Constructs an instance.
     *
     * @param dictionaries the dictionaries
     */
    CompositeSolverDictionary(final List<Dictionary> dictionaries) {
        if (dictionaries.size() == 1) {
            // Avoid creating a new collection when only one dictionary wrapped
            words = dictionaries.get(0).words();
        } else {
            words = dictionaries.stream()
                    .flatMap(dictionary -> dictionary.words().stream())
                    .collect(toCollection(LinkedHashSet::new));
        }
    }

    @Override
    public Collection<String> words() {
        return words;
    }
}
