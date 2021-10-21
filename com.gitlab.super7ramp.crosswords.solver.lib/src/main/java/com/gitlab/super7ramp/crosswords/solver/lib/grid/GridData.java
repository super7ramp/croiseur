package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import com.gitlab.super7ramp.crosswords.solver.api.Coordinate;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Stores a crossword puzzle data.
 */
final class GridData {

    /**
     * A {@link GridData} builder.
     */
    static class GridDataBuilder {

        /** The shaded boxes. */
        private final Set<Coordinate> shaded;

        /** The prefilled (not shaded) boxes. */
        private final Map<Coordinate, Character> prefilled;

        /** The height. */
        private int height;

        /** The width. */
        private int width;

        /**
         * Constructor.
         */
        private GridDataBuilder() {
            shaded = new HashSet<>();
            prefilled = new HashMap<>();
        }

        /**
         * Specify a height for {@link GridData} to build.
         *
         * @param anHeight the height
         * @return this builder
         */
        public GridDataBuilder withHeight(final int anHeight) {
            height = anHeight;
            return this;
        }

        /**
         * Specify a width for the {@link GridData} to build.
         *
         * @param aWidth the width
         * @return this builder
         */
        public GridDataBuilder withWidth(final int aWidth) {
            width = aWidth;
            return this;
        }

        /**
         * Specify the position of a shaded box.
         *
         * @param coordinate coordinate
         * @return this builder
         */
        public GridDataBuilder withShaded(final Coordinate coordinate) {
            shaded.add(coordinate);
            return this;
        }

        /**
         * Use a {@link PuzzleDefinition} to build the grid.
         *
         * @param puzzle the {@link PuzzleDefinition}
         * @return this {@link GridDataBuilder}
         */
        public GridDataBuilder from(final PuzzleDefinition puzzle) {
            width = puzzle.width();
            height = puzzle.height();
            shaded.addAll(puzzle.shaded());
            prefilled.putAll(puzzle.filled());
            return this;
        }

        /**
         * Actually builds the data.
         *
         * @return the built data
         * @throws IllegalArgumentException if given specifications are not valid
         */
        public GridData build() {
            validate();
            return new GridData(buildGrid(), buildSlots());
        }

        private Map<SlotIdentifier, SlotDefinition> buildSlots() {
            final Map<SlotIdentifier, SlotDefinition> slots = new HashMap<>();
            int id = 1;
            for (int x = 0; x < width; x++) {
                // Vertical slots
                for (int yStart = 0, yEnd = nextShadedOnColumn(x, 0);
                     yStart < height;
                     yStart = nextVerticalSlot(x, yEnd), yEnd = nextShadedOnColumn(x, yStart), id++) {
                    slots.put(new SlotIdentifier(id), new SlotDefinition(x, yStart, yEnd, SlotDefinition.Type.VERTICAL));
                }
            }
            for (int y = 0; y < height; y++) {
                // Horizontal slots
                for (int xStart = 0, xEnd = nextShadedOnLine(y, 0);
                     xStart < width;
                     xStart = nextHorizontalSlot(y, xEnd), xEnd = nextShadedOnLine(y, xStart), id++) {
                    slots.put(new SlotIdentifier(id), new SlotDefinition(y, xStart, xEnd, SlotDefinition.Type.HORIZONTAL));
                }
            }

            return slots;
        }

        private int nextShadedOnLine(final int y, final int xStart) {
            for (int x = xStart; x < width; x++) {
                if (shaded.contains(new Coordinate(x, y))) {
                    return x;
                }
            }
            return width;
        }

        private int nextShadedOnColumn(final int x, final int yStart) {
            for (int y = yStart; y < height; y++) {
                if (shaded.contains(new Coordinate(x, y))) {
                    return y;
                }
            }
            return height;
        }

        private int nextVerticalSlot(final int x, final int yStart) {
            for (int y = yStart; y < height; y++) {
                if (!shaded.contains(new Coordinate(x, y))) {
                    return y;
                }
            }
            return height;
        }

        private int nextHorizontalSlot(final int y, final int xStart) {
            for (int x = xStart; x < width; x++) {
                if (!shaded.contains(new Coordinate(x, y))) {
                    return x;
                }
            }
            return width;
        }

        private BoxData[][] buildGrid() {
            final BoxData[][] result = new BoxData[width][height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    result[x][y] = new BoxData();
                    final Coordinate coord = new Coordinate(x, y);
                    if (shaded.contains(coord)) {
                        result[x][y].set(BoxData.SHADED);
                    } else if (prefilled.containsKey(coord)) {
                        result[x][y].set(prefilled.get(coord));
                    }
                }
            }
            return result;
        }

