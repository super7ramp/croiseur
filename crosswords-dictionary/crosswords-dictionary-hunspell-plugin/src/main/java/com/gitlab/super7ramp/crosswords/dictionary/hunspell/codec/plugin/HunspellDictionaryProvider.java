package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.plugin;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.dictionary.common.DictionaryPath;
import com.gitlab.super7ramp.crosswords.dictionary.common.util.Lazy;
import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Hunspell dictionary provider.
 */
public final class HunspellDictionaryProvider implements DictionaryProvider {

    /** The provider description. */
    private final DictionaryProviderDescription description;

    /** The dictionaries, lazily evaluated */
    private final Lazy<Collection<Dictionary>> dictionaries;

    /**
     * Constructor.
     */
    public HunspellDictionaryProvider() {
        description = new DictionaryProviderDescription("Local Hunspell Provider",
                "Provides access to local dictionaries in the Hunspell format.");
        dictionaries =
                Lazy.of(() -> dictionaryFiles().<Dictionary>map(HunspellDictionary::new).toList());
    }

    /**
     * Retrieves the dictionary files.
     *
     * @return the dictionary files
     */
    private static Stream<URL> dictionaryFiles() {
        return DictionaryPath.getDefault().list().stream()
                     .filter(f -> f.getName().endsWith(".dic"))
                     .map(HunspellDictionaryProvider::urlFrom)
                     .filter(Objects::nonNull);
    }

    /**
     * Returns the given file's URL, or {@code null} if file location cannot be expressed with a
     * URL.
     *
     * @param file the file
     * @return the given file's URL, or {@code null} if file location cannot be expressed with a
     * URL.
     */
    private static URL urlFrom(File file) {
        // FIXME Not great, maybe drop entirely URL from reader's API and work with File only
        try {
            return file.toURI().toURL();
        } catch (final MalformedURLException e) {
            return null;
        }
    }

    @Override
    public DictionaryProviderDescription description() {
        return description;
    }

    @Override
    public Collection<Dictionary> get() {
        return dictionaries.get();
    }

}
