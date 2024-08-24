/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.clue;

import java.util.Collection;
import java.util.Map;
import re.belv.croiseur.api.clue.GetClueRequest;
import re.belv.croiseur.impl.clue.shared.SafeClueProvider;
import re.belv.croiseur.spi.clue.ClueProvider;
import re.belv.croiseur.spi.presenter.clue.CluePresenter;

/** Implementation of the 'get clue' usecase. */
final class GetClueUsecase {

    /** The clue retriever. */
    private final SafeClueProvider clueRetriever;

    /** The clue presenter. */
    private final CluePresenter cluePresenter;

    /**
     * Constructs an instance.
     *
     * @param clueProvidersArg the clue providers
     * @param cluePresenterArg the clue presenter
     */
    GetClueUsecase(final Collection<ClueProvider> clueProvidersArg, final CluePresenter cluePresenterArg) {
        clueRetriever = new SafeClueProvider(clueProvidersArg, cluePresenterArg);
        cluePresenter = cluePresenterArg;
    }

    /**
     * Processes the "get clue" event.
     *
     * @param event the "get clue" event
     */
    void process(final GetClueRequest event) {
        final Map<String, String> clues =
                clueRetriever.getClues(event.clueProvider().orElse(null), event.words());
        if (!clues.isEmpty()) {
            cluePresenter.presentClues(clues);
        }
    }
}
