package com.gitlab.super7ramp.crosswords.solver.lib.core;

import java.util.Optional;

public interface History {

    void recordAssignment(Slot slot, String value);

    void recordUnassignment(Slot slot);

    Optional<Slot> lastAssignedSlot();

}
