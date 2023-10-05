/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg;

import com.gitlab.super7ramp.croiseur.dictionary.common.StringTransformers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

/**
 * Mock for {@link Dictionary}.
 */
final class DictionaryMock implements Dictionary {

    private final Set<String> words;

    /**
     * Constructor.
     *
     * @param someWords words of the dictionary
     */
    DictionaryMock(final String... someWords) {
        words = Arrays.stream(someWords).collect(toCollection(LinkedHashSet::new));
    }

    DictionaryMock(final Path dictionaryPath) throws IOException {
        try (final Stream<String> lines = Files.lines(dictionaryPath)) {
            words = lines.map(StringTransformers.toAcceptableCrosswordEntry())
                         .collect(toCollection(LinkedHashSet::new));
        }
    }

    @Override
    public Set<String> words() {
        return Collections.unmodifiableSet(words);
    }
}
