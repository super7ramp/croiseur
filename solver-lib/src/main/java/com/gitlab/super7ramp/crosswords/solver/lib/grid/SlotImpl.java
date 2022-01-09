package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIdentifier;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of {@link Slot}.
 */
final class SlotImpl implements Slot {

    /** Connected slots. */
    private final Map<SlotIdentifier, SlotData> connectedSlots;

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
    SlotImpl(final SlotIdentifier anUid, final SlotData someData,
             final Map<SlotIdentifier, SlotData> someConnectedSlots) {
        uid = anUid;
        data = someData;
        connectedSlots = someConnectedSlots;
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
    public boolean isInstantiated() {
        return data.isInstantiated();
    }

    @Override
    public boolean isConnectedTo(SlotIdentifier other) {
        return connectedSlots.containsKey(other);
    }

    @Override
    public int emptyBoxRatio() {
        int empty = 0;
        for (int i = 0; i < data.length(); i++) {
            final char letter = data.letterAt(i);
            if (letter == BoxData.EMPTY_VALUE) {
                empty++;
            }
        }
        return empty * 100 / data.length();
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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
        return "SlotImpl{" + "data=" + data + ", uid=" + uid + '}';
    }

    @Override
    public String unassign() {

        final String clearedValue = value().orElseThrow(() -> new IllegalStateException("Illegal " +
                "unassignment of non-instantiated variable"));

        /*
         * Unassign only the boxes that are not part of a connected instantiated slot to avoid
         * unassignment of connected slots by side effect.
         */
        final SlotDefinition definition = data.definition();
        final Set<Integer> boxesToKeep =
                connectedSlots.values().stream()
                              .filter(SlotData::isInstantiated)
                              .map(connectedSlot ->
                                      definition.connectionWith(connectedSlot.definition()))
                              .collect(Collectors.toSet());

        if (boxesToKeep.size() == data.length()) {
            throw new IllegalStateException("Illegal attempt to unassign slot whereas all " +
                    "connected slots are instantiated");
        }
        data.clearExcept(boxesToKeep);

        return clearedValue;
    }
}
