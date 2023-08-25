/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.clue;

import com.gitlab.super7ramp.croiseur.api.clue.GetClueRequest;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordGridViewModel;

import java.util.Optional;
import java.util.Set;

/**
 * Implementation of {@link GetClueRequest}.
 */
final class GetClueRequestImpl implements GetClueRequest {

    /** The word for which to get a clue. */
    private final String word;

    /**
     * Constructs an instance.
     *
     * @param crosswordGridViewModel the crossword grid view model
     */
    GetClueRequestImpl(final CrosswordGridViewModel crosswordGridViewModel) {
        word = crosswordGridViewModel.currentSlotContent();
        if (word.isEmpty() || word.contains(".")) {
            throw new IllegalArgumentException(
                    "Current slot is not filled, cannot get a clue for it");
        }
    }

    @Override
    public Optional<String> clueProvider() {
        return Optional.empty();
    }

    @Override
    public Set<String> words() {
        return Set.of(word);
    }
}
