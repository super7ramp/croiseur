/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.codec.xd.plugin;

import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdCrossword;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.reader.XdCrosswordReader;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.reader.XdReadException;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleDecoder;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleDecodingException;

import java.io.InputStream;

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
    public String name() {
        return "xd";
    }

    @Override
    public String description() {
        return "xd format decoder";
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
