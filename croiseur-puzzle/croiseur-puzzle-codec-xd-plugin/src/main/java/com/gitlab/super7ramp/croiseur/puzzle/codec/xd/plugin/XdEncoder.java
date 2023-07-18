/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.codec.xd.plugin;

import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleCodecDetails;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdCrossword;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.writer.XdCrosswordWriter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleEncoder;

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
