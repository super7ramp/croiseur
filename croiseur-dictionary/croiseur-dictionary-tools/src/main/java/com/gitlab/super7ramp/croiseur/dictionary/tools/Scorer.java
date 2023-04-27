/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * For a given word list, computes a score corresponding to the capability of the words to cross
 * with each other: A <em>crossability</em> score.
 * <p>
 * This score corresponds to the cumulated sum of the number of crossings between words taken 2 by
 * 2.
 * <p>
 * Example: "HELLO", "CROSS", "WORLD" will return 6 because there are 6 ways to cross these words
 * 2-by-2:
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
public final class Scorer implements Callable<Long> {

    /** The words for which to compute the number of crossings. */
    private final List<String> words;

    /**
     * Constructs an instance.
     *
     * @param wordsArg the words for which to compute the number of crossings
     */
    Scorer(final List<String> wordsArg) {
        words = wordsArg;
    }

    @Override
    public Long call() {
        long score = 0;
        final Map<Character, Long> countedCharacters = new HashMap<>();
        for (final String word : words) {
            for (int i = 0; i < word.length(); i++) {
                score += countedCharacters.getOrDefault(word.charAt(i), 0L);
            }
            for (int i = 0; i < word.length(); i++) {
                countedCharacters.merge(word.charAt(i), 1L, Long::sum);
            }
        }
        return score;
    }

    /**
     * Usage: {@code scorer path/to/wordlist.txt}.
     *
     * @param args arguments
     */
    public static void main(final String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: scorer path/to/wordlist.txt");
            System.exit(1);
        }
        final Path wordListPath = Path.of(args[0]);
        try {
            final List<String> words = Files.readAllLines(wordListPath);
            final Long result = new Scorer(words).call();
            System.out.println(result);
        } catch (final IOException e) {
            System.err.println("Failed to read " + wordListPath + ": " + e.getMessage());
            System.exit(2);
        }
    }
}
