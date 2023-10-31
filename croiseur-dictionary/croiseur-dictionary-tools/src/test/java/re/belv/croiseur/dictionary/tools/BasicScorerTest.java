/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.tools;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link BasicScorer}.
 */
final class BasicScorerTest {

    /**
     * Example: "HELLO", "WORLD" will return 3.0 (3.0/1.0) because there are 3 ways to cross word
     * pairs on a total of 1 word pair:
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
        final BasicScorer scorer = new BasicScorer(List.of("HELLO", "WORLD"));
        final Double score = scorer.call();
        assertEquals(3.0, score);
    }

    /**
     * Example: "HELLO", "CROSS", "WORLD" will return 2.00 (6.0/3.0) because there are 6 ways to
     * cross word pairs on a total of 3 word pairs:
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
        final BasicScorer scorer = new BasicScorer(List.of("HELLO", "CROSS", "WORLD"));
        final Double score = scorer.call();
        assertEquals(2.00, score);
    }

    /**
     * Example: "HELLO", "CROSS", "WORD" will return 1.33 (4.0/3.0) because there are 4 ways to
     * cross word pairs on a total of 3 word pairs:
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
     * <li>HELLO and WORD:
     * <pre>
     *          |W|
     *  |H|E|L|L|O|
     *          |R|
     *          |D|
     * </pre>
     * </li>
     * <li>CROSS and WORD:
     * <pre>
     *        |W|            |W|
     *        |O|        |C|R|O|S|S|
     *      |C|R|O|S|S|      |R|
     *        |L|            |D|
     *
     * </pre>
     * </li>
     * </ul>
     */
    @Test
    void helloCrossWord() {
        final BasicScorer scorer = new BasicScorer(List.of("HELLO", "CROSS", "WORD"));
        final Double score = scorer.call();
        assertEquals(1.33, score, 10E-2);
    }
}
