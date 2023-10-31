/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.grid;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Access to data for a given slot.
 */
final class SlotData {

    /** The whole grid data. */
    private final BoxData[][] grid;

    /** The slot definition. */
    private final SlotDefinition definition;

    /**
     * Instantiation flag.
     * <p>
     * This flag allows to identify whether the variable represented by this data has been
     * instantiated. Relying on value presence is not sufficient as slot data may entirely be
     * filled by side effect of connected variables.
     */
    private boolean instantiated;

    /**
     * Constructor.
     *
     * @param aDefinition the slot definition
     * @param aGrid       the whole grid data
     */
    SlotData(final SlotDefinition aDefinition, final BoxData[][] aGrid) {
        this(aDefinition, aGrid, false);
    }

    /**
     * Constructor.
     *
     * @param aDefinition    the slot definition
     * @param aGrid          the whole grid data
     * @param anInstantiated whether the slot has been instantiated
     */
    SlotData(final SlotDefinition aDefinition, final BoxData[][] aGrid,
             final boolean anInstantiated) {
        definition = aDefinition;
        grid = aGrid;
        instantiated = anInstantiated;
    }

    @Override
    public String toString() {
        return "SlotData{" + "definition=" + definition + ", value=" + value() + '}';
    }

    int length() {
        return definition.length();
    }

    void write(final String value) {
        if (value == null || definition.length() != value.length()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < value.length(); i++) {
            boxAt(i).set(value.charAt(i));
        }
        instantiated = true;
    }

    /**
     * Delete all boxes of this slot except the ones passed as arguments.
     *
     * @param boxesToKeep the boxes to keep
     */
    void clearExcept(final Set<Integer> boxesToKeep) {
        for (int i = 0; i < definition.length(); i++) {
            if (!boxesToKeep.contains(i)) {
                boxAt(i).reset();
            }
        }
        instantiated = false;
    }

    /**
     * Delete all boxes of this slot.
     */
    void clear() {
        clearExcept(Collections.emptySet());
    }

    SlotDefinition definition() {
        return definition;
    }

    Optional<String> value() {
        if (!instantiated) {
            return Optional.empty();
        }
        final char[] readValue = new char[definition.length()];
        for (int i = 0; i < definition.length(); i++) {
            final BoxData box = boxAt(i);
            if (box.isEmpty()) {
                throw new IllegalStateException("Detected blank in an assigned word, check your " +
                        "dictionary");
            }
            readValue[i] = box.value();
        }
        return Optional.of(String.valueOf(readValue));
    }

    boolean isInstantiated() {
        return instantiated;
    }

    int emptyBoxRatio() {
        int empty = 0;
        for (int i = 0; i < length(); i++) {
            if (boxAt(i).isEmpty()) {
                empty++;
            }
        }
        return empty * 100 / length();
    }

    BoxData boxAt(final int i) {
        final BoxData box;
        if (definition.type().isHorizontal()) {
            box = grid[definition.offset()][definition.start() + i];
        } else {
            box = grid[definition.start() + i][definition.offset()];
        }
        return box;
    }
}
