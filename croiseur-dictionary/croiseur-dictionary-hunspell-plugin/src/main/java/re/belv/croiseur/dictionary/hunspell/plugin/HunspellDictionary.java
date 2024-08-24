/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.plugin;

import static java.util.stream.Collectors.toCollection;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import re.belv.croiseur.common.dictionary.DictionaryDetails;
import re.belv.croiseur.dictionary.common.StringFilters;
import re.belv.croiseur.dictionary.common.StringTransformers;
import re.belv.croiseur.dictionary.common.util.Lazy;
import re.belv.croiseur.dictionary.hunspell.codec.HunspellDictionaryReader;
import re.belv.croiseur.spi.dictionary.Dictionary;

/** Adapts {@link HunspellDictionaryReader} to {@link Dictionary}. */
final class HunspellDictionary implements Dictionary {

    /** Details about the dictionary, lazily read. */
    private final Lazy<DictionaryDetails> details;

    /** The dictionary words, lazily read. */
    private final Lazy<Set<String>> words;

    /**
     * Constructs an instance.
     *
     * @param aDicURL URL to Hunspell dictionary (.dic); affix file is expected to have same basename and extension .aff
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
