/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.context;

import com.gitlab.super7ramp.croiseur.api.CrosswordService;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
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
    @Before
    public void setEnglishLocale() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Given("system locale is {locale}")
    public void givenSystemLocale(final Locale locale) {
        Locale.setDefault(locale);
    }

    /**
     * Instantiates croiseur, simulating the deployment of an application using the library,
     * without dictionary provider.
     */
    @Given("an application deployed without dictionary provider")
    public void givenDeployedWithoutDictionaryProvider() {
        final Collection<DictionaryProvider> dictionaryProviders = Collections.emptyList();
        final Collection<CrosswordSolver> solvers = load(CrosswordSolver.class);
        final Presenter presenterMock = mock(Presenter.class);
        deploy(dictionaryProviders, solvers, presenterMock);
    }

    @Given("an application deployed without solver")
    public void givenDeployedWithoutSolver() {
        final Collection<DictionaryProvider> dictionaryProviders = load(DictionaryProvider.class);
        final Collection<CrosswordSolver> solvers = Collections.emptyList();
        final Presenter presenterMock = mock(Presenter.class);
        deploy(dictionaryProviders, solvers, presenterMock);
    }

    /**
     * Instantiates croiseur, simulating the deployment of an application using the library.
     */
    @Before("not @no-auto-deploy")
    public void deploy() {
        final Collection<DictionaryProvider> dictionaryProviders = load(DictionaryProvider.class);
        final Collection<CrosswordSolver> solvers = load(CrosswordSolver.class);
        final Presenter presenterMock = mock(Presenter.class);
        deploy(dictionaryProviders, solvers, presenterMock);
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

    private void deploy(final Collection<DictionaryProvider> dictionaryProviders,
                        final Collection<CrosswordSolver> solvers, final Presenter presenter) {
        final CrosswordService crosswordService = CrosswordService.create(dictionaryProviders,
                                                                          solvers, presenter);
        testContext.deploy(crosswordService, presenter);
    }

    /**
     * Cleans reference to the croiseur library once scenario has tested it.
     */
    @After
    public void undeploy() {
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
