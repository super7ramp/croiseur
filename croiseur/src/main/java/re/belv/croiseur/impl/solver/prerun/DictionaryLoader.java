/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.solver.prerun;

import re.belv.croiseur.api.dictionary.DictionaryIdentifier;
import re.belv.croiseur.impl.dictionary.selection.DictionarySelector;
import re.belv.croiseur.impl.dictionary.selection.SelectedDictionary;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;
import re.belv.croiseur.spi.solver.Dictionary;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Dictionary loader.
 */
public final class DictionaryLoader {

    /** The actual dictionary providers. */
    private final DictionarySelector selector;

    /**
     * Constructs an instance.
     *
     * @param dictionaryProvidersArg the actual dictionary providers
     */
    public DictionaryLoader(final Collection<DictionaryProvider> dictionaryProvidersArg) {
        selector = new DictionarySelector(dictionaryProvidersArg);
    }

    /**
     * Loads and aggregates requested dictionaries into a single {@link Dictionary} suitable for
     * solver, if any dictionary found.
     *
     * @param dictionaries the ids of the requested dictionaries
     * @return the dictionary, aggregating all the requested dictionaries, if any found
     */
    public Optional<Dictionary> load(final Collection<DictionaryIdentifier> dictionaries) {

        final List<SelectedDictionary> selectedDictionaries;
        if (dictionaries.isEmpty()) {
            // As per SolveRequest spec, no given dictionary means default dictionary
            selectedDictionaries = selector.selectDefault()
                                           .map(Collections::singletonList)
                                           .orElseGet(Collections::emptyList);
        } else {
            selectedDictionaries = selector.select(dictionaries);
        }

        // At least one dictionary is necessary for solving
        if (selectedDictionaries.isEmpty()) {
            return Optional.empty();
        }

        final List<Dictionary> selectedSolverDictionaries =
                selectedDictionaries.stream().map(this::toSolverDictionary).toList();

        return Optional.of(new CompositeSolverDictionary(selectedSolverDictionaries));
    }

    /**
     * Converts a {@link SelectedDictionary} to dictionary of solver SPI.
     *
     * @param dictionary the selected dictionary
     * @return the dictionary of solver SPI
     */
    private Dictionary toSolverDictionary(final SelectedDictionary dictionary) {
        return dictionary::words;
    }
}
