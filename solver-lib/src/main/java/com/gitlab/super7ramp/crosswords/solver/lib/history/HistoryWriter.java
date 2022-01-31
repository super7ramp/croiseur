package com.gitlab.super7ramp.crosswords.solver.lib.history;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;

public interface HistoryWriter extends History {

    static HistoryWriter create() {
        return new HistoryImpl();
    }

    void addAssignmentRecord(final Slot variable);

    void removeAssignmentRecord(final Slot variable);

}
