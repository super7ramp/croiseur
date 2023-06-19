/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The crossword clues.
 * <p>
 * Clues are immutable (returned collections are un-modifiable and will throw an exception if one
 * tries to modify them). Clues can only be created using the associated {@link Builder}.
 */
public final class XdClues {

    /**
     * Builder of {@link XdClues}.
     */
    public static final class Builder {

        /** The list of across clues being built. */
        private final List<XdClue> acrossClues;

        /** The list of down clues being built. */
        private final List<XdClue> downClues;

        /**
         * Constructs an instance.
         */
        public Builder() {
            acrossClues = new ArrayList<>();
            downClues = new ArrayList<>();
        }

        /**
         * Adds an across clue.
         *
         * @param number the across clue number
         * @param clue   the clue
         * @param answer the answer
         * @return this builder, for method chaining
         */
        public Builder across(final int number, final String clue, final String answer) {
            final XdClue xdClue = new XdClue(number, clue, answer);
            acrossClues.add(xdClue);
            return this;
        }

        /**
         * Adds an own clue.
         *
         * @param number the across clue number
         * @param clue   the clue
         * @param answer the answer
         * @return this builder, for method chaining
         */
        public Builder down(final int number, final String clue, final String answer) {
            final XdClue xdClue = new XdClue(number, clue, answer);
            downClues.add(xdClue);
            return this;
        }

        /**
         * Builds a {@link XdClues} from this builder.
         * <p>
         * Clues will be copied, this builder can be reused.
         *
         * @return a new {@link XdClues}
         */
        public XdClues build() {
            return new XdClues(acrossClues, downClues);
        }

        /**
         * Resets this builder.
         */
        public void reset() {
            acrossClues.clear();
            downClues.clear();
        }
    }

    /** The across clues, i.e. horizontal. The clue number is the index + 1. */
    private final List<XdClue> acrossClues;

    /** The down clues, i.e. vertical. The clue number is the index + 1. */
    private final List<XdClue> downClues;

    /**
     * Constructs an instance.
     *
     * @param acrossCluesArg the across clues
     * @param downCluesArg   the down clues
     */
    private XdClues(final List<XdClue> acrossCluesArg, final List<XdClue> downCluesArg) {
        acrossClues = new ArrayList<>(acrossCluesArg);
        downClues = new ArrayList<>(downCluesArg);
    }

    /**
     * Returns the across clues.
     *
     * @return the across clues
     */
    public List<XdClue> acrossClues() {
        return Collections.unmodifiableList(acrossClues);
    }

    /**
     * Returns the down clues.
     *
     * @return the down clues
     */
    public List<XdClue> downClues() {
        return Collections.unmodifiableList(downClues);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final XdClues xdClues)) return false;
        return Objects.equals(acrossClues, xdClues.acrossClues) &&
               Objects.equals(downClues, xdClues.downClues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(acrossClues, downClues);
    }

    @Override
    public String toString() {
        return "XdClues{" +
               "acrossClues=" + acrossClues +
               ", downClues=" + downClues +
               '}';
    }
}
