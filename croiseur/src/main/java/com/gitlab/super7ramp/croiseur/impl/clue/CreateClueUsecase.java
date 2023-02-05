/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.clue;

import com.gitlab.super7ramp.croiseur.api.clue.CreateClueRequest;
import com.gitlab.super7ramp.croiseur.spi.clue.ClueProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.clue.CluePresenter;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the 'create clue' usecase.
 */
final class CreateClueUsecase {

    /** The clue providers. */
    private final Collection<ClueProvider> clueProviders;

    /** The clue presenter. */
    private final CluePresenter cluePresenter;

    /**
     * Constructs an instance.
     *
     * @param clueProvidersArg the clue providers
     * @param cluePresenterArg the clue presenter
     */
    CreateClueUsecase(final Collection<ClueProvider> clueProvidersArg,
                      final CluePresenter cluePresenterArg) {
        clueProviders = clueProvidersArg;
        cluePresenter = cluePresenterArg;
    }

    /**
     * Processes the "give clue" event.
     *
     * @param event the "give clue" event
     */
    void process(final CreateClueRequest event) {
        final Optional<ClueProvider> selectedClueProvider = selectClueProvider(event);
        if (selectedClueProvider.isPresent()) {
            final Map<String, String> clues = selectedClueProvider.get().define(event.words());
            if (clues.isEmpty()) {
                cluePresenter.presentClueError("Couldn't find any acceptable clues");
            } else {
                cluePresenter.presentClues(clues);
            }
        } else {
            cluePresenter.presentClueError(ClueErrorMessages.NO_CLUE_PROVIDER_ERROR_MESSAGE);
        }
    }

    /**
     * Selects the appropriate clue provider given the request.
     *
     * @param event the 'give clue' request
     * @return the appropriate clue provider, if found, otherwise {@link Optional#empty()}
     */
    private Optional<ClueProvider> selectClueProvider(final CreateClueRequest event) {
        final Optional<String> requestedClueProviderName = event.clueProvider();
        return clueProviders.stream()
                            .filter(clueProvider -> requestedClueProviderName.isEmpty() ||
                                    clueProvider.name().equals(requestedClueProviderName.get()))
                            .findFirst();
    }
}
