/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.spi.presenter.clue;

import java.util.List;
import java.util.Map;

/** Clue presentation service. */
public interface CluePresenter {

    /**
     * Presents an error regarding the clue service.
     *
     * @param error the error
     */
    // TODO error should be a dedicated type
    void presentClueError(final String error);

    /**
     * Presents the available clue providers.
     *
     * @param clueProviderDescriptions the clue provider descriptions
     */
    void presentClueProviders(final List<ClueProviderDescription> clueProviderDescriptions);

    /**
     * Presents clues.
     *
     * @param clues the clues, indexed by the defined words; Map may be incomplete
     */
    void presentClues(final Map<String, String> clues);
}
