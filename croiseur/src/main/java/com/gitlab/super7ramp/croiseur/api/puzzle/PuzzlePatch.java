/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.puzzle;

import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;

import java.time.LocalDate;
import java.util.Optional;

public interface PuzzlePatch {

    long id();

    Optional<String> modifiedTitle();

    Optional<String> modifiedAuthor();

    Optional<String> modifiedEditor();

    Optional<String> modifiedCopyright();

    Optional<LocalDate> modifiedDate();

    Optional<PuzzleGrid> modifiedGrid();
}
