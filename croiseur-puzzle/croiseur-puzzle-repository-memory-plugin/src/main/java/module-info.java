/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.puzzle.repository.memory.plugin.InMemoryPuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.PuzzleRepository;

/**
 * A puzzle repository implementation whose storage is purely in memory.
 */
module com.gitlab.super7ramp.croiseur.puzzle.repository.memory.plugin {
    requires com.gitlab.super7ramp.croiseur.spi.puzzle.repository;
    provides PuzzleRepository with InMemoryPuzzleRepository;
}