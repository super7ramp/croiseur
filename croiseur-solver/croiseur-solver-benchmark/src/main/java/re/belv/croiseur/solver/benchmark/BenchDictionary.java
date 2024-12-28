/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.benchmark;

import static java.util.stream.Collectors.toCollection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import re.belv.croiseur.dictionary.common.StringTransformers;
import re.belv.croiseur.spi.solver.Dictionary;

/**
 * The dictionary used in the benchmarks.
 *
 * <p>This class is not meant to be overridden, it is public and not final only for the JMH instrumentation to work.
 */
@State(Scope.Benchmark)
public class BenchDictionary {

    /** A basic {@link Dictionary} implementation for benchmarks. */
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

    /**
     * Sets up the dictionary.
     * @throws IOException if dictionary cannot be read
     */
    @Setup
    public final void setup() throws IOException {
        final InputStream is = Objects.requireNonNull(
                BenchDictionary.class.getResourceAsStream("/UKACD18plus.txt"),
                "Test dictionary not found, verify the jmh resources.");
        dictionary = new DictionaryImpl(is);
    }

    /**
     * Returns the dictionary.
     * @return the dictionary
     */
    public final Dictionary get() {
        return dictionary;
    }
}
