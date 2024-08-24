/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.puzzle.importer;

import java.util.Collection;
import java.util.List;
import re.belv.croiseur.common.puzzle.PuzzleCodecDetails;
import re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import re.belv.croiseur.spi.puzzle.codec.PuzzleDecoder;

/** The 'list puzzle decoders' usecase. */
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

    /** Processes the 'list puzzle decoders' event. */
    void process() {
        presenter.presentPuzzleDecoders(decoders);
    }
}
