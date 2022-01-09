package com.gitlab.super7ramp.crosswords.solver.lib.grid;

interface BoxData {

    /** Empty character value. */
    char EMPTY_VALUE = ' ';

    /** Shaded value. */
    char SHADED_VALUE = '#';

    char value();

    boolean isShaded();

    void set(final char aCharacter);

    void reset();

    BoxData copy();
}
