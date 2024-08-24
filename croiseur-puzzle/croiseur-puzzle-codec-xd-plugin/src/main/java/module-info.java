/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import re.belv.croiseur.puzzle.codec.xd.plugin.XdDecoder;
import re.belv.croiseur.puzzle.codec.xd.plugin.XdEncoder;
import re.belv.croiseur.spi.puzzle.codec.PuzzleDecoder;
import re.belv.croiseur.spi.puzzle.codec.PuzzleEncoder;

/**
 * Puzzle codec provider adapting croiseur-puzzle-codec-xd.
 */
module re.belv.croiseur.puzzle.codec.xd.plugin {
    requires re.belv.croiseur.common;
    requires re.belv.croiseur.puzzle.codec.xd;
    requires re.belv.croiseur.spi.puzzle.codec;

    provides PuzzleDecoder with
            XdDecoder;
    provides PuzzleEncoder with
            XdEncoder;
}
