/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.txt.plugin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;
import re.belv.croiseur.common.dictionary.DictionaryProviderDetails;
import re.belv.croiseur.dictionary.common.DictionaryPath;
import re.belv.croiseur.dictionary.common.util.Lazy;
import re.belv.croiseur.spi.dictionary.Dictionary;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;

/**
 * A dictionary provider of simple text file dictionaries.
 */
public final class TxtDictionaryProvider implements DictionaryProvider {

    /** Details about the dictionary provider. */
    private final DictionaryProviderDetails details;

    /** The dictionaries, lazily evaluated */
    private final Lazy<Collection<Dictionary>> dictionaries;

    /**
     * Constructs an instance.
     */
    public TxtDictionaryProvider() {
        details = new DictionaryProviderDetails(
                "Local Text Provider", "Provides access to local dictionaries in a simple text format.");
        dictionaries = Lazy.of(
                () -> dictionaryFiles().<Dictionary>map(TxtDictionary::new).toList());
    }

    /**
     * Retrieves the dictionary files.
     *
     * @return the dictionary files
     */
    private static Stream<File> dictionaryFiles() {
        return DictionaryPath.getDefault().list().stream()
                .filter(f -> f.getName().endsWith(".txt") && Files.exists(Path.of(f.getPath() + ".properties")));
    }

    @Override
    public DictionaryProviderDetails details() {
        return details;
    }

    @Override
    public Collection<Dictionary> get() {
        return dictionaries.get();
    }
}
