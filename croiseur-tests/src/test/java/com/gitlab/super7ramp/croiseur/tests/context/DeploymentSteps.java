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
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
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
public class DeploymentSteps {

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
    @BeforeAll
    public static void setEnglishLocale() {
        Locale.setDefault(Locale.ENGLISH);
    }

    /**
     * Instantiates croiseur, simulating the deployment of an application using the library,
     * without dictionary provider.
     */
    @Given("an application deployed without dictionary provider")
    public void givenDeployedWithoutDictionaryProvider() {
        final Collection<DictionaryProvider> dictionaryProviders = Collections.emptyList();
        final Collection<CrosswordSolver> solvers =
                ServiceLoader.load(CrosswordSolver.class)
                             .stream()
                             .map(Supplier::get)
                             .toList();
        final Presenter presenterMock = mock(Presenter.class);
        deploy(dictionaryProviders, solvers, presenterMock);
    }

    /**
     * Instantiates croiseur, simulating the deployment of an application using the library.
     */
    @Before("not @no-auto-deploy")
    public void deploy() {
        final Collection<DictionaryProvider> dictionaryProviders =
                ServiceLoader.load(DictionaryProvider.class).stream()
                             .map(Supplier::get)
                             .toList();
        final Collection<CrosswordSolver> solvers =
                ServiceLoader.load(CrosswordSolver.class)
                             .stream()
                             .map(Supplier::get)
                             .toList();
        final Presenter presenterMock = mock(Presenter.class);
        deploy(dictionaryProviders, solvers, presenterMock);
    }

    /**
     * Cleans reference to the croiseur library once scenario has tested it.
     */
    @After
    public void undeploy() {
        testContext.undeploy();
    }

    private void deploy(final Collection<DictionaryProvider> dictionaryProviders,
                        final Collection<CrosswordSolver> solvers, final Presenter presenter) {
        final CrosswordService crosswordService = CrosswordService.create(dictionaryProviders,
                solvers, presenter);
        testContext.deploy(crosswordService, presenter);
    }
}
