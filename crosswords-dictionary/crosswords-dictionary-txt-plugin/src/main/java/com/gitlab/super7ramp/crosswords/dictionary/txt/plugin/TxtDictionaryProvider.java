package com.gitlab.super7ramp.crosswords.dictionary.txt.plugin;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.dictionary.common.DictionaryPath;
import com.gitlab.super7ramp.crosswords.dictionary.common.util.Lazy;
import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * A dictionary provider of simple text file dictionaries.
 */
public final class TxtDictionaryProvider implements DictionaryProvider {

    /** Provider description. */
    private final DictionaryProviderDescription description;

    /** The dictionaries, lazily evaluated */
    private final Lazy<Collection<Dictionary>> dictionaries;

    /**
     * Constructs an instance.
     */
    public TxtDictionaryProvider() {
        description = new DictionaryProviderDescription("Local Text Provider",
                "Provides access to local dictionaries in a simple text format.");
        dictionaries = Lazy.of(() -> dictionaryFiles().<Dictionary>map(TxtDictionary::new)
                                                      .toList());
    }

    /**
     * Retrieves the dictionary files.
     *
     * @return the dictionary files
     */
    private static Stream<File> dictionaryFiles() {
        return DictionaryPath.getDefault()
                             .list()
                             .stream()
                             .filter(f -> f.getName().endsWith(".txt") &&
                                     Files.exists(Path.of(f.getPath() + ".properties")));
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
