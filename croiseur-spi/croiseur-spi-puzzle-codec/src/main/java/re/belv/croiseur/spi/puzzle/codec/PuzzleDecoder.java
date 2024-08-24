/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.spi.puzzle.codec;

import java.io.InputStream;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.PuzzleCodecDetails;

/** A puzzle decoder. */
public interface PuzzleDecoder {

    /**
     * Details about the decoder.
     *
     * @return details about the decoder
     */
    PuzzleCodecDetails details();

    /**
     * Decodes an input as a puzzle.
     *
     * @param input the input to decode
     * @return the decoded {@link Puzzle}
     * @throws NullPointerException if the given input is {@code null}
     * @throws PuzzleDecodingException if decoder fails to read the given input for any other reason
     */
    Puzzle decode(final InputStream input) throws PuzzleDecodingException;
}
