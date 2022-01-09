package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import java.util.NoSuchElementException;

/**
 * Slot definition.
 * <p>
 * Example of {@link Type#HORIZONTAL horizontal} slot:
 * <pre>
 *      0 1 2 3 4 5 6
 *   0 | | | | | | | |
 *   1 | |#|A|B|C|#| | <-- offset = 1
 *   2 | | | | | | | |
 *   3 | | | |#| | | |
 *          ^     ^
 *          |      ` end = 4 (exclusive)
 *          `- start = 2 (inclusive)
 * </pre>
 * Example of {@link Type#VERTICAL vertical} slot:
 * <pre>
 *      0 1 2 3 4 5 6
 *   0 | | | |B| | | | <-- start = 0 (inclusive)
 *   1 | |#| |B| |#| |
 *   2 | | | |B| | | |
 *   3 | | | |#| | | | <-- end = 3 (exclusive)
 *            ^
 *            ` offset = 3
 * </pre>
 */
final class SlotDefinition {

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
    SlotDefinition(final int anOffset, final int aStart, final int aEnd, final Type aType) {
        validateRange(aStart, aEnd);
        offset = anOffset;
        start = aStart;
        end = aEnd;
        type = aType;
    }

    /**
     * Validate range.
     *
     * @param aStart start index
     * @param aEnd   end index
     */
    private static void validateRange(final int aStart, final int aEnd) {
        if (aEnd <= aStart) {
            throw new IllegalArgumentException("Invalid slot definition");
        }
    }

    @Override
    public String toString() {
        return "SlotDefinition{" + "offset=" + offset + ", start=" + start + ", end=" + end + ", "
                + "type=" + type + '}';
    }

    /**
     * @return the type of slot
     */
    Type type() {
        return type;
    }

    /**
     * Return the offset of this slot, i.e.:
     * <ul>
     *     <li>the x coordinate for a vertical slot definition</li>
     *     <li>the y coordinate for a horizontal slot definition</li>
     * </ul>
     *
     * @return the offset
     */
    int offset() {
        return offset;
    }

    /**
     * @return the start index
     */
    int start() {
        return start;
    }

    /**
     * @return the end index
     */
    int length() {
        return end - start;
    }

    /**
     * Returns whether this slot definition is connected with given one, i.e. if both slots cross.
     *
     * @param other other slot definition
     * @return whether slots cross
     */
    boolean isConnected(final SlotDefinition other) {
        final boolean differentType = type != other.type;
        final boolean thisOffsetIncludedInOtherRange = offset >= other.start && offset < other.end;
        final boolean otherOffsetIncludedInThisRange = other.offset >= start && other.offset < end;
        return differentType && thisOffsetIncludedInOtherRange && otherOffsetIncludedInThisRange;
    }

    /**
     * Returns where this instance crosses the other slot.
     * <p>
     * Example:
     * <pre>
     *      0 1 2 3 4 5 6
     *   0 | | | |E| | | |
     *   1 |#|A|B|C|D|#| |
     *   2 | | | |F| | | |
     * </pre>
     * The crossing box is "C" at (3,1). The method returns:
     * <ul>
     * <li>2 if "this" is "ABCD" and "other" is "ECF" because "C" is the third letter for "this"
     * slot.
     * <li>1 if "this" is "ECF" and "other" is "ABCD" because "C" is the second letter for "this"
     * slot.
     * </ul>
     *
     * @param other other slot definition
     * @return where other slot crosses this slot
     * @throws NoSuchElementException if slots do not cross
     */
    int connectionWith(final SlotDefinition other) {
        if (!isConnected(other)) {
            throw new NoSuchElementException();
        }
        return other.offset - start;
    }
}
