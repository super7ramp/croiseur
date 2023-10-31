/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.grid;

import re.belv.croiseur.solver.ginsberg.core.Slot;
import re.belv.croiseur.solver.ginsberg.core.SlotIdentifier;

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

    /** Connectivity between slots. */
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
    public String asPattern() {
        final StringBuilder sb = new StringBuilder(data.length());
        for (int i = 0; i < data.length(); i++) {
            final BoxData box = data.boxAt(i);
            if (box.isEmpty()) {
                sb.append(' ');
            } else {
                sb.append(box.value());
            }
        }
        return sb.toString();
    }

    @Override
    public boolean isInstantiated() {
        return data.isInstantiated();
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
    public void assign(final String value) {
        data.write(value);
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

        data.clearExcept(boxesToKeep);

        return clearedValue;
    }

    @Override
    public SlotDefinition definition() {
        return data.definition();
    }

    @Override
    public String toString() {
        return "SlotImpl{" + "data=" + data + ", uid=" + uid + '}';
    }
}
