/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Encodes/decodes the xd format, a crossword format.
 *
 * @see <a
 * href=https://github.com/century-arcade/xd/blob/059c2eca6917cd94c0a61199198b96e8aa80f6db/doc/xd-format.md>The
 * xd format specification</a>
 */
module re.belv.croiseur.puzzle.codec.xd {
    exports re.belv.croiseur.puzzle.codec.xd.model;
    exports re.belv.croiseur.puzzle.codec.xd.reader;
    exports re.belv.croiseur.puzzle.codec.xd.writer;
}