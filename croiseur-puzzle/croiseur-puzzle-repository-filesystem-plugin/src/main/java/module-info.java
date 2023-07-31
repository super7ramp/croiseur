/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.puzzle.repository.filesystem.plugin.FileSystemPuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.PuzzleRepository;

/**
 * A puzzle repository implementation whose storage is filesystem. Files are written in the xd
 * format, which is easily readable by user.
 */
module com.gitlab.super7ramp.croiseur.repository.puzzle.filesystem.plugin {
    requires com.gitlab.super7ramp.croiseur.puzzle.codec.xd;
    requires com.gitlab.super7ramp.croiseur.spi.puzzle.repository;
    requires java.logging;
    provides PuzzleRepository with FileSystemPuzzleRepository;
}