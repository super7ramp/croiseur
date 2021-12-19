package com.gitlab.super7ramp.crosswords.dictionary.hunspell;

import com.gitlab.super7ramp.crosswords.dictionary.api.Dictionary;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.HunspellDictionary;
import com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryProvider;

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
    public String name() {
        return "hunspell";
    }

    @Override
    public String description() {
        return "Hunspell dictionary backend";
    }

    @Override
    public Quality quality() {
        return Quality.BAD;
    }

    @Override
    public Collection<Dictionary> get(final URL... dictionaryPaths) {
        return streamOf(dictionaryPaths).map(HunspellDictionary::new).collect(Collectors.toList());
    }

}
