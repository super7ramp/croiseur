/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.tools;

import com.gitlab.super7ramp.croiseur.dictionary.common.StringTransformers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static java.lang.Math.pow;
import static java.lang.Math.round;

/**
 * Estimates the number of solutions for a square grid of given size n and a given dictionary.
 * <p>
 * The estimation comes from Chris Long's 1992 paper "Mathematics of Square Construction" and is:
 * <em>{@literal E = W^(2n) * p^(n^2))}</em> where:
 * <ul>
 *     <li>n is the size of the square grid</li>
 *     <li>W is the number of words of size n in the dictionary</li>
 *     <li>p = f(a)^2 + f(b)^2 + ... + f(z)^2 where f(*) is the frequency of letter * in the word list.
 * </ul>
 */
public final class SquareSolutionEstimator
        implements Callable<SquareSolutionEstimator.EstimationSummary> {

    /**
     * The summary of the estimation of the number of solutions for a square grid.
     *
     * @param estimation                the estimation itself (E)
     * @param squareSize                the square size (n)
     * @param numberOfWords             the number of words in the dictionary (W)
     * @param letterFrequenciesPowerSum the power sum of letter frequencies in dictionary (p)
     */
    public record EstimationSummary(long estimation, int squareSize,
                                    int numberOfWords, double letterFrequenciesPowerSum) {
        @Override
        public String toString() {
            final String format = "Square Grid Estimation for n = %d: %d (W = %d; p = %f)";
            return String.format(format, squareSize, estimation, numberOfWords,
                                 letterFrequenciesPowerSum);
        }
    }

    /** The words for which to compute the score. */
    private final List<String> words;

    /** The size of the square grid. */
    private final int n;

    /**
     * Constructs an instance.
     *
     * @param wordsArg the words for which to compute the score
     * @param sizeArg  the size of the square grid
     */
    SquareSolutionEstimator(final List<String> wordsArg, final int sizeArg) {
        words = wordsArg.stream().map(StringTransformers.toAcceptableCrosswordEntry())
                        .filter(word -> word.length() == sizeArg)
                        .toList();
        n = sizeArg;
    }

    @Override
    public EstimationSummary call() {
        final double p = frequenciesPowerSum();
        final int w = words.size();
        final long e = round(pow(w, 2 * n) * pow(p, n * n));
        return new EstimationSummary(e, n, w, p);
    }

    private Map<Character, Double> computeLetterFrequencies() {
        final Map<Character, Long> letterOccurrences = new HashMap<>();
        long total = 0;
        for (final String word : words) {
            assert word.length() == n;
            for (int i = 0; i < n; i++) {
                letterOccurrences.merge(word.charAt(i), 1L, Long::sum);
            }
            total += n;
        }
        final Map<Character, Double> letterFrequencies = new HashMap<>(letterOccurrences.size());
        for (final Map.Entry<Character, Long> letterOccurrence : letterOccurrences.entrySet()) {
            final Character letter = letterOccurrence.getKey();
            final Long occurrence = letterOccurrence.getValue();
            letterFrequencies.put(letter, (double) occurrence / total);
        }
        return letterFrequencies;
    }

    private double frequenciesPowerSum() {
        final Map<Character, Double> frequencies = computeLetterFrequencies();
        return frequencies.values().stream()
                          .mapToDouble(f -> f * f)
                          .sum();
    }

    /**
     * Computes the estimated number of solutions for square grids of sizes 5, 6, 7 with the
     * dictionary whose path is given as argument.
     * <p>
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
            for (int squareSize = 5; squareSize <= 7; squareSize++) {
                final EstimationSummary result =
                        new SquareSolutionEstimator(words, squareSize).call();
                System.out.println(result);
            }
        } catch (final IOException e) {
            System.err.println("Failed to read " + wordListPath + ": " + e.getMessage());
            System.exit(2);
        }
    }
}
