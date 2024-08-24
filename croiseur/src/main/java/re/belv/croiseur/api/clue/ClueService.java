/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.api.clue;

import java.util.List;
import java.util.Map;

/** Clue service: Allows to get clues, definitions of words. */
public interface ClueService {

    /**
     * Lists the available clue providers.
     *
     * @see re.belv.croiseur.spi.presenter.clue.CluePresenter#presentClueProviders(List)
     *     CluePresenter#presentClueProviders
     */
    void listProviders();

    /**
     * Gets clues from a clue provider.
     *
     * @param getClueRequest the clue request
     * @see re.belv.croiseur.spi.presenter.clue.CluePresenter#presentClues(Map) CluePresenter#presentClues
     */
    void getClues(final GetClueRequest getClueRequest);
}
