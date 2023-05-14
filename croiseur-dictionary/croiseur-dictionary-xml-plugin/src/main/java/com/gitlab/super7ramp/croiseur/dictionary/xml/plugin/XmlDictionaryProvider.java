/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.xml.plugin;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryProviderDetails;
import com.gitlab.super7ramp.croiseur.dictionary.common.DictionaryPath;
import com.gitlab.super7ramp.croiseur.dictionary.common.util.Lazy;
import com.gitlab.super7ramp.croiseur.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;

import java.io.File;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * A dictionary provider for XML-based dictionaries.
 */
public final class XmlDictionaryProvider implements DictionaryProvider {

    /** Details about the dictionary provider. */
    private final DictionaryProviderDetails details;

    /** The dictionaries, lazily evaluated */
    private final Lazy<Collection<Dictionary>> dictionaries;

    /**
     * Constructs an instance.
     */
    public XmlDictionaryProvider() {
        details = new DictionaryProviderDetails("Local XML Provider",
                                                "Provides access to local dictionaries in an XML format.");
        dictionaries = Lazy.of(() -> dictionaryFiles().<Dictionary>map(XmlDictionary::new)
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
                             .filter(f -> f.getName().endsWith(".xml"));
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
