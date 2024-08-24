/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.common.puzzle;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

/**
 * Details about a puzzle.
 *
 * <p>All fields can be empty can return can be empty but never {@code null}.
 *
 * @param title the title
 * @param author the author
 * @param editor the editor
 * @param copyright the copyright
 * @param date the date
 */
public record PuzzleDetails(String title, String author, String editor, String copyright, Optional<LocalDate> date) {

    /** An empty instance. */
    private static final PuzzleDetails EMPTY = new PuzzleDetails("", "", "", "", Optional.empty());

    /**
     * Validates the record fields.
     *
     * @param title the title
     * @param author the author
     * @param editor the editor
     * @param copyright the copyright
     * @param date the date
     */
    public PuzzleDetails {
        Objects.requireNonNull(title, "Puzzle title shall not be null, use an empty String.");
        Objects.requireNonNull(editor, "Puzzle editor shall not be null, use an empty String.");
        Objects.requireNonNull(copyright, "Puzzle copyright shall not be null, use an empty String.");
        Objects.requireNonNull(date, "Puzzle date shall not be null, use an empty Optional.");
    }

    /**
     * Returns an instance with no details in it.
     *
     * @return an instance with no details in it
     */
    public static PuzzleDetails empty() {
        return EMPTY;
    }

    /**
     * Creates a new instance with only the date set to today's date.
     *
     * @return a new instance with only the date set to today's date
     */
    public static PuzzleDetails emptyOfToday() {
        return new PuzzleDetails("", "", "", "", Optional.of(LocalDate.now()));
    }
}
