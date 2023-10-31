/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.xml.plugin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import re.belv.croiseur.dictionary.common.DictionaryPath;
import re.belv.croiseur.spi.dictionary.Dictionary;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests on {@link XmlDictionaryProvider}.
 */
final class XmlDictionaryProviderTest {

    /** The default locale. */
    private Locale defaultLocale;

    /**
     * Sets the locale to English for reproducible results.
     */
    @BeforeEach
    void before() {
        defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
    }

    @Test
    void get() {
        System.setProperty(DictionaryPath.SYSTEM_PROPERTY, System.getProperty("user.dir") + "/src" +
                "/test/resources/dictionaries");
        final DictionaryProvider xmlDictionaryProvider = new XmlDictionaryProvider();

        final Collection<Dictionary> dictionaries = xmlDictionaryProvider.get();

        assertEquals(1, dictionaries.size());
        final Dictionary dictionary = dictionaries.iterator().next();
        assertEquals("Dictionary example", dictionary.details().name());
        assertEquals(Set.of("HELLO", "WORLD"), dictionary.words());
    }

    /**
     * No dictionary found: Dictionary path is empty.
     */
    @Test
    void getNone() {
        final DictionaryProvider xmlDictionaryProvider = new XmlDictionaryProvider();

        final Collection<Dictionary> dictionaries = xmlDictionaryProvider.get();

        assertEquals(0, dictionaries.size());
    }

    /**
     * Resets the default locale.
     */
    @AfterEach
    void after() {
        Locale.setDefault(defaultLocale);
        System.clearProperty(DictionaryPath.SYSTEM_PROPERTY);
    }
}
