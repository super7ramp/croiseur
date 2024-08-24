/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.clue;

import java.util.Collection;
import java.util.List;
import re.belv.croiseur.impl.clue.error.ClueErrorMessages;
import re.belv.croiseur.spi.clue.ClueProvider;
import re.belv.croiseur.spi.presenter.clue.CluePresenter;
import re.belv.croiseur.spi.presenter.clue.ClueProviderDescription;

/**
 * Implementation of the 'list clue providers' usecase.
 */
final class ListClueProvidersUsecase {

    /** The clue providers. */
    private final Collection<ClueProvider> clueProviders;

    /** The clue-related presenter. */
    private final CluePresenter cluePresenter;

    /**
     * Constructs an instance.
     *
     * @param clueProvidersArg the clue providers
     * @param cluePresenterArg the clue-related presenter
     */
    ListClueProvidersUsecase(final Collection<ClueProvider> clueProvidersArg, final CluePresenter cluePresenterArg) {
        clueProviders = clueProvidersArg;
        cluePresenter = cluePresenterArg;
    }

    /**
     * Processes the 'list providers' event.
     */
    void process() {
        final List<ClueProviderDescription> descriptions = clueProviders.stream()
                .map(clueProvider -> new ClueProviderDescription(clueProvider.name(), clueProvider.description()))
                .toList();
        if (descriptions.isEmpty()) {
            cluePresenter.presentClueError(ClueErrorMessages.NO_CLUE_PROVIDER);
        } else {
            cluePresenter.presentClueProviders(descriptions);
        }
    }
}
