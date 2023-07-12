/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.puzzle.codec;

import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;

import java.io.InputStream;

/**
 * A puzzle decoder.
 */
public interface PuzzleDecoder {

    /**
     * The decoder name.
     *
     * @return the decoder name
     */
    String name();

    /**
     * The decoder description.
     *
     * @return the decoder description
     */
    String description();

    /**
     * Decodes an input as a puzzle.
     *
     * @param input the input to decode
     * @return the decoded {@link Puzzle}
     * @throws NullPointerException    if the given input is {@code null}
     * @throws PuzzleDecodingException if decoder fails to read the given input for any other
     *                                 reason
     */
    Puzzle decode(final InputStream input) throws PuzzleDecodingException;
}
