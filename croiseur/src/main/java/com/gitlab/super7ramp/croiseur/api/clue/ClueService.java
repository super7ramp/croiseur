/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.clue;

/**
 * Clue service: Allows to get clues, definitions of words.
 */
public interface ClueService {

    /**
     * Lists the available clue providers.
     */
    void listProviders();

    /**
     * Gets clues from a clue provider.
     *
     * @param getClueRequest the clue request
     */
    void getClues(final GetClueRequest getClueRequest);
}
