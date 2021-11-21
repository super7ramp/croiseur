package com.gitlab.super7ramp.crosswords.dictionary.hunspell;

import com.gitlab.super7ramp.crosswords.dictionary.api.Dictionary;
import com.gitlab.super7ramp.crosswords.dictionary.api.spi.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.HunspellDictionary;

import java.net.URL;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hunspell dictionary provider.
 */
public final class HunspellDictionaryProvider implements DictionaryProvider {

    /**
     * Constructor.
     */
    public HunspellDictionaryProvider() {
        // Nothing to do.
    }

    @Override
    public Collection<Dictionary> get(final URL... dictionaryPaths) {
        return Stream.of(dictionaryPaths).map(HunspellDictionary::new).collect(Collectors.toList());
    }

    @Override
    public Quality quality() {
        return Quality.BAD;
    }
}
