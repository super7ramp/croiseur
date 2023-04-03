/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

/**
 * Mock for {@link Dictionary}.
 */
final class DictionaryMock implements Dictionary {

    private static final Pattern DIC_ENTRY = Pattern.compile("^[\\p{L}-]+/.*$");

    private static final Pattern AFFIXES = Pattern.compile("/.*$");

    private static final Pattern ACCENTS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    private static final String EMPTY = "";

    private static final String HYPHEN = "-";

    private final Set<String> words;

    /**
     * Constructor.
     *
     * @param someWords words of the dictionary
     */
    DictionaryMock(final String... someWords) {
        words = Arrays.stream(someWords).collect(toCollection(LinkedHashSet::new));
    }

    DictionaryMock(final Path dicFile) throws IOException {
        try (final Stream<String> lines = Files.lines(dicFile)) {
            words = lines.filter(DIC_ENTRY.asPredicate())
                         .map(DictionaryMock::removeAffixes)
                         .map(DictionaryMock::removeHyphen)
                         .map(DictionaryMock::removeAccentuation)
                         .map(String::toUpperCase)
                         .collect(toCollection(LinkedHashSet::new));
        }
    }

    private static String removeAffixes(final String word) {
        return AFFIXES.matcher(word).replaceAll(EMPTY);
    }

    private static String removeAccentuation(final String word) {
        final String normalized = Normalizer.normalize(word, Normalizer.Form.NFD);
        return ACCENTS.matcher(normalized).replaceAll(EMPTY);
    }

    private static String removeHyphen(final String word) {
        return word.replace(HYPHEN, EMPTY);
    }

    @Override
    public Set<String> words() {
        return Collections.unmodifiableSet(words);
    }
}
