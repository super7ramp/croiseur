/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.codec.xd.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The crossword grid.
 * <p>
 * Grid is immutable (returned collections are un-modifiable and will throw an exception if one
 * tries to modify them). Grid can only be created using the associated {@link Builder}.
 */
public final class XdGrid {

    /**
     * Position on the grid.
     *
     * @param column the column number, starting at zero
     * @param row    the row number, starting at zero
     */
    public record Index(int column, int row) {
        /**
         * Convenience factory method.
         *
         * @param column the column number, starting at zero
         * @param row    the row number, starting at zero
         * @return the created grid index
         */
        public static Index at(final int column, final int row) {
            return new Index(column, row);
        }
    }

    /**
     * The builder class for {@link XdGrid}.
     * <p>
     * Example:
     * <pre>{@code
     * var builder = new XdGrid.Builder();
     * var grid = builder.block(at(0,0))
     *                   .nonFilled(at(1,0))
     *                   .filled(at(2,0), "A")
     *                   .space(at(3,0))
     *                   .build();
     * }</pre>
     * Corresponds to: {@code #.A_}
     */
    public static final class Builder {

        /** The block positions. */
        private final Set<Index> blocks;

        /** The space positions. */
        private final Set<Index> spaces;

        /** The non-filled positions. */
        private final Set<Index> nonFilled;

        /** The filled cells. */
        private final Map<Index, String> filled;

        /**
         * Constructs an instance.
         */
        public Builder() {
            blocks = new HashSet<>();
            spaces = new HashSet<>();
            nonFilled = new HashSet<>();
            filled = new HashMap<>();
        }

        /**
         * Adds a block at given position.
         *
         * @param index the block position
         * @return this builder
         */
        public Builder block(final Index index) {
            blocks.add(index);
            return this;
        }

        /**
         * Adds a non-filled cell at given position.
         *
         * @param index the non-filled cell position
         * @return this builder
         */
        public Builder nonFilled(final Index index) {
            nonFilled.add(index);
            return this;
        }

        /**
         * Adds a void space at given position.
         *
         * @param index the void space position
         * @return this builder
         */
        public Builder space(final Index index) {
            spaces.add(index);
            return this;
        }

        /**
         * Adds a filled cell at given position
         *
         * @param index     the filled cell position
         * @param character the filled cell value
         * @return this builder
         */
        public Builder filled(final Index index, final char character) {
            filled.put(index, String.valueOf(character));
            return this;
        }

        /**
         * Builds the grid.
         * <p>
         * The data will-be deep-copied into the new grid, thus this builder can be reused.
         *
         * @return a new grid
         */
        public XdGrid build() {
            return new XdGrid(this);
        }

        /**
         * Resets this builder.
         */
        public void reset() {
            blocks.clear();
            spaces.clear();
            nonFilled.clear();
            filled.clear();
        }
    }

    /** The block positions. */
    private final Set<Index> blocks;

    /** The space positions. */
    private final Set<Index> spaces;

    /** The non-filled positions. */
    private final Set<Index> nonFilled;

    /** The filled cells. */
    private final Map<Index, String> filled;

    /**
     * Constructs an instance.
     *
     * @param builder the builder
     */
    private XdGrid(final Builder builder) {
        blocks = new HashSet<>(builder.blocks);
        spaces = new HashSet<>(builder.spaces);
        nonFilled = new HashSet<>(builder.nonFilled);
        filled = new HashMap<>(builder.filled);
    }

    /**
     * @return the block positions
     */
    public Set<Index> blocks() {
        return Collections.unmodifiableSet(blocks);
    }

    /**
     * @return the space positions
     */
    public Set<Index> spaces() {
        return Collections.unmodifiableSet(spaces);
    }

    /**
     * @return the non-filled positions
     */
    public Set<Index> nonFilled() {
        return Collections.unmodifiableSet(nonFilled);
    }

    /**
     * @return the filled cells
     */
    public Map<Index, String> filled() {
        return Collections.unmodifiableMap(filled);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final XdGrid xdGrid)) return false;
        return Objects.equals(blocks, xdGrid.blocks) &&
               Objects.equals(spaces, xdGrid.spaces) &&
               Objects.equals(nonFilled, xdGrid.nonFilled) &&
               Objects.equals(filled, xdGrid.filled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blocks, spaces, nonFilled, filled);
    }

    @Override
    public String toString() {
        return "XdGrid{" +
               "blocks=" + blocks +
               ", spaces=" + spaces +
               ", nonFilled=" + nonFilled +
               ", filled=" + filled +
               '}';
    }
}
