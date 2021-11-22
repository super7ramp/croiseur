package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link SlotDefinition}.
 */
final class SlotDefinitionTest {

    /**
     * Tests that {@link SlotDefinition#isConnected(SlotDefinition)} returns <code>true</code> in this situation:
     * <pre>
     *      0 1 2 3 4 5
     *   0 |X|H|H|H|H| |
     *   1 |V| | | | | |
     *   2 |V| | | | | |
     *   3 |V| | | | | |
     *   4 |V| | | | | |
     * </pre>
     */
    @Test
    void connectedStartStart() {
        final SlotDefinition a = new SlotDefinition(0, 0, 5, SlotDefinition.Type.HORIZONTAL);
        final SlotDefinition b = new SlotDefinition(0, 0, 5, SlotDefinition.Type.VERTICAL);

        assertTrue(a.isConnected(b));
        assertTrue(b.isConnected(a));
    }

    /**
     * Tests that {@link SlotDefinition#isConnected(SlotDefinition)} returns <code>true</code> in this situation:
     * <pre>
     *      0 1 2 3 4 5
     *   0 |H|H|X|H|H| |
     *   1 | | |V| | | |
     *   2 | | |V| | | |
     *   3 | | |V| | | |
     *   4 | | |V| | | |
     * </pre>
     */
    @Test
    void connectedStartMiddle() {
        final SlotDefinition a = new SlotDefinition(0, 0, 5, SlotDefinition.Type.HORIZONTAL);
        final SlotDefinition b = new SlotDefinition(2, 0, 5, SlotDefinition.Type.VERTICAL);

        assertTrue(a.isConnected(b));
        assertTrue(b.isConnected(a));
    }

    /**
     * Tests that {@link SlotDefinition#isConnected(SlotDefinition)} returns <code>true</code> in this situation:
     * <pre>
     *      0 1 2 3 4 5
     *   0 |H|H|H|H|X| |
     *   1 | | | | |V| |
     *   2 | | | | |V| |
     *   3 | | | | |V| |
     *   4 | | | | |V| |
     * </pre>
     */
    @Test
    void connectedStartEnd() {
        final SlotDefinition a = new SlotDefinition(0, 0, 5, SlotDefinition.Type.HORIZONTAL);
        final SlotDefinition b = new SlotDefinition(4, 0, 5, SlotDefinition.Type.VERTICAL);

        assertTrue(a.isConnected(b));
        assertTrue(b.isConnected(a));
    }

    /**
     * Tests that {@link SlotDefinition#isConnected(SlotDefinition)} returns <code>true</code> in this situation:
     * <pre>
     *      0 1 2 3 4 5
     *   0 | | |V| | | |
     *   1 | | |V| | | |
     *   2 |H|H|X|H|H| |
     *   3 | | |V| | | |
     *   4 | | |V| | | |
     * </pre>
     */
    @Test
    void connectedMiddleMiddle() {
        final SlotDefinition a = new SlotDefinition(2, 0, 5, SlotDefinition.Type.HORIZONTAL);
        final SlotDefinition b = new SlotDefinition(2, 0, 5, SlotDefinition.Type.VERTICAL);

        assertTrue(a.isConnected(b));
        assertTrue(b.isConnected(a));
    }

    /**
     * Tests that {@link SlotDefinition#isConnected(SlotDefinition)} returns <code>true</code> in this situation:
     * <pre>
     *      0 1 2 3 4 5
     *   0 | | | | |V| |
     *   1 | | | | |V| |
     *   2 | | | | |V| |
     *   3 | | | | |V| |
     *   4 |H|H|H|H|X| |
     * </pre>
     */
    @Test
    void connectedEndEnd() {
        final SlotDefinition a = new SlotDefinition(4, 0, 5, SlotDefinition.Type.HORIZONTAL);
        final SlotDefinition b = new SlotDefinition(4, 0, 5, SlotDefinition.Type.VERTICAL);

        assertTrue(a.isConnected(b));
        assertTrue(b.isConnected(a));
    }

    /**
     * Tests that {@link SlotDefinition#isConnected(SlotDefinition)} returns <code>false</code> in this situation:
     * <pre>
     *      0 1 2 3 4 5
     *   0 |H|H|H|H|H| |
     *   1 |I|I|I|I|I| |
     *   2 | | | | | | |
     *   3 | | | | | | |
     *   4 | | | | | | |
     * </pre>
     */
    @Test
    void notConnectedBothHorizontal() {
        final SlotDefinition a = new SlotDefinition(0, 0, 5, SlotDefinition.Type.HORIZONTAL);
        final SlotDefinition b = new SlotDefinition(1, 0, 5, SlotDefinition.Type.HORIZONTAL);

        assertFalse(a.isConnected(b));
        assertFalse(b.isConnected(a));
    }

    /**
     * Tests that {@link SlotDefinition#isConnected(SlotDefinition)} returns <code>false</code> in this situation:
     * <pre>
     *      0 1 2 3 4 5
     *   0 |V|W| | | | |
     *   1 |V|W| | | | |
     *   2 |V|W| | | | |
     *   3 |V|W| | | | |
     *   4 |V|W| | | | |
     * </pre>
     */
    @Test
    void notConnectedBothVertical() {
        final SlotDefinition a = new SlotDefinition(0, 0, 5, SlotDefinition.Type.VERTICAL);
        final SlotDefinition b = new SlotDefinition(1, 0, 5, SlotDefinition.Type.VERTICAL);

        assertFalse(a.isConnected(b));
        assertFalse(b.isConnected(a));
    }

    /**
     * Tests that {@link SlotDefinition#isConnected(SlotDefinition)} returns <code>false</code> in this situation:
     * <pre>
     *      0 1 2 3 4 5
     *   0 |V| |H|H|H| |
     *   1 |V| | | | | |
     *   2 |V| | | | | |
     *   3 |V| | | | | |
     *   4 |V| | | | | |
     * </pre>
     */
    @Test
    void notConnectedBeforeStart() {
        final SlotDefinition a = new SlotDefinition(0, 2, 5, SlotDefinition.Type.HORIZONTAL);
        final SlotDefinition b = new SlotDefinition(0, 0, 5, SlotDefinition.Type.VERTICAL);

        assertFalse(a.isConnected(b));
        assertFalse(b.isConnected(a));
    }

    /**
     * Tests that {@link SlotDefinition#isConnected(SlotDefinition)} returns <code>false</code> in this situation:
     * <pre>
     *      0 1 2 3 4 5 6
     *   0 |H|H|H|H|H| |V|
     *   1 | | | | | | |V|
     *   2 | | | | | | |V|
     *   3 | | | | | | |V|
     *   4 | | | | | | |V|
     * </pre>
     */
    @Test
    void notConnectedAfterEnd() {
        final SlotDefinition a = new SlotDefinition(0, 0, 5, SlotDefinition.Type.HORIZONTAL);
        final SlotDefinition b = new SlotDefinition(6, 0, 5, SlotDefinition.Type.VERTICAL);

        assertFalse(a.isConnected(b));
        assertFalse(b.isConnected(a));
    }
}
