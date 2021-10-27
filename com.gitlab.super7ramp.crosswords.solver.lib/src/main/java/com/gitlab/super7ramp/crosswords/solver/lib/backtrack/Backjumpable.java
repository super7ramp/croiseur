package com.gitlab.super7ramp.crosswords.solver.lib.backtrack;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Connectable;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.lookahead.Probable;

import java.util.Set;

public interface Backjumpable extends Connectable, Probable {
    /**
     * Return all the slots.
     *
     * @return all the slots
     */
    Set<Slot> slots();
}
