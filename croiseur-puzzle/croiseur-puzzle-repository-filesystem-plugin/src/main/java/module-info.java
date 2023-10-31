/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import re.belv.croiseur.puzzle.repository.filesystem.plugin.FileSystemPuzzleRepository;
import re.belv.croiseur.spi.puzzle.repository.PuzzleRepository;

/**
 * A puzzle repository implementation whose storage is filesystem. Files are written in the xd
 * format, which is easily readable by user.
 */
module re.belv.croiseur.puzzle.repository.filesystem.plugin {
    requires re.belv.croiseur.puzzle.codec.xd;
    requires re.belv.croiseur.spi.puzzle.repository;
    requires java.logging;
    provides PuzzleRepository with FileSystemPuzzleRepository;
}