/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.puzzle;

import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleCodecDetails;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleDecoder;

import java.util.Collection;
import java.util.List;

/**
 * The 'list puzzle decoders' usecase.
 */
final class ListPuzzleDecodersUsecase {

    /** The available puzzle decoders. */
    private final List<PuzzleCodecDetails> decoders;

    /** The puzzle presenter. */
    private final PuzzlePresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param decodersArg the decoders
     * @param presenterArg the presenter
     */
    ListPuzzleDecodersUsecase(final Collection<PuzzleDecoder> decodersArg, final PuzzlePresenter presenterArg) {
        decoders = decodersArg.stream().map(PuzzleDecoder::details).toList();
        presenter = presenterArg;
    }

    /**
     * Processes the 'list puzzle decoders' event.
     */
    void process() {
        presenter.presentPuzzleDecoders(decoders);
    }
}
