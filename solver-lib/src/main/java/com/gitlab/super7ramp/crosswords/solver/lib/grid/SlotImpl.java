package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIdentifier;

import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of {@link Slot}.
 */
final class SlotImpl implements Slot {

    /** Raw data access to slot. */
    private final SlotData data;

    /** Uid. */
    private final SlotIdentifier uid;

    /**
     * Constructor.
     *
     * @param anUid    a {@link SlotIdentifier}
     * @param someData raw data access
     */
    SlotImpl(final SlotIdentifier anUid, final SlotData someData) {
        uid = anUid;
        data = someData;
    }

    @Override
    public SlotIdentifier uid() {
        return uid;
    }

    @Override
    public Optional<String> value() {
        return data.value();
    }

    @Override
    public boolean isCompatibleWith(final String value) {
        if (data.length() != value.length()) {
            return false;
        }

        for (int i = 0; i < data.length(); i++) {
            final char letter = data.letterAt(i);
            if (letter != BoxData.EMPTY_VALUE && letter != value.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isPrefilled() {
        return false;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SlotImpl slot = (SlotImpl) o;
        return uid.equals(slot.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }

    @Override
    public void assign(final String value) {
        data.write(value);
    }

    @Override
    public String toString() {
        return "SlotImpl{" +
                "data=" + data +
                ", uid=" + uid +
                '}';
    }

    @Override
    public Optional<String> unassign() {
        final Optional<String> clearedValue = value();
        data.clear();
        return clearedValue;
    }
}
