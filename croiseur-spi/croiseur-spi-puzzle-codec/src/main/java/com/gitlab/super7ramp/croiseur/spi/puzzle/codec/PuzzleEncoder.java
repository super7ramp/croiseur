/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.puzzle.codec;

import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleCodecDetails;

import java.io.OutputStream;

/**
 * A puzzle encoder.
 */
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
     * @throws NullPointerException    if any of the parameters is {@code null}
     * @throws PuzzleEncodingException if encoder fails to read the given input for any other
     *                                 reason
     */
    void encode(final Puzzle puzzle, final OutputStream output) throws PuzzleEncodingException;
}
