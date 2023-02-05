/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui;

import com.gitlab.super7ramp.croiseur.api.CrosswordService;
import com.gitlab.super7ramp.croiseur.spi.clue.ClueProvider;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * A utility class to load {@link CrosswordService}.
 */
final class CrosswordServiceLoader {

    /**
     * Private constructor to prevent instantiation.
     */
    private CrosswordServiceLoader() {
        // Nothing to do.
    }

    /**
     * Loads all providers of given class.
     *
     * @param providerClass the provider class
     * @param <T>           the type of provider
     * @return the providers
     */
    private static <T> Collection<T> load(final Class<T> providerClass) {
        return ServiceLoader.load(providerClass)
                            .stream()
                            .map(Supplier::get)
                            .toList();
    }

    /**
     * Creates the {@link CrosswordService}.
     *
     * @param presenter the presenter
     * @return the {@link CrosswordService}
     */
    static CrosswordService load(final Presenter presenter) {
        final Collection<DictionaryProvider> dictionaryProviders = load(DictionaryProvider.class);
        final Collection<ClueProvider> clueProviders = load(ClueProvider.class);
        final Collection<CrosswordSolver> solvers = load(CrosswordSolver.class);
        return CrosswordService.create(dictionaryProviders, solvers, clueProviders, presenter);
    }
}
