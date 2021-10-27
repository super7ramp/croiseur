package com.gitlab.super7ramp.crosswords.solver.lib.history;

import com.gitlab.super7ramp.crosswords.solver.lib.core.History;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.SlotMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests on {@link HistoryImpl}.
 */
final class HistoryTest {

    @Test
    void recordAssignment() {
        final History history = new HistoryImpl();
        final SlotMock slot = new SlotMock(1);

        history.recordAssignment(slot, "value");

        assertTrue(history.lastAssignedSlot().isPresent());
        assertEquals(slot, history.lastAssignedSlot().get());
    }

    @Test
    void recordUnassignment() {
        final History history = new HistoryImpl();
        final SlotMock slot = new SlotMock(1);

        history.recordAssignment(slot, "value");
        history.recordUnassignment(slot);

        assertFalse(history.lastAssignedSlot().isPresent());
    }

    @Test
    void recordSeveralAssignments() {
        final History history = new HistoryImpl();
        final SlotMock slot1 = new SlotMock(1);
        final SlotMock slot2 = new SlotMock(2);
        final SlotMock slot3 = new SlotMock(2);

        history.recordAssignment(slot1, "aaa");
        history.recordAssignment(slot2, "bbb");
        history.recordAssignment(slot3, "ccc");

        assertTrue(history.lastAssignedSlot().isPresent());
        assertEquals(slot3, history.lastAssignedSlot().get());
    }

    @Test
    void recordSeveralUnassignments() {
        final History history = new HistoryImpl();
        final SlotMock slot1 = new SlotMock(1);
        final SlotMock slot2 = new SlotMock(2);
        final SlotMock slot3 = new SlotMock(2);

        history.recordAssignment(slot1, "aaa");
        history.recordAssignment(slot2, "bbb");
        history.recordAssignment(slot3, "ccc");
        history.recordUnassignment(slot1);
        history.recordUnassignment(slot2);
        history.recordUnassignment(slot3);

        assertFalse(history.lastAssignedSlot().isPresent());
    }

    @Test
    void recordUnassignmentMiddle() {
        final History history = new HistoryImpl();
        final SlotMock slot1 = new SlotMock(1);
        final SlotMock slot2 = new SlotMock(2);
        final SlotMock slot3 = new SlotMock(2);

        history.recordAssignment(slot1, "aaa");
        history.recordAssignment(slot2, "bbb");
        history.recordAssignment(slot3, "ccc");
        history.recordUnassignment(slot2);

        assertTrue(history.lastAssignedSlot().isPresent());
        assertEquals(slot3, history.lastAssignedSlot().get());
    }
}
