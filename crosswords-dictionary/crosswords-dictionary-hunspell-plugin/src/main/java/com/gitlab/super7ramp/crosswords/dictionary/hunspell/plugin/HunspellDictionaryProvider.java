package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.HunspellDictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;

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

    private static Stream<URL> streamOf(final URL... dictionaryPaths) {
        final Stream<URL> paths;
        if (dictionaryPaths.length == 0) {
            paths = DefaultDictionaries.get().stream();
        } else {
            paths = Stream.of(dictionaryPaths);
        }
        return paths;
    }

    @Override
    public DictionaryProviderDescription description() {
        return new DictionaryProviderDescription("hunspell", "Hunspell dictionary backend");
    }

    @Override
    public Collection<Dictionary> get(final URL... dictionaryPaths) {
        return streamOf(dictionaryPaths).map(HunspellDictionary::new).collect(Collectors.toList());
    }

}