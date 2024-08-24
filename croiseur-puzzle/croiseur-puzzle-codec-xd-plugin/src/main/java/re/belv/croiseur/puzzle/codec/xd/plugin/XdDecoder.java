/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.codec.xd.plugin;

import java.io.InputStream;
import java.util.List;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.PuzzleCodecDetails;
import re.belv.croiseur.puzzle.codec.xd.model.XdCrossword;
import re.belv.croiseur.puzzle.codec.xd.reader.XdCrosswordReader;
import re.belv.croiseur.puzzle.codec.xd.reader.XdReadException;
import re.belv.croiseur.spi.puzzle.codec.PuzzleDecoder;
import re.belv.croiseur.spi.puzzle.codec.PuzzleDecodingException;

/**
 * {@link PuzzleDecoder} implementation for the xd format.
 */
public final class XdDecoder implements PuzzleDecoder {

    /** The actual decoder. */
    private final XdCrosswordReader xd;

    /**
     * Constructs an instance.
     */
    public XdDecoder() {
        xd = new XdCrosswordReader();
    }

    @Override
    public PuzzleCodecDetails details() {
        return new PuzzleCodecDetails("xd", "xd format decoder", List.of("*.xd"));
    }

    @Override
    public Puzzle decode(final InputStream input) throws PuzzleDecodingException {
        try {
            final XdCrossword xdCrossword = xd.read(input);
            return PuzzleConverter.toDomain(xdCrossword);
        } catch (final XdReadException e) {
            throw new PuzzleDecodingException(e);
        }
    }
}
