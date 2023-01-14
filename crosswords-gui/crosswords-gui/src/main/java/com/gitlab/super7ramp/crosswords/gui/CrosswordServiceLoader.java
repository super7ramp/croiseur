/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.gui;

import com.gitlab.super7ramp.crosswords.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;

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
     * Creates the {@link CrosswordService}.
     *
     * @param presenter the presenter
     * @return the {@link CrosswordService}
     */
    static CrosswordService load(final Presenter presenter) {
        final Collection<DictionaryProvider> dictionaryProviders =
                ServiceLoader.load(DictionaryProvider.class).stream()
                             .map(Supplier::get)
                             .toList();
        final Collection<CrosswordSolver> solvers =
                ServiceLoader.load(CrosswordSolver.class)
                             .stream()
                             .map(Supplier::get)
                             .toList();
        return CrosswordService.create(dictionaryProviders, solvers, presenter);
    }
}
