/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import picocli.CommandLine.Command;

/**
 * The 'puzzle' command: Manage the puzzle repository.
 */
@Command(name = "puzzle")
public final class PuzzleCommand {

    /** The puzzle service. */
    private final PuzzleService puzzleService;

    /**
     * Constructs an instance.
     *
     * @param puzzleServiceArg the puzzle service
     */
    public PuzzleCommand(final PuzzleService puzzleServiceArg) {
        puzzleService = puzzleServiceArg;
    }

    /**
     * Lists the available puzzles.
     */
    @Command(name = "list", aliases = {"ls"})
    void list() {
        puzzleService.list();
    }
}
