/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.spi.puzzle.codec;

import java.io.OutputStream;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.PuzzleCodecDetails;

/** A puzzle encoder. */
public interface PuzzleEncoder {

    /**
     * Details about the encoder.
     *
     * @return details about the encoder
     */
    PuzzleCodecDetails details();

    /**
     * Encodes a puzzle to given output stream.
     *
     * @param puzzle the puzzle to encode
     * @param output where to encode the puzzle
     * @throws NullPointerException if any of the parameters is {@code null}
     * @throws PuzzleEncodingException if encoder fails to read the given input for any other reason
     */
    void encode(final Puzzle puzzle, final OutputStream output) throws PuzzleEncodingException;
}
