/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.plugin.XdDecoder;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.plugin.XdEncoder;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleDecoder;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleEncoder;

/**
 * Puzzle codec provider adapting croiseur-puzzle-codec-xd.
 */
module com.gitlab.super7ramp.croiseur.puzzle.codec.xd.plugin {
    requires com.gitlab.super7ramp.croiseur.common;
    requires com.gitlab.super7ramp.croiseur.puzzle.codec.xd;
    requires com.gitlab.super7ramp.croiseur.spi.puzzle.codec;
    provides PuzzleDecoder with XdDecoder;
    provides PuzzleEncoder with XdEncoder;
}