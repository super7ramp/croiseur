/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.codec.xd.plugin;

import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.PuzzleCodecDetails;
import re.belv.croiseur.puzzle.codec.xd.model.XdCrossword;
import re.belv.croiseur.puzzle.codec.xd.writer.XdCrosswordWriter;
import re.belv.croiseur.spi.puzzle.codec.PuzzleEncoder;

import java.io.OutputStream;
import java.util.List;

/**
 * {@link PuzzleEncoder} implementation for the xd format.
 */
public final class XdEncoder implements PuzzleEncoder {

    /** The actual encoder. */
    private final XdCrosswordWriter xd;

    /**
     * Constructs an instance.
     */
    public XdEncoder() {
        xd = new XdCrosswordWriter();
    }

    @Override
    public PuzzleCodecDetails details() {
        return new PuzzleCodecDetails("xd", "xd format encoder", List.of("*.xd"));
    }

    @Override
    public void encode(final Puzzle puzzle, final OutputStream output) {
        final XdCrossword xdCrossword = PuzzleConverter.toXd(puzzle);
        xd.write(xdCrossword, output);
    }
}
