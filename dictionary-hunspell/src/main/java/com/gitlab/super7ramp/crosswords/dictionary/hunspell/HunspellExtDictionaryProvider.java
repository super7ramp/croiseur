package com.gitlab.super7ramp.crosswords.dictionary.hunspell;

import com.gitlab.super7ramp.crosswords.dictionary.api.Dictionary;
import com.gitlab.super7ramp.crosswords.dictionary.api.spi.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.external.HunspellDictionary;

import java.net.URL;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Alternative Hunspell {@link DictionaryProvider} relying on deprecated "unmunch" utility of
 * Hunspell, called as an
 * external process.
 */
public final class HunspellExtDictionaryProvider implements DictionaryProvider {

    /**
     * Constructor.
     */
    public HunspellExtDictionaryProvider() {
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
        return "hunspell-ext";
    }

    @Override
    public String description() {
        return "Alternative Hunspell dictionary backend (relies on external utilities)";
    }

    @Override
    public Quality quality() {
        return Quality.DEBUG_ONLY;
    }

    @Override
    public Collection<Dictionary> get(final URL... dictionaryPaths) {
        return streamOf(dictionaryPaths).map(HunspellDictionary::new).collect(Collectors.toList());
    }
}
