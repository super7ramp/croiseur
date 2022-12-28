package com.gitlab.super7ramp.crosswords.dictionary.xml.plugin;

import com.gitlab.super7ramp.crosswords.dictionary.common.DictionaryPath;
import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

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
        System.setProperty(DictionaryPath.SYSTEM_PROPERTY, System.getProperty("user.dir") + "src" +
                "/test/resources");
        final DictionaryProvider xmlDictionaryProvider = new XmlDictionaryProvider();

        final Collection<Dictionary> dictionaries = xmlDictionaryProvider.get();

        assertEquals(1, dictionaries.size());
        final Dictionary dictionary = dictionaries.iterator().next();
        assertEquals("Dictionary example", dictionary.description().name());
        assertEquals(List.of("Hello", "World"), dictionary.stream().toList());
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
