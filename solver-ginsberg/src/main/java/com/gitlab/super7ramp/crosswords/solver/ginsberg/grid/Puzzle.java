package com.gitlab.super7ramp.crosswords.solver.ginsberg.grid;

import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.lookahead.Probable;

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
