/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.context;

import com.gitlab.super7ramp.croiseur.api.CrosswordService;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleDecoder;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleEncoder;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.PuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.ServiceLoader;
import java.util.function.Supplier;

import static org.mockito.Mockito.mock;

/**
 * Technical steps related to deploying the test environment.
 */
public final class DeploymentSteps {

    /** Locale before deployment. */
    private static final Locale ORIGIN_LOCALE = Locale.getDefault();

    /** The test context. */
    private final TestContext testContext;

    /**
     * Constructs an instance.
     *
     * @param testContextArg the test context
     */
    public DeploymentSteps(final TestContext testContextArg) {
        testContext = testContextArg;
    }

    /**
     * Sets default locale to {@link Locale#ENGLISH} in order to have results independent of
     * system's locale.
     */
    @Before(order = 1 /* run before deployment step */)
    public void setEnglishLocale() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Given("system locale is {locale}")
    public void givenSystemLocale(final Locale locale) {
        Locale.setDefault(locale);
    }

    /**
     * Instantiates croiseur, simulating the deployment of an application using the library, without
     * dictionary provider.
     */
    @Given("an application deployed without dictionary provider")
    public void givenDeployedWithoutDictionaryProvider() {
        final Collection<DictionaryProvider> dictionaryProviders = Collections.emptyList();
        final Collection<CrosswordSolver> solvers = load(CrosswordSolver.class);
        final Collection<PuzzleDecoder> puzzleDecoders = load(PuzzleDecoder.class);
        final Collection<PuzzleEncoder> puzzleEncoders = load(PuzzleEncoder.class);
        final PuzzleRepositorySpy puzzleRepositorySpy = loadRepositorySpy();
        final Presenter presenterMock = mock(Presenter.class);
        deploy(dictionaryProviders, solvers, puzzleDecoders, puzzleEncoders, puzzleRepositorySpy,
               presenterMock);
    }

    @Given("an application deployed without solver")
    public void givenDeployedWithoutSolver() {
        final Collection<DictionaryProvider> dictionaryProviders = load(DictionaryProvider.class);
        final Collection<CrosswordSolver> solvers = Collections.emptyList();
        final Collection<PuzzleDecoder> puzzleDecoders = load(PuzzleDecoder.class);
        final Collection<PuzzleEncoder> puzzleEncoders = load(PuzzleEncoder.class);
        final PuzzleRepositorySpy puzzleRepositorySpy = loadRepositorySpy();
        final Presenter presenterMock = mock(Presenter.class);
        deploy(dictionaryProviders, solvers, puzzleDecoders, puzzleEncoders, puzzleRepositorySpy,
               presenterMock);
    }

    /**
     * Instantiates croiseur, simulating the deployment of an application using the library.
     */
    @Before("not @no-auto-deploy")
    public void deploy() {
        final Collection<DictionaryProvider> dictionaryProviders = load(DictionaryProvider.class);
        final Collection<CrosswordSolver> solvers = load(CrosswordSolver.class);
        final Collection<PuzzleDecoder> puzzleDecoders = load(PuzzleDecoder.class);
        final Collection<PuzzleEncoder> puzzleEncoders = load(PuzzleEncoder.class);
        final PuzzleRepositorySpy puzzleRepositorySpy = loadRepositorySpy();
        final Presenter presenterMock = mock(Presenter.class);
        deploy(dictionaryProviders, solvers, puzzleDecoders, puzzleEncoders, puzzleRepositorySpy,
               presenterMock);
    }

    /**
     * Loads all the implementations of the given service class.
     *
     * @param clazz the service provider class
     * @param <T>   the type of the service
     * @return all the implementations of the given service class
     */
    private static <T> Collection<T> load(final Class<T> clazz) {
        return ServiceLoader.load(clazz)
                            .stream()
                            .map(Supplier::get)
                            .toList();
    }

    /**
     * Loads the puzzle repository spy.
     *
     * @return the puzzle repository spy
     */
    private static PuzzleRepositorySpy loadRepositorySpy() {
        final PuzzleRepository puzzleRepository =
                load(PuzzleRepository.class).stream().findFirst().orElseThrow(
                        () -> new IllegalStateException(
                                "No puzzle repository implementation found, check your tests setup"));
        return new PuzzleRepositorySpy(puzzleRepository);
    }

    private void deploy(final Collection<DictionaryProvider> dictionaryProviders,
                        final Collection<CrosswordSolver> solvers,
                        final Collection<PuzzleDecoder> puzzleDecoders,
                        final Collection<PuzzleEncoder> puzzleEncoders,
                        final PuzzleRepositorySpy puzzleRepository, final Presenter presenter) {
        final CrosswordService crosswordService = CrosswordService.create(dictionaryProviders,
                                                                          solvers, puzzleDecoders,
                                                                          puzzleEncoders,
                                                                          puzzleRepository,
                                                                          presenter);
        testContext.deploy(crosswordService, puzzleRepository, presenter);
    }

    /**
     * Verifies that all interactions have been verified and cleans reference to the croiseur
     * library.
     */
    @After
    public void after() {
        testContext.verifyNoMoreInteractions();
        testContext.undeploy();
    }

    /**
     * Restores origin locale.
     */
    @AfterAll
    public static void afterAll() {
        Locale.setDefault(ORIGIN_LOCALE);
    }
}
