/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.puzzle;

import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;

import java.time.LocalDate;
import java.util.Optional;

/**
 * An aggregate of modifications to be applied on a puzzle.
 */
public interface PuzzlePatch {

    /**
     * The new title, or {@link Optional#empty()} if title shouldn't be changed.
     *
     * @return the new title, or {@link Optional#empty()} if title shouldn't be changed.
     */
    Optional<String> modifiedTitle();

    /**
     * The new author, or {@link Optional#empty()} if author shouldn't be changed.
     *
     * @return the new author, or {@link Optional#empty()} if author shouldn't be changed.
     */
    Optional<String> modifiedAuthor();

    /**
     * The new editor, or {@link Optional#empty()} if editor shouldn't be changed.
     *
     * @return the new editor, or {@link Optional#empty()} if editor shouldn't be changed.
     */
    Optional<String> modifiedEditor();

    /**
     * The new copyright, or {@link Optional#empty()} if copyright shouldn't be changed.
     *
     * @return the new copyright, or {@link Optional#empty()} if copyright shouldn't be changed.
     */
    Optional<String> modifiedCopyright();

    /**
     * The new date, or {@link Optional#empty()} if date shouldn't be changed.
     *
     * @return the new date, or {@link Optional#empty()} if date shouldn't be changed.
     */
    Optional<LocalDate> modifiedDate();

    /**
     * The new grid, or {@link Optional#empty()} if grid shouldn't be changed.
     *
     * @return the new grid, or {@link Optional#empty()} if grid shouldn't be changed.
     */
    Optional<PuzzleGrid> modifiedGrid();
}
