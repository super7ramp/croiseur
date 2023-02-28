/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.txt.plugin;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryDescription;
import com.gitlab.super7ramp.croiseur.dictionary.common.StringFilters;
import com.gitlab.super7ramp.croiseur.dictionary.common.StringTransformers;
import com.gitlab.super7ramp.croiseur.dictionary.common.util.Lazy;
import com.gitlab.super7ramp.croiseur.spi.dictionary.Dictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Dictionary backed by simple text file.
 */
final class TxtDictionary implements Dictionary {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(TxtDictionary.class.getName());

    /** The dictionary description, lazily read. */
    private final Lazy<DictionaryDescription> description;

    /** The dictionary words, lazily read. */
    private final Lazy<List<String>> words;

    /**
     * Constructs an instance.
     *
     * @param file the text file
     */
    TxtDictionary(final File file) {
        description = Lazy.of(() -> readDescription(file));
        words = Lazy.of(() -> readWords(file));
    }

    /**
     * Reads the dictionary words.
     * <p>
     * One word is expected by line.
     *
     * @return the dictionary words
     */
    private static List<String> readWords(final File file) {
        try (final Stream<String> lines = Files.lines(file.toPath())) {
            return lines.filter(StringFilters.notEmpty())
                        .map(StringTransformers.toAcceptableCrosswordEntry())
                        .toList();
        } catch (final IOException e) {
            LOGGER.log(Level.WARNING, e, () -> "Failed to read dictionary words");
            return Collections.emptyList();
        }
    }

    /**
     * Reads the dictionary description.
     * <p>
     * Description is stored in a companion properties file, so that dictionary remains a simple
     * list of words.
     * <p>
     * Name of the companion properties file is the name of the dictionary file suffixed with
     * {@code .properties}, e.g. if dictionary is {@code example.txt}, then description is stored
     * in {@code example.txt.properties}.
     *
     * @return the dictionary description
     */
    private static DictionaryDescription readDescription(final File file) {
        final String propertiesPath = file.getPath() + ".properties";
        final Properties properties = new Properties();
        try (final InputStream fis = new FileInputStream(propertiesPath)) {
            properties.load(fis);
            final Locale locale = Locale.forLanguageTag(properties.getProperty("locale", "en"));
            final String name = properties.getProperty("name");
            return new DictionaryDescription(name, locale);
        } catch (final IOException e) {
            LOGGER.log(Level.WARNING, e, () -> "Failed to read dictionary description");
            return DictionaryDescription.unknown();
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
