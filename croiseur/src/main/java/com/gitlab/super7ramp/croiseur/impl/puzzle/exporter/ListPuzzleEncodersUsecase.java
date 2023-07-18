/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.puzzle.exporter;

import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleCodecDetails;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleEncoder;

import java.util.Collection;
import java.util.List;

/**
 * The 'list puzzle encoders' usecase.
 */
final class ListPuzzleEncodersUsecase {

    /** The available puzzle decoders. */
    private final List<PuzzleCodecDetails> encoders;

    /** The puzzle presenter. */
    private final PuzzlePresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param encodersArg  the encoders
     * @param presenterArg the presenter
     */
    ListPuzzleEncodersUsecase(final Collection<PuzzleEncoder> encodersArg,
                              final PuzzlePresenter presenterArg) {
        encoders = encodersArg.stream().map(PuzzleEncoder::details).toList();
        presenter = presenterArg;
    }

    /**
     * Processes the 'list puzzle encoders' event.
     */
    void process() {
        presenter.presentPuzzleEncoders(encoders);
    }
}