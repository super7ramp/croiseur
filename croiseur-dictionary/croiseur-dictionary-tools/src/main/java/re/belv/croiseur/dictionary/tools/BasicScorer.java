/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Stream;
import re.belv.croiseur.dictionary.common.StringTransformers;

/**
 * For a given word list, computes a score corresponding to the capability of the words to cross
 * with each other: A <em>crossability</em> score.
 * <p>
 * This score corresponds to the cumulated sum of the number of crossings between word pairs divided
 * by the number of pairs.
 * <p>
 * Example: "HELLO", "CROSS", "WORLD" will return 2.0 (6.0/3.0) because there are 6 ways to cross
 * word pairs on a total of 3 word pairs.
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
public final class BasicScorer implements Callable<Double> {

    /** The words for which to compute the number of crossings. */
    private final List<String> words;

    /**
     * Constructs an instance.
     *
     * @param wordsArg the words for which to compute the number of crossings
     */
    BasicScorer(final List<String> wordsArg) {
        words = wordsArg;
    }

    @Override
    public Double call() {
        final double score;
        if (words.size() > 1) {

            long numberOfCrossings = 0;
            final Map<Character, Long> countedCharacters = new HashMap<>();
            for (final String word : words) {
                for (int i = 0; i < word.length(); i++) {
                    numberOfCrossings += countedCharacters.getOrDefault(word.charAt(i), 0L);
                }
                for (int i = 0; i < word.length(); i++) {
                    countedCharacters.merge(word.charAt(i), 1L, Long::sum);
                }
            }
            final double numberOfPairs = words.size() * (words.size() - 1.0) / 2.0;
            score = numberOfCrossings / numberOfPairs;

        } else {
            score = 0.0;
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
        try (final Stream<String> lines = Files.lines(wordListPath)) {
            final List<String> words =
                    lines.map(StringTransformers.toAcceptableCrosswordEntry()).toList();
            final Double result = new BasicScorer(words).call();
            System.out.printf("%.2f%n", result);
        } catch (final IOException e) {
            System.err.println("Failed to read " + wordListPath + ": " + e.getMessage());
            System.exit(2);
        }
    }
}
