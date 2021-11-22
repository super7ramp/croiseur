package com.gitlab.super7ramp.crosswords.solver.lib.history;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;

/**
 * Consumer: instantiation.
 */
public interface InstantiationHistoryProducer {

    void recordAssignment(Slot slot, String value);

}
