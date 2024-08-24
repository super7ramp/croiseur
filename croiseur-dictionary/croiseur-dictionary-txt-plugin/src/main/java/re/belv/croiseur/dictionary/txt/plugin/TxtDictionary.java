/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.txt.plugin;

import static java.util.stream.Collectors.toCollection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import re.belv.croiseur.common.dictionary.DictionaryDetails;
import re.belv.croiseur.dictionary.common.StringFilters;
import re.belv.croiseur.dictionary.common.StringTransformers;
import re.belv.croiseur.dictionary.common.util.Lazy;
import re.belv.croiseur.spi.dictionary.Dictionary;

/**
 * Dictionary backed by simple text file.
 */
final class TxtDictionary implements Dictionary {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(TxtDictionary.class.getName());

    /** Details about the dictionary, lazily read. */
    private final Lazy<DictionaryDetails> details;

    /** The dictionary words, lazily read. */
    private final Lazy<Set<String>> words;

    /**
     * Constructs an instance.
     *
     * @param file the text file
     */
    TxtDictionary(final File file) {
        details = Lazy.of(() -> readDetails(file));
        words = Lazy.of(() -> readWords(file));
    }

    /**
     * Reads the dictionary words.
     * <p>
     * One word is expected by line.
     *
     * @return the dictionary words
     */
    private static Set<String> readWords(final File file) {
        try (final Stream<String> lines = Files.lines(file.toPath())) {
            return lines.filter(StringFilters.notEmpty())
                    .map(StringTransformers.toAcceptableCrosswordEntry())
                    .collect(toCollection(LinkedHashSet::new));
        } catch (final IOException e) {
            LOGGER.log(Level.WARNING, e, () -> "Failed to read dictionary words");
            return Collections.emptySet();
        }
    }

    /**
     * Reads the dictionary details.
     * <p>
     * Details are stored in a companion properties file, so that dictionary remains a simple
     * list of words.
     * <p>
     * Name of the companion properties file is the name of the dictionary file suffixed with
     * {@code .properties}, e.g. if dictionary is {@code example.txt}, then details are stored in
     * {@code example.txt.properties}.
     *
     * @return the dictionary details
     */
    private static DictionaryDetails readDetails(final File file) {
        final String propertiesPath = file.getPath() + ".properties";
        final Properties properties = new Properties();
        try (final InputStream fis = new FileInputStream(propertiesPath)) {
            properties.load(fis);
            final Locale locale = Locale.forLanguageTag(properties.getProperty("locale", "en"));
            // TODO #63 manage translations
            final String name = properties.getProperty("name");
            final String description = properties.getProperty("description");
            return new DictionaryDetails(name, locale, description);
        } catch (final IOException e) {
            LOGGER.log(Level.WARNING, e, () -> "Failed to read dictionary details");
            return DictionaryDetails.unknown();
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
