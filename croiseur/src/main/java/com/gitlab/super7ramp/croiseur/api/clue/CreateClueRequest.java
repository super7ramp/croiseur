/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.clue;

import java.util.List;
import java.util.Optional;

/**
 * A request to create clues.
 */
public interface CreateClueRequest {

    /**
     * The name of the clue provider to use, if any.
     * <p>
     * If not present, the default clue provider will be used. The default clue provider is the
     * first one detected.
     *
     * @return the clue provider to use
     */
    Optional<String> clueProvider();

    /**
     * The words for which to create clues.
     *
     * @return the words for which to create clues
     */
    List<String> words();

}
