/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.clue;

import com.gitlab.super7ramp.croiseur.api.clue.ClueService;
import com.gitlab.super7ramp.croiseur.api.clue.GetClueRequest;
import com.gitlab.super7ramp.croiseur.spi.presenter.clue.CluePresenter;
import com.gitlab.super7ramp.croiseur.tests.context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Steps pertaining to the {@link ClueService}.
 */
public final class ClueSteps {

    /** The clue service. */
    private final ClueService clueService;

    /** The mocked presenter. */
    private final CluePresenter presenterMock;

    /**
     * Constructs an instance.
     *
     * @param testContext the test context
     */
    public ClueSteps(final TestContext testContext) {
        clueService = testContext.clueService();
        presenterMock = testContext.presenterMock();
    }

    @When("user requests to list the available clue providers")
    public void whenListProviders() {
        clueService.listProviders();
    }

    @When("user requests clues for the following words:")
    public void whenGetClues(final List<String> words) {
        final GetClueRequest request = new GetClueRequest() {

            @Override
            public Optional<String> clueProvider() {
                return Optional.empty();
            }

            @Override
            public List<String> words() {
                return words;
            }
        };
        clueService.getClues(request);
    }

    @Then("the application presents the clue service error {string}")
    public void thenClueError(final String error) {
        verify(presenterMock).presentClueError(eq(error));
    }

}
