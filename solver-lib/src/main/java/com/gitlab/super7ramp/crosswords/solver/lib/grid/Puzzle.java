package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.lookahead.Probable;

import java.util.Collection;

/**
 * Read/write access to crossword puzzle model.
 */
public interface Puzzle extends Probable {

    /**
     * The slots.
     *
     * @return the slots
     */
    Collection<Slot> slots();
}
