/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.solver;

import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.croiseur.api.solver.SolveRequest;
import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import com.gitlab.super7ramp.croiseur.common.PuzzleDefinition;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverDescription;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter;
import com.gitlab.super7ramp.croiseur.tests.context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.gitlab.super7ramp.croiseur.tests.solver.SolverMatchers.impossible;
import static com.gitlab.super7ramp.croiseur.tests.solver.SolverMatchers.success;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Steps pertaining to the solver service.
 */
public final class SolverSteps {

    /** The dictionary service. */
    private final SolverService solverService;

    /** The mocked presenter. */
    private final SolverPresenter presenterMock;

    /**
     * Constructs an instance.
     *
     * @param testContext the test context
     */
    public SolverSteps(final TestContext testContext) {
        solverService = testContext.solverService();
        presenterMock = testContext.presenterMock();
    }

    @When("user requests to list the available solvers")
    public void whenListSolvers() {
        solverService.listSolvers();
    }

    @When("^user requests to solve the following grid(?: with \"([^\"]+)\" solver)?(?: and)?(?: " +
            "with \"([^\"]+)\"(?: dictionary)? provided by \"([^\"]+)\")?:$")
    public void whenSolveWith(final String solver,
                              final String dictionary,
                              final String dictionaryProvider,
                              final PuzzleDefinition puzzleDefinition) {
        final SolveRequest solveRequest = new SolveRequest() {

            @Override
            public PuzzleDefinition puzzle() {
                return puzzleDefinition;
            }

            @Override
            public Collection<DictionaryIdentifier> dictionaries() {
                final Collection<DictionaryIdentifier> dictionaryIdentifiers;
                if (dictionary != null) {
                    final DictionaryIdentifier dictionaryIdentifier =
                            new DictionaryIdentifier(dictionaryProvider, dictionary);
                    dictionaryIdentifiers = Collections.singleton(dictionaryIdentifier);
                } else {
                    dictionaryIdentifiers = Collections.emptySet();
                }
                return dictionaryIdentifiers;
            }

            @Override
            public Optional<String> solver() {
                return Optional.ofNullable(solver);
            }

            @Override
            public SolverProgressNotificationMethod progress() {
                return SolverProgressNotificationMethod.NONE;
            }
        };
        solverService.solve(solveRequest);
    }

    @Then("the application presents the following solvers:")
    public void thenPresentSolvers(final List<SolverDescription> solverDescriptions) {
        verify(presenterMock).presentAvailableSolvers(eq(solverDescriptions));
    }

    @Then("the application presents the solver error {string}")
    public void thenSolverError(final String error) {
        verify(presenterMock).presentSolverError(eq(error));
    }

    @Then("the application presents the following successful solver result:")
    public void thenPresentSolverResultSuccess(final PuzzleDefinition solution) {
        verify(presenterMock).presentResult(success(solution.filled()));
    }

    @Then("the application presents the grid as impossible to solve")
    public void thenPresentSolverResultImpossible() {
        verify(presenterMock).presentResult(impossible());
    }
}
