/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.web.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import re.belv.croiseur.api.CrosswordService;
import re.belv.croiseur.api.puzzle.PuzzleService;
import re.belv.croiseur.api.puzzle.persistence.PuzzlePersistenceService;
import re.belv.croiseur.api.solver.SolverService;
import re.belv.croiseur.spi.clue.ClueProvider;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;
import re.belv.croiseur.spi.presenter.Presenter;
import re.belv.croiseur.spi.puzzle.codec.PuzzleDecoder;
import re.belv.croiseur.spi.puzzle.codec.PuzzleEncoder;
import re.belv.croiseur.spi.puzzle.repository.PuzzleRepository;
import re.belv.croiseur.spi.solver.CrosswordSolver;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * Declares core services as Spring beans so that they are accessible from controllers.
 */
@Configuration
public class ServiceConfiguration {

    /** The Spring application context. */
    private final ApplicationContext applicationContext;

    /**
     * Constructs an instance.
     *
     * @param applicationContextArg the application context
     */
    public ServiceConfiguration(final ApplicationContext applicationContextArg) {
        applicationContext = applicationContextArg;
    }

    /**
     * The root {@link CrosswordService} bean.
     *
     * @return the crossword service
     */
    @Bean
    public CrosswordService crosswordService() {
        // Rely on module path and ServiceLoader for core dependencies which we don't need to
        // configure programmatically
        final Collection<DictionaryProvider> dictionaryProviders = load(DictionaryProvider.class);
        final Collection<CrosswordSolver> solvers = load(CrosswordSolver.class);
        final Collection<ClueProvider> clueProviders = load(ClueProvider.class);
        final Collection<PuzzleDecoder> puzzleDecoders = load(PuzzleDecoder.class);
        final Collection<PuzzleEncoder> puzzleEncoders = load(PuzzleEncoder.class);
        final PuzzleRepository puzzleRepository =
                load(PuzzleRepository.class).stream().findFirst().orElseThrow(
                        () -> new NoSuchElementException(
                                "No implementation of PuzzleRepository found, check your classpath/modulepath."));
        final Presenter presenter = applicationContext.getBean(Presenter.class);
        return CrosswordService.create(dictionaryProviders, solvers, clueProviders, puzzleDecoders,
                                       puzzleEncoders, puzzleRepository, presenter);
    }

    /**
     * Loads all the implementations of the given service class using {@link ServiceLoader}.
     *
     * @param clazz the service provider class
     * @param <T>   the type of the service
     * @return all the implementations of the given service class
     */
    private static <T> Collection<T> load(final Class<T> clazz) {
        return ServiceLoader.load(clazz).stream().map(Supplier::get).toList();
    }

    /**
     * The {@link SolverService}.
     *
     * @return the solver service
     */
    @Bean
    public SolverService solverService() {
        return crosswordService().solverService();
    }

    /**
     * The {@link PuzzleService} bean.
     *
     * @return the puzzle service
     */
    @Bean
    public PuzzleService puzzleService() {
        return crosswordService().puzzleService();
    }

    /**
     * The {@link PuzzlePersistenceService}.
     *
     * @return puzzle persistence service
     */
    @Bean
    public PuzzlePersistenceService puzzlePersistenceService() {
        return puzzleService().persistence();
    }
}
