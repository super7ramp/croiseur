/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.hunspell.plugin;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryDetails;
import com.gitlab.super7ramp.croiseur.dictionary.common.StringFilters;
import com.gitlab.super7ramp.croiseur.dictionary.common.StringTransformers;
import com.gitlab.super7ramp.croiseur.dictionary.common.util.Lazy;
import com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec.HunspellDictionaryReader;
import com.gitlab.super7ramp.croiseur.spi.dictionary.Dictionary;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;

/**
 * Adapts {@link HunspellDictionaryReader} to
 * {@link Dictionary}.
 */
final class HunspellDictionary implements Dictionary {

    /** Details about the dictionary, lazily read. */
    private final Lazy<DictionaryDetails> details;

    /** The dictionary words, lazily read. */
    private final Lazy<Set<String>> words;

    /**
     * Constructs an instance.
     *
     * @param aDicURL URL to Hunspell dictionary (.dic); affix file is expected to have same
     *                basename and extension .aff
     */
    public HunspellDictionary(final URL aDicURL) {
        final HunspellDictionaryReader dictionary = new HunspellDictionaryReader(aDicURL);
        details = Lazy.of(dictionary::details);
        words = Lazy.of(() -> dictionary.stream()
                                        .filter(StringFilters.notEmpty())
                                        .map(StringTransformers.toAcceptableCrosswordEntry())
                                        .collect(toCollection(LinkedHashSet::new)));
    }

    @Override
    public DictionaryDetails details() {
        return details.get();
    }

    @Override
    public Set<String> words() {
        return Collections.unmodifiableSet(words.get());
    }
}
