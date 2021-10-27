package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import java.util.Optional;

/**
 * Access to data for a given slot.
 */
final class SlotData {

    /** The whole grid data. */
    private final BoxData[][] grid;

    /** The slot definition. */
    private final SlotDefinition definition;

    /**
     * Constructor.
     *
     * @param aDefinition the slot definition
     * @param aGrid       the whole grid data
     */
    SlotData(final SlotDefinition aDefinition, BoxData[][] aGrid) {
        definition = aDefinition;
        grid = aGrid;
    }

    @Override
    public String toString() {
        return "SlotData{" +
                "definition=" + definition +
                '}';
    }

    int length() {
        return definition.length();
    }

    char letterAt(final int index) {
        return boxAt(index).value();
    }

    void write(final String value) {
        if (value == null || definition.length() != value.length()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < value.length(); i++) {
            boxAt(i).set(value.charAt(i));
        }
    }

    void clear() {
        for (int i = 0; i < definition.length(); i++) {
            boxAt(i).reset();
        }
    }

    Optional<String> value() {
        final char[] readValue = new char[definition.length()];
        for (int i = 0; i < definition.length(); i++) {
            final char letter = boxAt(i).value();
            if (letter == BoxData.EMPTY) {
                return Optional.empty();
            }
            readValue[i] = letter;
        }
        return Optional.of(String.valueOf(readValue));
    }

    private BoxData boxAt(final int i) {
        final BoxData box;
        if (definition.type().isHorizontal()) {
            box = grid[definition.start() + i][definition.offset()];
        } else {
            box = grid[definition.offset()][definition.start() + i];
        }
        return box;
    }
}
