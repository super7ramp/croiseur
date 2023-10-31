/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.tests.solver;

import re.belv.croiseur.api.dictionary.DictionaryIdentifier;
import re.belv.croiseur.api.solver.SolveRequest;
import re.belv.croiseur.api.solver.SolverService;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.spi.presenter.solver.SolverDescription;
import re.belv.croiseur.spi.presenter.solver.SolverPresenter;
import re.belv.croiseur.tests.context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static re.belv.croiseur.tests.solver.SolverMatchers.impossible;
import static re.belv.croiseur.tests.solver.SolverMatchers.success;
import static org.mockito.Mockito.verify;

/**
 * Steps pertaining to the solver service.
 */
public final class SolverSteps {

    /** Non-capturing regex for " and". */
    private static final String OPTIONALLY_AND = "(?: and)?";

    /** Regex for solver declaration. Captures the solver name. */
    private static final String OPTIONALLY_WITH_SOLVER = "(?: with \"([^\"]+)\" solver)?";

    /** Regex for dictionary declaration. Captures the dictionary name and its provider. */
    private static final String AND_OPTIONALLY_WITH_DICTIONARY =
            OPTIONALLY_AND + "(?: with \"([^\"]+)\"(?: dictionary)? provided by \"([^\"]+)\")?";

    /** Regex for dictionary shuffle randomness source definition. Captures the randomness seed. */
    private static final String AND_OPTIONALLY_WITH_RANDOMNESS =
            OPTIONALLY_AND + "(?: with dictionary shuffled using a seed of (\\d+))?";

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

    @When("^user requests to solve and save the following grid" +
          OPTIONALLY_WITH_SOLVER +
          AND_OPTIONALLY_WITH_DICTIONARY + AND_OPTIONALLY_WITH_RANDOMNESS + ":$")
    public void whenSolveAndSave(final String solver, final String dictionary,
                                 final String dictionaryProvider, final Long randomSeed,
                                 final PuzzleGrid puzzleGrid) {
        callSolver(solver, dictionary, dictionaryProvider, randomSeed, puzzleGrid,
                   false /* do not get clues */, true /* save puzzle */);
    }

    @When("^user requests to solve and get clues for the following grid" + OPTIONALLY_WITH_SOLVER +
          AND_OPTIONALLY_WITH_DICTIONARY + AND_OPTIONALLY_WITH_RANDOMNESS + ":$")
    public void whenSolveAndGetClues(final String solver, final String dictionary,
                                     final String dictionaryProvider, final Long randomSeed,
                                     final PuzzleGrid puzzleGrid) {
        callSolver(solver, dictionary, dictionaryProvider, randomSeed, puzzleGrid,
                   true /* get clues */, false /* do not save puzzle */);
    }

    @When("^user requests to solve the following grid" + OPTIONALLY_WITH_SOLVER +
          AND_OPTIONALLY_WITH_DICTIONARY + AND_OPTIONALLY_WITH_RANDOMNESS + ":$")
    public void whenSolve(final String solver, final String dictionary,
                          final String dictionaryProvider, final Long randomSeed,
                          final PuzzleGrid puzzleGrid) {
        callSolver(solver, dictionary, dictionaryProvider, randomSeed, puzzleGrid,
                   false /* do not get clues */, false /* do not save puzzle */);
    }

    /**
     * Calls solver service.
     *
     * @param solver             the solver name, if any, otherwise {@code null}
     * @param dictionary         the dictionary name, if any, otherwise {@code null}
     * @param dictionaryProvider the dictionary provider, if any, otherwise {@code null}
     * @param randomSeed         the random seed, if any, otherwise {@code null}
     * @param puzzleGrid         the grid
     * @param withClues          whether to get clues for the solution, if any found
     * @param savePuzzle         whether to publish the given grid as a puzzle in repository
     */
    private void callSolver(final String solver, final String dictionary,
                            final String dictionaryProvider, final Long randomSeed,
                            final PuzzleGrid puzzleGrid, final boolean withClues,
                            final boolean savePuzzle) {
        final SolveRequest solveRequest = new SolveRequest() {

            @Override
            public PuzzleGrid grid() {
                return puzzleGrid;
            }

            @Override
            public boolean savePuzzle() {
                return savePuzzle;
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
            public Optional<Random> dictionariesShuffle() {
                return Optional.ofNullable(randomSeed).map(Random::new);
            }

            @Override
            public Optional<String> solver() {
                return Optional.ofNullable(solver);
            }

            @Override
            public SolverProgressNotificationMethod progress() {
                return SolverProgressNotificationMethod.NONE;
            }

            @Override
            public boolean withClues() {
                return withClues;
            }
        };
        solverService.solve(solveRequest);
    }

    @Then("the application presents the following solvers:")
    public void thenPresentSolvers(final List<SolverDescription> solverDescriptions) {
        verify(presenterMock).presentAvailableSolvers(solverDescriptions);
    }

    @Then("the application presents the solver error {string}")
    public void thenSolverError(final String error) {
        verify(presenterMock).presentSolverError(error);
    }

    @Then("the application presents the following successful solver result:")
    public void thenPresentSolverResultSuccess(final PuzzleGrid solution) {
        verify(presenterMock).presentSolverResult(success(solution.filled()));
    }

    @Then("the application presents the grid as impossible to solve")
    public void thenPresentSolverResultImpossible() {
        verify(presenterMock).presentSolverResult(impossible());
    }
}
