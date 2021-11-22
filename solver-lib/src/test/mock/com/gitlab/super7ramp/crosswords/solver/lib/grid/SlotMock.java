package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIdentifier;

import java.util.Optional;

/**
 * Mock for {@link Slot}.
 */
public class SlotMock implements Slot {

    private final SlotIdentifier uid;

    /**
     * Constructor.
     */
    public SlotMock(final int id) {
        uid = new SlotIdentifier(id);
    }

    @Override
    public SlotIdentifier uid() {
        return uid;
    }

    @Override
    public boolean isCompatibleWith(String value) {
        return false;
    }

    @Override
    public Optional<String> value() {
        return Optional.empty();
    }

    @Override
    public void assign(String value) {
        // Stub.
    }

    @Override
    public void unassign() {
        // Stub.
    }
}
