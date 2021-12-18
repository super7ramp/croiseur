package com.gitlab.super7ramp.crosswords.solver.lib.history;

import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIdentifier;

public record DeadEnd(SlotIdentifier unassignable, SlotIdentifier unassigned) {

}
