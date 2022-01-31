package com.gitlab.super7ramp.crosswords.solver.lib.elimination;

import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIdentifier;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Stores the no-goods encountered during backtrack.
 */
final class EliminationSpaceImpl implements EliminationSpaceWriter {

    /**
     * The eliminated no-goods as a map:
     * <ul>
     *     <li>Key: The slot identifier </li>
     *     <li>Value: The eliminations for this slot</li>
     * </ul>
     * The eliminations for a slot are also stored as a map with:
     * <ul>
     *     <li>Key: The eliminated value</li>
     *     <li>Value: The reason(s) for this value to be eliminated - typically some well
     *     chosen slots connected to the unassignable variable at the origin of the elimination</li>
     * </ul>
     * <p>
     * Eliminations for a slot are indexed by eliminated values rather than by reason because
     * it's faster on read and read is more used than write.
     */
    private final Map<SlotIdentifier, Map<String, Set<SlotIdentifier>>> eliminations;

    /**
     * Constructor.
     */
    EliminationSpaceImpl() {
        eliminations = new HashMap<>();
    }

    @Override
    public Set<String> eliminations(final SlotIdentifier slot) {
        return Collections.unmodifiableSet(initialisedEliminations(slot).keySet());
    }

    @Override
    public void eliminate(final SlotIdentifier unassigned, final Collection<SlotIdentifier> reasons,
                          final String eliminated) {

        initialisedEliminations(unassigned, eliminated).addAll(reasons);

        // Unassigned slot is not a valid elimination reasons any more
        for (final Map<String, Set<SlotIdentifier>> entry : eliminations.values()) {

            final Iterator<Map.Entry<String, Set<SlotIdentifier>>> it = entry.entrySet().iterator();
            while (it.hasNext()) {

                final Set<SlotIdentifier> previousReasons = it.next().getValue();
                previousReasons.remove(unassigned);
                if (previousReasons.isEmpty()) {
                    it.remove();
                }

            }
        }
    }

    /**
     * Return the eliminations of given slots - initializing data if necessary.
     *
     * @param slot the slot for which to return the eliminations
     * @return the eliminations
     */
    private Map<String, Set<SlotIdentifier>> initialisedEliminations(final SlotIdentifier slot) {
        return eliminations.computeIfAbsent(slot, k -> new HashMap<>());
    }

    /**
     * Return the reasons of given eliminated value - adding eliminated value if necessary.
     *
     * @param slot            the slot for which to return the elimination reasons
     * @param eliminatedValue the eliminated value
     * @return the elimination reasons
     */
    private Set<SlotIdentifier> initialisedEliminations(final SlotIdentifier slot,
                                                        final String eliminatedValue) {
        return initialisedEliminations(slot).computeIfAbsent(eliminatedValue, k -> new HashSet<>());
    }
}
