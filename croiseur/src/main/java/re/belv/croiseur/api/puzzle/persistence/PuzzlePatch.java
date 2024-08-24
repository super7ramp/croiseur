/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.api.puzzle.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import re.belv.croiseur.common.puzzle.PuzzleGrid;

/**
 * Modifications to be applied on a puzzle.
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

    /**
     * The new across clues, or {@link Optional#empty()} if across clues shouldn't be changed.
     *
     * @return the new across clues, or {@link Optional#empty()} if across clues shouldn't be
     * changed.
     */
    Optional<List<String>> modifiedAcrossClues();

    /**
     * The new down clues, or {@link Optional#empty()} if down clues shouldn't be changed.
     *
     * @return the new down clues, or {@link Optional#empty()} if down clues shouldn't be changed.
     */
    Optional<List<String>> modifiedDownClues();
}
