/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.clue;

import com.gitlab.super7ramp.croiseur.spi.clue.ClueProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.clue.CluePresenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.clue.ClueProviderDescription;

import java.util.Collection;
import java.util.List;

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
    ListClueProvidersUsecase(final Collection<ClueProvider> clueProvidersArg,
                             final CluePresenter cluePresenterArg) {
        clueProviders = clueProvidersArg;
        cluePresenter = cluePresenterArg;
    }

    /**
     * Processes the 'list providers' event.
     */
    void process() {
        final List<ClueProviderDescription> descriptions =
                clueProviders.stream()
                             .map(clueProvider -> new ClueProviderDescription(clueProvider.name(),
                                     clueProvider.description()))
                             .toList();
        if (descriptions.isEmpty()) {
            cluePresenter.presentClueError(ClueErrorMessages.NO_CLUE_PROVIDER);
        } else {
            cluePresenter.presentClueProviders(descriptions);
        }
    }
}
