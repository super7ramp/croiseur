/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.tools;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link Scorer}.
 */
final class ScorerTest {

    /**
     * Example: "HELLO", "WORLD" will return 3 because there are three ways to cross these words:
     * <pre>
     *        |W|             |W|             |W|
     *        |O|             |O|     |H|E|L|L|O|
     *        |R|             |R|             |R|
     *    |H|E|L|L|O|   |H|E|L|L|O|           |L|
     *        |D|             |D|             |D|
     * </pre>
     */
    @Test
    void helloWorld() {
        final Scorer scorer = new Scorer(List.of("HELLO", "WORLD"));
        final Long score = scorer.call();
        assertEquals(3, score);
    }

    /**
     * Example: "HELLO", "CROSS", "WORLD" will return 6 because there are 6 ways to cross these
     * words 2-by-2:
     *
     * <ul>
     * <li>HELLO and CROSS:
     * <pre>
     *             |C|
     *             |R|
     *     |H|E|L|L|O|
     *             |S|
     *             |S|
     * </pre>
     * </li>
     * <li>HELLO and WORLD:
     * <pre>
     *        |W|             |W|             |W|
     *        |O|             |O|     |H|E|L|L|O|
     *        |R|             |R|             |R|
     *    |H|E|L|L|O|   |H|E|L|L|O|           |L|
     *        |D|             |D|             |D|
     * </pre>
     * </li>
     * <li>CROSS and WORLD:
     * <pre>
     *        |W|            |W|
     *        |O|        |C|R|O|S|S|
     *      |C|R|O|S|S|      |R|
     *        |L|            |L|
     *        |D|            |D|
     *
     * </pre>
     * </li>
     * </ul>
     */
    @Test
    void helloCrossWorld() {
        final Scorer scorer = new Scorer(List.of("HELLO", "CROSS", "WORLD"));
        final Long score = scorer.call();
        assertEquals(6, score);
    }
}
