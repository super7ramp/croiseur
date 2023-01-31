/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.xml.plugin;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryDescription;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * An XML dictionary.
 */
final class XmlDictionary implements Dictionary {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(XmlDictionary.class.getName());

    /** The dictionary description, lazily read. */
    private final Lazy<DictionaryDescription> description;

    /** The dictionary words, lazily read. */
    private final Lazy<List<String>> words;

    /**
     * Constructs an instance.
     *
     * @param file the dictionary to read
     */
    XmlDictionary(final File file) {
        final DictionaryReader reader = new DictionaryReader(() -> new FileInputStream(file));
        description = Lazy.of(() -> readDescription(reader));
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
     * Reads the dictionary description.
     *
     * @param reader the reader
     * @return the dictionary description
     */
    private static DictionaryDescription readDescription(final DictionaryReader reader) {
        try {
            final DictionaryHeader header = reader.readHeader();
            return new DictionaryDescription(extractName(header), header.locale());
        } catch (final DictionaryReadException e) {
            LOGGER.log(Level.WARNING, e, () -> "Failed to read dictionary description");
            return DictionaryDescription.unknown();
        }
    }

    /**
     * Reads the dictionary words.
     *
     * @param reader the reader
     * @return the dictionary words
     */
    private static List<String> readWords(final DictionaryReader reader) {
        try {
            return reader.readWords()
                         .filter(StringFilters.atLeastTwoCharacters())
                         .map(StringTransformers.toAcceptableCrosswordEntry()).toList();
        } catch (final DictionaryReadException e) {
            LOGGER.log(Level.WARNING, e, () -> "Failed to read dictionary words");
            return Collections.emptyList();
        }
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
