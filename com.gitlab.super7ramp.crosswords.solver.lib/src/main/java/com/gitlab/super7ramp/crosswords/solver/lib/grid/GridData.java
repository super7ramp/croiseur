package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import com.gitlab.super7ramp.crosswords.solver.api.Coordinates;

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
     * The grid.
     */
    private final BoxData[][] grid;
    /**
     * The word slots.
     */
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

    /**
     * A box containing a letter.
     */
    private static class BoxData {

        /**
         * Empty character value.
         */
        private static final char EMPTY = 0;

        private char character;

        /**
         * Constructor.
         */
        BoxData() {
            // Nothing to do
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

        void set(final char aCharacter) {
            character = aCharacter;
        }

        void reset() {
            character = EMPTY;
        }

    }

    /**
     * A {@link GridData} builder.
     */
    static class GridDataBuilder {

        /**
         * The shaded boxes.
         */
        private final Set<Coordinates> shaded;
        /**
         * The UID to use for next slot.
         */
        private int currentUid;
        /**
         * The height.
         */
        private int height;
        /**
         * The width.
         */
        private int width;

        /**
         * Constructor.
         */
        private GridDataBuilder() {
            shaded = new HashSet<>();
            currentUid = 1;
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
         * @param x horizontal coordinate
         * @param y vertical coordinate
         * @return this builder
         */
        public GridDataBuilder withShaded(final int x, final int y) {
            shaded.add(new Coordinates(x, y));
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
            if (!shaded.isEmpty()) {
                throw new UnsupportedOperationException("Shaded not yet implemented");
            }

            // TODO consider shaded
            final Map<SlotIdentifier, SlotDefinition> slots = new HashMap<>();
            for (int x = 0; x < width; x++) {
                // Vertical slots
                slots.put(getAndIncrementUid(), new SlotDefinition(x, 0, height, SlotDefinition.Type.VERTICAL));
            }
            for (int y = 0; y < height; y++) {
                // Horizontal slots
                slots.put(getAndIncrementUid(), new SlotDefinition(y, 0, height, SlotDefinition.Type.HORIZONTAL));
            }
            return slots;
        }

        private BoxData[][] buildGrid() {
            final BoxData[][] result = new BoxData[width][height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    result[x][y] = new BoxData();
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

        private SlotIdentifier getAndIncrementUid() {
            return new SlotIdentifier(currentUid++);
        }

    }

    /**
     * Access to data for a given slot.
     */
    static class SlotData {

        private final BoxData[][] grid;
        private final SlotDefinition definition;

        SlotData(final SlotDefinition aDefinition, BoxData[][] aGrid) {
            definition = aDefinition;
            grid = aGrid;
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
            if (definition.isHorizontal()) {
                box = grid[definition.offset()][definition.start() + i];
            } else {
                box = grid[definition.start() + i][definition.offset()];
            }
            return box;
        }

    }

    /**
     * Slot definition.
     */
    private static class SlotDefinition {

        /**
         * Offset.
         */
        private final int offset;
        /**
         * Start of slot (included).
         */
        private final int start;
        /**
         * End of slot (excluded).
         */
        private final int end;
        /**
         * Type.
         */
        private final Type type;

        /**
         * Constructor.
         *
         * @param anOffset offset
         * @param aStart   start of slot (included)
         * @param aEnd     end of slot (excluded)
         * @param aType    type of slot
         */
        SlotDefinition(final int anOffset, final int aStart, final int aEnd, final Type aType) {
            offset = anOffset;
            start = aStart;
            end = aEnd;
            type = aType;
        }

        boolean isHorizontal() {
            return type == Type.HORIZONTAL;
        }

        int offset() {
            return offset;
        }

        int start() {
            return start;
        }

        int end() {
            return end;
        }

        int length() {
            return end - start - 1;
        }

        boolean isConnected(final SlotDefinition other) {
            return type != other.type && offset >= other.start && offset <= other.end;
        }

        /**
         * Type of slot.
         */
        private enum Type {
            /**
             * A horizontally aligned slot.
             */
            HORIZONTAL,
            /**
             * A vertically aligned slot.
             */
            VERTICAL
        }
    }
}
