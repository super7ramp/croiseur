/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import re.belv.croiseur.puzzle.repository.memory.plugin.InMemoryPuzzleRepository;
import re.belv.croiseur.spi.puzzle.repository.PuzzleRepository;

/**
 * A puzzle repository implementation whose storage is purely in memory.
 */
module re.belv.croiseur.puzzle.repository.memory.plugin {
    requires re.belv.croiseur.spi.puzzle.repository;
    provides PuzzleRepository with InMemoryPuzzleRepository;
}