/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.function.Supplier;
import re.belv.croiseur.api.CrosswordService;
import re.belv.croiseur.spi.clue.ClueProvider;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;
import re.belv.croiseur.spi.presenter.Presenter;
import re.belv.croiseur.spi.puzzle.codec.PuzzleDecoder;
import re.belv.croiseur.spi.puzzle.codec.PuzzleEncoder;
import re.belv.croiseur.spi.puzzle.repository.DummyPuzzleRepository;
import re.belv.croiseur.spi.puzzle.repository.PuzzleRepository;
import re.belv.croiseur.spi.solver.CrosswordSolver;

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
        final Collection<DictionaryProvider> dictionaryProviders = load(DictionaryProvider.class);
        final Collection<CrosswordSolver> solvers = load(CrosswordSolver.class);
        final Collection<ClueProvider> clueProviders = load(ClueProvider.class);
        final Collection<PuzzleDecoder> puzzleDecoders = load(PuzzleDecoder.class);
        final Collection<PuzzleEncoder> puzzleEncoders = load(PuzzleEncoder.class);
        final PuzzleRepository puzzleRepository =
                load(PuzzleRepository.class).stream().findFirst().orElseGet(DummyPuzzleRepository::new);
        return CrosswordService.create(
                dictionaryProviders,
                solvers,
                clueProviders,
                puzzleDecoders,
                puzzleEncoders,
                puzzleRepository,
                presenter);
    }

    /**
     * Loads all the implementations of the given service class.
     *
     * @param clazz the service provider class
     * @param <T>   the type of the service
     * @return all the implementations of the given service class
     */
    private static <T> Collection<T> load(final Class<T> clazz) {
        return ServiceLoader.load(clazz).stream().map(Supplier::get).toList();
    }
}