        private void validate() {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Invalid dimensions");
            }
            if (shaded.stream().anyMatch(s -> s.x() < 0 || s.x() >= width || s.y() < 0 || s.y() >= height)) {
                throw new IllegalArgumentException("Invalid shaded definitions");
            }
        }

    }

    /**
     * Access to data for a given slot.
     */
    static class SlotData {

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

        Optional<Character> letterAt(final int index) {
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
                final Optional<Character> letter = boxAt(i).value();
                if (letter.isEmpty()) {
                    return Optional.empty();
                }
                readValue[i] = letter.get();
            }
            return Optional.of(String.valueOf(readValue));
        }

        private BoxData boxAt(final int i) {
            final BoxData box;
            if (definition.type().isHorizontal()) {
                box = grid[definition.offset()][definition.start() + i];
            } else {
                box = grid[definition.start() + i][definition.offset()];
            }
            return box;
        }
    }

    /**
     * Slot definition.
     * <p>
     * Example of {@link Type#HORIZONTAL horizontal} slot:
     * <pre>
     *      0 1 2 3 4 5 6
     *   0 | | | | | | | |
     *   1 | |#|A|B|C|#| | <-- offset = 1
     *   2 | | | | | | | |
     *          ^   ^
     *          |   ` end = 4
     *          `- start = 2
     * </pre>
     * Example of {@link Type#VERTICAL vertical} slot:
     * <pre>
     *      0 1 2 3 4 5 6
     *   0 | | | |B| | | | <-- start = 0
     *   1 | |#| |B| |#| |
     *   2 | | | |B| | | | <-- end = 1
     *            ^
     *            ` offset = 3
     * </pre>
     */
    static class SlotDefinition {

        /**
         * Type of slot.
         */
        enum Type {
            /** A horizontally aligned slot. */
            HORIZONTAL,
            /** A vertically aligned slot. */
            VERTICAL;

            /** @return whether this is {@link #HORIZONTAL} */
            boolean isHorizontal() {
                return this == HORIZONTAL;
            }
        }

        /** Offset. */
        private final int offset;

        /** Start of slot (included). */
        private final int start;

        /** End of slot (excluded). */
        private final int end;

        /** Type. */
        private final Type type;

        /**
         * Constructor.
         *
         * @param anOffset offset
         * @param aStart   start of slot (included)
         * @param aEnd     end of slot (excluded)
         * @param aType    type of slot
         */
        private SlotDefinition(final int anOffset, final int aStart, final int aEnd, final Type aType) {
            offset = anOffset;
            start = aStart;
            end = aEnd;
            type = aType;
        }

        @Override
        public String toString() {
            return "SlotDefinition{" +
                    "offset=" + offset +
                    ", start=" + start +
                    ", end=" + end +
                    ", type=" + type +
                    '}';
        }

        Type type() {
            return type;
        }

        int offset() {
            return offset;
        }

        int start() {
            return start;
        }

        int length() {
            return end - start;
        }

        boolean isConnected(final SlotDefinition other) {
            return type != other.type && offset >= other.start && offset <= other.end;
        }
    }

    /**
     * A box, either containing a letter or shaded.
     */
    private static class BoxData {

        /** Empty character value. */
        private static final char EMPTY = 0;

        /** Shaded value. */
        private static final char SHADED = '#';

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

    /** The grid. */
    private final BoxData[][] grid;

    /** The word slots. */
    private final Map<SlotIdentifier, SlotDefinition> slots;

    /**
     * Constructor.
     *
     * @param aGrid     the grid
     * @param someSlots the word slots
     */
    private GridData(final BoxData[][] aGrid, final Map<SlotIdentifier, SlotDefinition> someSlots) {
        grid = aGrid;
        slots = Collections.unmodifiableMap(someSlots);
    }

    /**
     * Copy constructor.
     *
     * @param other other data
     */
    private GridData(final GridData other) {
        grid = new BoxData[other.grid.length][other.grid[0].length];
        for (int x = 0; x < other.grid.length; x++) {
            for (int y = 0; y < other.grid[0].length; y++) {
                grid[x][y] = new BoxData(other.grid[x][y]);
            }
        }

        // Unmodifiable map of immutable objects, no need to copy
        slots = other.slots;
    }

    /**
     * Creates a new {@link GridDataBuilder}.
     *
     * @return a new {@link GridDataBuilder}
     */
    static GridDataBuilder newBuilder() {
        return new GridDataBuilder();
    }

    /**
     * Returns a deep-copy of this {@link GridData}.
     *
     * @return a deep-copy of this {@link GridData}
     */
    GridData copy() {
        return new GridData(this);
    }

    /**
     * Returns the {@link SlotData} for given {@link SlotIdentifier}.
     *
     * @param slotId the {@link SlotIdentifier}
     * @return the corresponding {@link SlotData}
     */
    SlotData slot(final SlotIdentifier slotId) {
        return new SlotData(slots.get(slotId), grid);
    }

    /**
     * Returns all the {@link SlotData}.
     *
     * @return all the {@link SlotData}
     */
    Map<SlotIdentifier, SlotData> slots() {
        final Map<SlotIdentifier, SlotData> result = new HashMap<>();
        slots.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), new SlotData(entry.getValue(), grid)))
                .forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        return result;
    }

    /**
     * Returns the slots connected to given slot identifier
     */
    Map<SlotIdentifier, SlotData> connectedSlots(final SlotIdentifier slotId) {
        final SlotDefinition slotDefinition = slots.get(slotId);
        final Map<SlotIdentifier, SlotData> result = new HashMap<>();
        slots.entrySet().stream()
                .filter(entry -> entry.getValue().isConnected(slotDefinition))
                .map(entry -> Map.entry(entry.getKey(), new SlotData(entry.getValue(), grid)))
                .forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        return result;
    }

    Map<Coordinate, Character> toBoxes() {
        final Map<Coordinate, Character> result = new HashMap<>();
        for (int x = 0; x < grid.length; x++) {
            final BoxData[] line = grid[x];
            for (int y = 0; y < line.length; y++) {
                if (!line[y].isShaded()) {
                    final Optional<Character> boxValue = line[y].value();
                    if (boxValue.isPresent()) {
                        result.put(new Coordinate(x, y), boxValue.get());
                    } else {
                        return Collections.emptyMap();
                    }
                }
            }
        }
        return result;
    }
}
