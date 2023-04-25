/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Computes a <em>crossability</em> score for the words of a given dictionary.
 * <p>
 * This score corresponds to the cumulated sum of the number of crossings between words taken 2 by
 * 2.
 * <p>
 * Example: "HELLO", "WORLD" will return 3 because there are three ways to cross these words:
 * <pre>
 *        |W|             |W|             |W|
 *        |O|             |O|     |H|E|L|L|O|
 *        |R|             |R|             |R|
 *    |H|E|L|L|O|   |H|E|L|L|O|           |L|
 *        |D|             |D|             |D|
 * </pre>
 */
public final class Scorer implements Callable<Long> {

    /** The number of words to process between each progress print. */
    private static final int WORDS_BETWEEN_EACH_PRINT_PROGRESS = 1_000;

    /** The words to compute for which to compute the number of crossings. */
    private final List<String> words;

    /**
     * A shared counter incremented each time the crossings of a word with all the other words have
     * been computed.
     */
    private final AtomicInteger counter;

    /**
     * Constructs an instance.
     *
     * @param wordsArg The words to compute for which to compute the number of crossings
     */
    Scorer(final List<String> wordsArg) {
        words = wordsArg;
        counter = new AtomicInteger();
    }

    @Override
    public Long call() {
        return IntStream.range(0, words.size())
                        .parallel()
                        .mapToLong(i -> IntStream.range(i + 1, words.size())
                                                 .parallel()
                                                 .mapToLong(
                                                         j -> crossings(words.get(i), words.get(j)))
                                                 .sum())
                        .peek(i -> printProgress())
                        .sum();
    }

    /**
     * Computes the number of crossings between two given words.
     *
     * @param a a word
     * @param b another word
     * @return the number of crossings between the two given words
     */
    private static int crossings(final String a, final String b) {
        int numberOfCrossings = 0;
        for (int i = 0; i < a.length(); i++) {
            for (int j = 0; j < b.length(); j++) {
                numberOfCrossings += eq(a.charAt(i), b.charAt(j));
            }
        }
        return numberOfCrossings;
    }

    /**
     * Returns 1 if given characters are equal, 0 otherwise.
     *
     * @param a left operand
     * @param b right operand
     * @return 1 if given characters are equal, 0 otherwise
     */
    private static int eq(final char a, final char b) {
        return a == b ? 1 : 0;
    }

    /**
     * Prints progress on standard output every {@value WORDS_BETWEEN_EACH_PRINT_PROGRESS} words
     * processed.
     */
    private void printProgress() {
        final int current = counter.incrementAndGet();
        if (current % WORDS_BETWEEN_EACH_PRINT_PROGRESS == 0) {
            final long completionPercentage = (long) ((current * 100.0) / words.size());
            final String progress =
                    String.format("[%d] %d / %d [%d %%]", Thread.currentThread().getId(), current,
                                  words.size(), completionPercentage);
            System.err.println(progress);
        }
    }

    /**
     * Usage: {@code scorer path/to/wordlist.txt}.
     *
     * @param args arguments
     * @throws ExecutionException   if computation fails
     * @throws InterruptedException if interrupted while computing
     */
    public static void main(final String[] args) throws ExecutionException, InterruptedException {

        if (args.length != 1) {
            System.err.println("Usage: scorer path/to/wordlist.txt");
            System.exit(1);
        }

        final Path wordListPath = Path.of(args[0]);
        List<String> words = null;
        try (final Stream<String> lines = Files.lines(wordListPath)) {
            words = lines.distinct().toList();
        } catch (final IOException e) {
            System.err.println("Failed to read " + wordListPath + ": " + e.getMessage());
            System.exit(2);
        }

        final ForkJoinPool pool = new ForkJoinPool(16);
        final Long result = pool.submit(new Scorer(words)).get();

        System.out.println("score = " + result);
    }
}
