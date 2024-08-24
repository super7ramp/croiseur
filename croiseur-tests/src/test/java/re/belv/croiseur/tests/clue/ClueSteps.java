/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.tests.clue;

import static org.mockito.Mockito.verify;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import re.belv.croiseur.api.clue.ClueService;
import re.belv.croiseur.api.clue.GetClueRequest;
import re.belv.croiseur.spi.presenter.clue.CluePresenter;
import re.belv.croiseur.tests.context.TestContext;

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
            public Set<String> words() {
                return new HashSet<>(words);
            }
        };
        clueService.getClues(request);
    }

    @Then("the application presents the clue service error {string}")
    public void thenClueError(final String error) {
        verify(presenterMock).presentClueError(error);
    }
}
