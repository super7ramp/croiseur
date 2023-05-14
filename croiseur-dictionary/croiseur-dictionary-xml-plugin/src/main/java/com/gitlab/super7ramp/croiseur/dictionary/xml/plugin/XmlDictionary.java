/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.xml.plugin;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryDetails;
import com.gitlab.super7ramp.croiseur.dictionary.common.StringFilters;
import com.gitlab.super7ramp.croiseur.dictionary.common.StringTransformers;
import com.gitlab.super7ramp.croiseur.dictionary.common.util.Lazy;
import com.gitlab.super7ramp.croiseur.dictionary.xml.codec.DictionaryHeader;
import com.gitlab.super7ramp.croiseur.dictionary.xml.codec.DictionaryReadException;
import com.gitlab.super7ramp.croiseur.dictionary.xml.codec.DictionaryReader;
import com.gitlab.super7ramp.croiseur.spi.dictionary.Dictionary;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toCollection;

/**
 * An XML dictionary.
 */
final class XmlDictionary implements Dictionary {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(XmlDictionary.class.getName());

    /** Details about the dictionary, lazily read. */
    private final Lazy<DictionaryDetails> details;

    /** The dictionary words, lazily read. */
    private final Lazy<Set<String>> words;

    /**
     * Constructs an instance.
     *
     * @param file the dictionary to read
     */
    XmlDictionary(final File file) {
        final DictionaryReader reader = new DictionaryReader(() -> new FileInputStream(file));
        details = Lazy.of(() -> readDetails(reader));
        words = Lazy.of(() -> readWords(reader));
    }

    /**
     * Returns the name of the dictionary from the given header, in the system's current locale
     * if present and fallbacks on English if not present.
     *
     * @param header the dictionary header
     * @return the name of the dictionary, in system's locale or in English if no translation
     * in system's locale is available
     */
    private static String extractName(final DictionaryHeader header) {
        final Map<Locale, String> names = header.names();
        return Objects.requireNonNullElseGet(names.get(Locale.getDefault()),
                () -> names.get(Locale.ENGLISH));
    }

    /**
     * Reads the dictionary details.
     *
     * @param reader the reader
     * @return the dictionary details
     */
    private static DictionaryDetails readDetails(final DictionaryReader reader) {
        try {
            final DictionaryHeader header = reader.readHeader();
            return new DictionaryDetails(extractName(header), header.locale());
        } catch (final DictionaryReadException e) {
            LOGGER.log(Level.WARNING, e, () -> "Failed to read dictionary details");
            return DictionaryDetails.unknown();
        }
    }

    /**
     * Reads the dictionary words.
     *
     * @param reader the reader
     * @return the dictionary words
     */
    private static Set<String> readWords(final DictionaryReader reader) {
        try {
            return reader.readWords()
                         .filter(StringFilters.notEmpty())
                         .map(StringTransformers.toAcceptableCrosswordEntry())
                         .collect(toCollection(LinkedHashSet::new));
        } catch (final DictionaryReadException e) {
            LOGGER.log(Level.WARNING, e, () -> "Failed to read dictionary words");
            return Collections.emptySet();
        }
    }

    @Override
    public DictionaryDetails details() {
        return details.get();
    }

    @Override
    public Set<String> words() {
        return words.get();
    }
}
