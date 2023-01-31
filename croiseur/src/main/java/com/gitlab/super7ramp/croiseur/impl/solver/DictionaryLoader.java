/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.solver;

import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.croiseur.impl.common.DictionarySelection;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.solver.Dictionary;

import java.util.Collection;
import java.util.Optional;

/**
 * Dictionary loader.
 */
final class DictionaryLoader {

    /** The actual dictionary providers. */
    private final Collection<DictionaryProvider> dictionaryProviders;

    /**
     * Constructs an instance.
     *
     * @param dictionaryProvidersArg the actual dictionary providers
     */
    DictionaryLoader(final Collection<DictionaryProvider> dictionaryProvidersArg) {
        dictionaryProviders = dictionaryProvidersArg;
    }

    /**
     * Loads and aggregates requested dictionaries into a single {@link Dictionary} suitable for
     * solver, if any dictionary found.
     *
     * @param dictionaries the ids of the requested dictionaries
     * @return the dictionary, aggregating all the requested dictionaries, if any found
     */
    Optional<Dictionary> load(final Collection<DictionaryIdentifier> dictionaries) {

        // Create a DictionarySelection from the received event
        final DictionarySelection selection;
        if (dictionaries.isEmpty()) {
            // As per SolveRequest spec, no given dictionary means default dictionary
            selection = DictionarySelection.byDefault();
        } else {
            selection = dictionaries.stream()
                                    .map(DictionarySelection::byId)
                                    .reduce(DictionarySelection.none(), DictionarySelection::or);
        }

        // Retrieve all selected dictionaries
        final Collection<Dictionary> selectedDictionaries =
                selection.apply(dictionaryProviders)
                         .stream()
                         .flatMap(dictionaryProvider -> dictionaryProvider.get()
                                                                          .stream())
                         .map(this::toSolverDictionary)
                         .toList();

        // At least one dictionary is necessary for solving
        if (selectedDictionaries.isEmpty()) {
            return Optional.empty();
        }

        // Return a composite of all selected dictionaries
        return Optional.of(new CompositeSolverDictionary(selectedDictionaries));

    }

    /**
     * Converts a dictionary from dictionary SPI to dictionary of solver SPI.
     *
     * @param dictionary the dictionary from dictionary SPI
     * @return the dictionary of solver SPI
     */
    private Dictionary toSolverDictionary(final com.gitlab.super7ramp.croiseur.spi.dictionary.Dictionary dictionary) {
        return dictionary::lookup;
    }
}
