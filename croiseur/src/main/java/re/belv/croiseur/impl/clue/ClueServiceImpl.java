/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.clue;

import re.belv.croiseur.api.clue.ClueService;
import re.belv.croiseur.api.clue.GetClueRequest;
import re.belv.croiseur.spi.clue.ClueProvider;
import re.belv.croiseur.spi.presenter.clue.CluePresenter;

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
