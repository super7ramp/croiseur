package com.gitlab.super7ramp.crosswords.solver.ginsberg.history;

import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.Slot;

public interface HistoryWriter extends History {

    static HistoryWriter create() {
        return new HistoryImpl();
    }

    void addAssignmentRecord(final Slot variable);

    void removeAssignmentRecord(final Slot variable);

}
