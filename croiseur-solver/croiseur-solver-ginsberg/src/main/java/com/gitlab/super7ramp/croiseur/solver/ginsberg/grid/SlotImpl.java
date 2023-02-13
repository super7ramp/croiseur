/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.grid;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.SlotIdentifier;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * Implementation of {@link Slot}.
 * <p>
 * Just a view above {@link SlotData}.
 */
final class SlotImpl implements InternalSlot {

    /** Raw data access to slot. */
    private final SlotData data;

    /** Uid. */
    private final SlotIdentifier uid;

    private final Connectivity connectivity;

    /**
     * Constructor.
     *
     * @param anUid    a {@link SlotIdentifier}
     * @param someData raw data access
     */
    SlotImpl(final SlotIdentifier anUid, final SlotData someData,
             final Connectivity connectivityArg) {
        uid = anUid;
        data = someData;
        connectivity = connectivityArg;
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
    public boolean isConnectedTo(final SlotIdentifier other) {
        return connectivity.test(uid, other);
    }

    @Override
    public Stream<? extends Slot> connectedSlots() {
        return connectivity.connectedSlots(uid);
    }

    @Override
    public int emptyBoxRatio() {
        return data.emptyBoxRatio();
    }

    @Override
    public boolean isCompatibleWith(final String value) {
        if (data.length() != value.length()) {
            return false;
        }

        for (int i = 0; i < data.length(); i++) {
            final BoxData box = data.boxAt(i);
            if (!box.isEmpty() && box.value() != value.charAt(i)) {
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
                connectivity.connectedSlots(uid)
                            .filter(Slot::isInstantiated)
                            .map(connectedSlot ->
                                    definition.connectionWith(connectedSlot.definition()))
                            .collect(toSet());

        /*
        if (boxesToKeep.size() == data.length()) {
            throw new IllegalStateException("Illegal attempt to unassign slot whereas all " +
                    "connected slots are instantiated");
        }
         */
        data.clearExcept(boxesToKeep);

        return clearedValue;
    }

    @Override
    public SlotDefinition definition() {
        return data.definition();
    }
}
