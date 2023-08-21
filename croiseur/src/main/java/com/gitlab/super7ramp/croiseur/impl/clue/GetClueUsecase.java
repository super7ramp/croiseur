/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.clue;

import com.gitlab.super7ramp.croiseur.api.clue.GetClueRequest;
import com.gitlab.super7ramp.croiseur.spi.clue.ClueProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.clue.CluePresenter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the 'get clue' usecase.
 */
final class GetClueUsecase {

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
    GetClueUsecase(final Collection<ClueProvider> clueProvidersArg,
                   final CluePresenter cluePresenterArg) {
        clueProviders = clueProvidersArg;
        cluePresenter = cluePresenterArg;
    }

    /**
     * Processes the "get clue" event.
     *
     * @param event the "get clue" event
     */
    void process(final GetClueRequest event) {
        final Optional<ClueProvider> selectedClueProvider = selectClueProvider(event);
        if (selectedClueProvider.isEmpty()) {
            cluePresenter.presentClueError(ClueErrorMessages.NO_CLUE_PROVIDER);
            return;
        }
        final Map<String, String> clues = getClues(selectedClueProvider.get(), event.words());
        if (clues.isEmpty()) {
            cluePresenter.presentClueError(ClueErrorMessages.NO_CLUE);
            return;
        }
        cluePresenter.presentClues(clues);
    }

    /**
     * Selects the appropriate clue provider given the request.
     *
     * @param event the 'give clue' request
     * @return the appropriate clue provider, if found, otherwise {@link Optional#empty()}
     */
    private Optional<ClueProvider> selectClueProvider(final GetClueRequest event) {
        final Optional<String> requestedClueProviderName = event.clueProvider();
        return clueProviders.stream()
                            .filter(clueProvider -> requestedClueProviderName.isEmpty() ||
                                                    clueProvider.name()
                                                                .equals(requestedClueProviderName.get()))
                            .findFirst();
    }

    /**
     * Calls the clue provider, handling the potential exceptions.
     *
     * @param clueProvider the clue provider
     * @param words        the words for which to get clues
     * @return the clues indexed by the defined words
     */
    private Map<String, String> getClues(final ClueProvider clueProvider,
                                         final List<String> words) {
        try {
            return clueProvider.define(words);
        } catch (final Exception e) {
            /*
             * Present exception message, even for runtime exceptions: Exception comes from only one
             * clue provider plugin, it should not stop the whole application or print a stacktrace.
             */
            final String error =
                    String.format(ClueErrorMessages.CLUE_PROVIDER_FAILED, clueProvider.name(),
                                  e.getMessage());
            cluePresenter.presentClueError(error);
            return Collections.emptyMap();
        }
    }
}
