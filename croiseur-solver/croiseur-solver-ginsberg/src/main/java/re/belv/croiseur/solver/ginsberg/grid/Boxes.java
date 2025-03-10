/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.grid;

import java.util.NoSuchElementException;

/** Factory of {@link BoxData} implementations. */
final class Boxes {

    /** A box containing a computed letter. */
    private static final class ComputedBox implements BoxData {

        /** The empty value. */
        private static char EMPTY_VALUE = ' ';

        /** The value. */
        private char character;

        /** Constructor. */
        ComputedBox() {
            character = EMPTY_VALUE;
        }

        /**
         * Copy constructor.
         *
         * @param other object to copy
         */
        private ComputedBox(final ComputedBox other) {
            character = other.character;
        }

        @Override
        public char value() {
            if (character == EMPTY_VALUE) {
                throw new NoSuchElementException("Box is empty");
            }
            return character;
        }

        @Override
        public boolean isShaded() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return character == EMPTY_VALUE;
        }

        @Override
        public void set(final char aCharacter) {
            character = aCharacter;
        }

        @Override
        public void reset() {
            character = EMPTY_VALUE;
        }

        @Override
        public BoxData copy() {
            return new ComputedBox(this);
        }

        @Override
        public String toString() {
            return String.valueOf(character);
        }
    }

    /** A box containing a pre-filled letter. */
    private static final class PrefilledBox implements BoxData {

        /** The value. */
        private final char character;

        /**
         * Constructor.
         *
         * @param aCharacter prefilled box value
         */
        PrefilledBox(final char aCharacter) {
            character = aCharacter;
        }

        @Override
        public char value() {
            return character;
        }

        @Override
        public boolean isShaded() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void set(final char aCharacter) {
            if (character != aCharacter) {
                throw new UnsupportedOperationException("Illegal attempt to modify a prefilled " + "box");
            }
        }

        @Override
        public void reset() {
            // Do nothing.
        }

        @Override
        public BoxData copy() {
            // Immutable object, no need to copy
            return this;
        }

        @Override
        public String toString() {
            return String.valueOf(character);
        }
    }

    /** A shaded box. */
    private static final class ShadedBox implements BoxData {

        /** Error message. */
        private static final String SHADED_BOXES_ARE_IMMUTABLE = "Shaded boxes are immutable";

        /** Constructor. */
        ShadedBox() {
            // Nothing to do.
        }

        @Override
        public char value() {
            throw new NoSuchElementException("Shaded box has no value");
        }

        @Override
        public boolean isShaded() {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public void set(final char aCharacter) {
            throw new UnsupportedOperationException(SHADED_BOXES_ARE_IMMUTABLE);
        }

        @Override
        public void reset() {
            throw new UnsupportedOperationException(SHADED_BOXES_ARE_IMMUTABLE);
        }

        @Override
        public BoxData copy() {
            // Immutable object, no need to create a new object
            return this;
        }

        @Override
        public String toString() {
            return String.valueOf('#');
        }
    }

    /** Shaded box constant. */
    private static final BoxData SHADED = new ShadedBox();

    /** Private constructor, static utilities only. */
    private Boxes() {
        // Nothing to do.
    }

    /** @return a shaded unmodifiable box */
    static BoxData shaded() {
        return SHADED;
    }

    /** @return an empty modifiable box */
    static BoxData computed() {
        return new ComputedBox();
    }

    /**
     * @param character pre-filled value
     * @return a prefilled immutable box
     */
    static BoxData prefilled(final char character) {
        return new PrefilledBox(character);
    }
}
