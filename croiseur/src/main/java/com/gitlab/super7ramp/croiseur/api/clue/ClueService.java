/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.clue;

/**
 * Clue service: Allows to create clues, definitions of words.
 */
public interface ClueService {

    /**
     * Lists the available clue providers.
     */
    void listProviders();

    /**
     * Creates clues.
     *
     * @param createClueRequest the clue request
     */
    void createClues(final CreateClueRequest createClueRequest);
}
