/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.puzzle.exporter;

import java.util.Collection;
import java.util.List;
import re.belv.croiseur.common.puzzle.PuzzleCodecDetails;
import re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import re.belv.croiseur.spi.puzzle.codec.PuzzleEncoder;

/** The 'list puzzle encoders' usecase. */
final class ListPuzzleEncodersUsecase {

    /** The available puzzle decoders. */
    private final List<PuzzleCodecDetails> encoders;

    /** The puzzle presenter. */
    private final PuzzlePresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param encodersArg the encoders
     * @param presenterArg the presenter
     */
    ListPuzzleEncodersUsecase(final Collection<PuzzleEncoder> encodersArg, final PuzzlePresenter presenterArg) {
        encoders = encodersArg.stream().map(PuzzleEncoder::details).toList();
        presenter = presenterArg;
    }

    /** Processes the 'list puzzle encoders' event. */
    void process() {
        presenter.presentPuzzleEncoders(encoders);
    }
}
