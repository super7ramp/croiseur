/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.repository.filesystem.plugin;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Path to repository.
 */
final class PuzzleRepositoryPath {

    /** Private constructor to prevent instantiation. */
    private PuzzleRepositoryPath() {
        // Nothing to do.
    }

    /**
     * Gets the path to the puzzle repository.
     *
     * @return the path to the puzzle repository
     */
    static Path get() {
        return Path.of(systemProperty().orElseGet(PuzzleRepositoryPath::directoryUnderUserHome));
    }

    private static Optional<String> systemProperty() {
        return Optional.ofNullable(
                System.getProperty("com.gitlab.super7ramp.croiseur.puzzle.path"));
    }

    private static String directoryUnderUserHome() {
        return System.getProperty("user.home") + "/croiseur/puzzles";
    }
}
