package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import java.util.Optional;

/**
 * A box, either containing a letter or shaded.
 */
final class BoxData {

    /** Empty character value. */
    static final char EMPTY = 0;

    /** Shaded value. */
    static final char SHADED = '#';

    /** The value. */
    private char character;

    /**
     * Constructor.
     */
    BoxData() {
        // Nothing to do.
    }

    /**
     * Copy constructor.
     */
    BoxData(final BoxData other) {
        character = other.character;
    }

    Optional<Character> value() {
        if (character == EMPTY) {
            return Optional.empty();
        }
        return Optional.of(character);
    }

    boolean isShaded() {
        return character == SHADED;
    }

    void set(final char aCharacter) {
        character = aCharacter;
    }

    void reset() {
        character = EMPTY;
    }
}
