package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.plugin;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.crosswords.dictionary.common.StringFilters;
import com.gitlab.super7ramp.crosswords.dictionary.common.StringTransformers;
import com.gitlab.super7ramp.crosswords.dictionary.common.util.Lazy;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.HunspellDictionaryReader;
import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;

import java.net.URL;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Adapts {@link HunspellDictionaryReader} to
 * {@link Dictionary}.
 */
final class HunspellDictionary implements Dictionary {

    /** The dictionary description, lazily read. */
    private final Lazy<DictionaryDescription> description;

    /** The dictionary words, lazily read. */
    private final Lazy<Collection<String>> words;

    /**
     * Constructs an instance.
     *
     * @param aDicURL URL to Hunspell dictionary (.dic); affix file is expected to have same
     *                basename and extension .aff
     */
    public HunspellDictionary(final URL aDicURL) {
        final HunspellDictionaryReader dictionary = new HunspellDictionaryReader(aDicURL);
        description = Lazy.of(dictionary::description);
        words = Lazy.of(() -> dictionary.stream()
                                        .filter(StringFilters.atLeastTwoCharacters())
                                        .map(StringTransformers.toAcceptableCrosswordEntry())
                                        .toList());
    }

    @Override
    public DictionaryDescription description() {
        return description.get();
    }

    @Override
    public Stream<String> stream() {
        return words.get().stream();
    }
}