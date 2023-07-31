/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.clue;

import com.gitlab.super7ramp.croiseur.api.clue.ClueService;
import com.gitlab.super7ramp.croiseur.api.clue.GetClueRequest;
import com.gitlab.super7ramp.croiseur.spi.clue.ClueProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.clue.CluePresenter;

import java.util.Collection;

/**
 * Implementation of {@link ClueService}.
 */
public final class ClueServiceImpl implements ClueService {

    /** The 'list clue providers' use case. */
    private final ListClueProvidersUsecase listClueProvidersUseCase;

    /** The 'get clue' use case. */
    private final GetClueUsecase getClueUsecase;

    /**
     * Constructs an instance.
     *
     * @param clueProviders the clue providers
     * @param cluePresenter the clue presenter
     */
    public ClueServiceImpl(final Collection<ClueProvider> clueProviders,
                           final CluePresenter cluePresenter) {
        listClueProvidersUseCase = new ListClueProvidersUsecase(clueProviders, cluePresenter);
        getClueUsecase = new GetClueUsecase(clueProviders, cluePresenter);
    }

    @Override
    public void listProviders() {
        listClueProvidersUseCase.process();
    }

    @Override
    public void getClues(final GetClueRequest getClueRequest) {
        getClueUsecase.process(getClueRequest);
    }
}
