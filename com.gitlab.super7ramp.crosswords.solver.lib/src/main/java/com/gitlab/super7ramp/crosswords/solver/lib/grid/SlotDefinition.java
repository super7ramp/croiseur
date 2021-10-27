package com.gitlab.super7ramp.crosswords.solver.lib.grid;

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

    private static void validateRange(final int aStart, final int aEnd) {
        if (aEnd <= aStart) {
            throw new IllegalArgumentException("Invalid slot definition");
        }
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

    /**
     * Returns whether this slot definition is connected with given one, i.e. if they cross.
     *
     * @param other other slot definition
     * @return whether slots cross
     */
    boolean isConnected(final SlotDefinition other) {
        final boolean differentType = type != other.type;
        final boolean thisOffsetIncludedInOtherRange = offset >= other.start && offset <= other.end;
        final boolean otherOffsetIncludedInThisRange = other.offset >= start && other.offset <= end;
        return differentType && thisOffsetIncludedInOtherRange && otherOffsetIncludedInThisRange;
    }
}
