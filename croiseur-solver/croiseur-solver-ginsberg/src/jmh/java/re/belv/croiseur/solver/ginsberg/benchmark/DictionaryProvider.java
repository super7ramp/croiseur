/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.benchmark;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import re.belv.croiseur.dictionary.common.StringTransformers;
import re.belv.croiseur.solver.ginsberg.Dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;

@State(Scope.Benchmark)
public class DictionaryProvider {

    private static class DictionaryImpl implements Dictionary {

        private final Set<String> words;

        DictionaryImpl(final InputStream is) throws IOException {
            try (final var bufferedReader = new BufferedReader(new InputStreamReader(is))) {
                words = bufferedReader
                        .lines()
                        .map(StringTransformers.toAcceptableCrosswordEntry())
                        .collect(toCollection(LinkedHashSet::new));
            }
        }

        @Override
        public Set<String> words() {
            return Collections.unmodifiableSet(words);
        }
    }

    private Dictionary dictionary;

    @Setup
    public void setup() throws IOException {
        final InputStream is = Objects.requireNonNull(
                SolverBenchmark.class.getResourceAsStream("/UKACD18plus.txt"),
                "Test dictionary not found, verify the jmh resources.");
        dictionary = new DictionaryImpl(is);
    }

    public Dictionary get() {
        return dictionary;
    }
}
