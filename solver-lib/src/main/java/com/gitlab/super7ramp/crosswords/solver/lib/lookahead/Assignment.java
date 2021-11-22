package com.gitlab.super7ramp.crosswords.solver.lib.lookahead;

import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIdentifier;

/**
 * Represents the assignment of a slot with a value.
 */
public interface Assignment {

    /**
     * Builds an {@link Assignment}.
     *
     * @param anUid an UID
     * @param aWord a word
     * @return the {@link Assignment}
     */
    static Assignment of(final SlotIdentifier anUid, final String aWord) {
        return new Assignment() {
            @Override
            public SlotIdentifier slotUid() {
                return anUid;
            }

            @Override
            public String word() {
                return aWord;
            }
        };
    }

    /**
     * The identifier of the slot to assign.
     *
     * @return the identifier of the slot to assign.
     */
    SlotIdentifier slotUid();

    /**
     * The word to fill the slot with.
     *
     * @return the word to fill the slot with
     */
    String word();
}
