/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model.puzzle.selection;

import com.gitlab.super7ramp.croiseur.gui.view.model.puzzle.edition.GridCoord;
import com.gitlab.super7ramp.croiseur.gui.view.model.puzzle.edition.PuzzleDetailsViewModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An immutable model for displaying a fixed view of a saved crossword puzzle.
 */
public final class SavedPuzzleViewModel {

    /**
     * A builder of the immutable view model.
     */
    public static final class Builder {

        /** The shaded boxes. */
        private final Set<GridCoord> shaded;

        /** The filled boxes. */
        private final Map<GridCoord, Character> filled;

        /** The puzzle id. */
        private long id;

        /** The puzzle revision. */
        private int revision;

        /** The puzzle title. */
        private String title;

        /** The puzzle author. */
        private String author;

        /** The puzzle editor. */
        private String editor;

        /** The puzzle copyright. */
        private String copyright;

        /** The puzzle creation date. */
        private String date;

        /** The number of columns. */
        private int numberOfColumns;

        /** The number of rows. */
        private int numberOfRows;

        /**
         * Constructs an instance.
         */
        public Builder() {
            shaded = new HashSet<>();
            filled = new HashMap<>();
            title = "";
            author = "";
            editor = "";
            copyright = "";
            date = "";
            numberOfColumns = 0;
            numberOfRows = 0;
        }

        /**
         * Defines the puzzle id.
         *
         * @param value the id
         * @return this builder for chaining
         */
        public Builder id(final long value) {
            id = value;
            return this;
        }

        /**
         * Defines the puzzle revision.
         *
         * @param value the revision
         * @return this builder for chaining
         */
        public Builder revision(final int value) {
            revision = value;
            return this;
        }

        /**
         * Defines the title.
         *
         * @param value the title
         * @return this builder for chaining
         */
        public Builder title(final String value) {
            title = value;
            return this;
        }

        /**
         * Defines the author.
         *
         * @param value the author
         * @return this builder for chaining
         */
        public Builder author(final String value) {
            author = value;
            return this;
        }

        /**
         * Defines the editor.
         *
         * @param value the editor
         * @return this builder for chaining
         */
        public Builder editor(final String value) {
            editor = value;
            return this;
        }

        /**
         * Defines the copyright.
         *
         * @param value the copyright
         * @return this builder for chaining
         */
        public Builder copyright(final String value) {
            copyright = value;
            return this;
        }

        /**
         * Defines the date.
         *
         * @param value the date
         * @return this builder for chaining
         */
        public Builder date(final String value) {
            date = value;
            return this;
        }

        /**
         * Defines the number of columns.
         *
         * @param value the number of columns
         * @return this builder for chaining
         */
        public Builder numberOfColumns(final int value) {
            numberOfColumns = value;
            return this;
        }

        /**
         * Defines the number of rows.
         *
         * @param value the number of rows
         * @return this builder for chaining
         */
        public Builder numberOfRows(final int value) {
            numberOfRows = value;
            return this;
        }

        /**
         * Defines a shaded box position.
         *
         * @param position a position
         * @return this builder for chaining
         */
        public Builder shaded(final GridCoord position) {
            shaded.add(position);
            return this;
        }

        /**
         * Defines a filled box.
         *
         * @param position the box position
         * @param content  the box content
         * @return this builder for chaining
         */
        public Builder filled(final GridCoord position, final Character content) {
            filled.put(position, content);
            return this;
        }

        /**
         * Builds an immutable view model.
         *
         * @return the built view model
         */
        public SavedPuzzleViewModel build() {
            if (numberOfColumns <= 0) {
                throw new IllegalArgumentException("Number of columns shall be strictly positive");
            }
            if (numberOfRows <= 0) {
                throw new IllegalArgumentException("Number of rows shall be strictly positive");
            }
            final var details = new PuzzleDetailsViewModel();
            details.id(id);
            details.revision(revision);
            details.title(title);
            details.author(author);
            details.editor(editor);
            details.copyright(copyright);
            details.date(date);
            final var grid =
                    new SavedPuzzleGridViewModel(numberOfColumns, numberOfRows, shaded, filled);
            return new SavedPuzzleViewModel(details, grid);
        }

    }

    /** The puzzle details. */
    private final PuzzleDetailsViewModel details;

    /** The puzzle grid. */
    private final SavedPuzzleGridViewModel grid;

    /**
     * Constructs an instance.
     *
     * @param detailsArg the puzzle details
     * @param gridArg    the puzzle grid
     */
    public SavedPuzzleViewModel(final PuzzleDetailsViewModel detailsArg,
                                final SavedPuzzleGridViewModel gridArg) {
        details = detailsArg;
        grid = gridArg;
    }

    /**
     * Returns the puzzle id.
     *
     * @return the puzzle id
     */
    public long id() {
        return details.id();
    }

    /**
     * Returns the puzzle revision.
     *
     * @return the puzzle revision
     */
    public int revision() {
        return details.revision();
    }

    /**
     * Returns the title.
     *
     * @return the title
     */
    public String title() {
        return details.title();
    }

    /**
     * Returns the author.
     *
     * @return the author
     */
    public String author() {
        return details.author();
    }

    /**
     * Returns the editor.
     *
     * @return the editor
     */
    public String editor() {
        return details.editor();
    }

    /**
     * Returns the copyright.
     *
     * @return the copyright
     */
    public String copyright() {
        return details.copyright();
    }

    /**
     * Returns the date.
     *
     * @return the date
     */
    public String date() {
        return details.date();
    }

    /**
     * Returns the grid column count.
     *
     * @return the grid column count
     */
    public int columnCount() {
        return grid.columnCount();
    }

    /**
     * Returns the grid row count.
     *
     * @return the grid row count
     */
    public int rowCount() {
        return grid.rowCount();
    }

    /**
     * Returns the grid shaded box positions.
     *
     * @return the grid shaded box positions.
     */
    public Set<GridCoord> shadedBoxes() {
        return grid.shaded();
    }

    /**
     * Returns the grid filled boxes.
     *
     * @return the grid filled boxes
     */
    public Map<GridCoord, Character> filledBoxes() {
        return grid.filled();
    }
}