package com.gitlab.super7ramp.crosswords.solver.lib.core.sap;

import java.util.Collection;

/**
 * An elimination.
 *
 * @param <VariableT>          the eliminated variable type
 * @param <EliminationReasonT> the elimination reason
 */
// FIXME EliminationReasonT should be VariableT, clarify Slot vs. SlotIdentifier usage
public record Elimination<VariableT, EliminationReasonT>(VariableT eliminated,
                                                         Collection<EliminationReasonT> reason) {
    // Nothing to add
}
