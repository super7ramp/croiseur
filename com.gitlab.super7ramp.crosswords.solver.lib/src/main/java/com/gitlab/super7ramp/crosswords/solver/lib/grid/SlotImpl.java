package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;

import java.util.Optional;

/**
 * Implementation of {@link Slot}.
 */
final class SlotImpl implements Slot {

    /**
     * Raw data access to slot.
     */
    private final GridData.SlotData data;

    /**
     * Uid.
     */
    private final SlotIdentifier uid;

    /**
     * Constructor.
     *
     * @param anUid    a {@link SlotIdentifier}
     * @param someData raw data access
     */
    SlotImpl(final SlotIdentifier anUid, final GridData.SlotData someData) {
        uid = anUid;
        data = someData;
    }

    @Override
    public SlotIdentifier uid() {
        return uid;
    }

    @Override
    public Optional<String> value() {
        return data.value();
    }

    @Override
    public boolean isCompatibleWith(final String value) {
        if (data.length() != value.length()) {
            return false;
        }

        for (int i = 0; i < data.length(); i++) {
            final Optional<Character> letter = data.letterAt(i);
            if (letter.isPresent() && !letter.get().equals(value.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void assign(final String value) {
        data.write(value);
    }

    @Override
    public void unassign() {
        data.clear();
    }

}
