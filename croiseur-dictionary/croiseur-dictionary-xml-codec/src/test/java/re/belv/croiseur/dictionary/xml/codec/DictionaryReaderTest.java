/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.xml.codec;

import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests on {@link DictionaryReader}.
 */
final class DictionaryReaderTest {

    @Test
    void readHeader() throws DictionaryReadException {
        final URL dictionaryUrl = DictionaryReaderTest.class.getResource("/example.xml");
        final DictionaryReader reader = new DictionaryReader(dictionaryUrl::openStream);

        final DictionaryHeader header = reader.readHeader();

        assertEquals(Locale.ENGLISH, header.locale());
        assertEquals("Dictionary example", header.names().get(Locale.ENGLISH));
        assertEquals("Exemple de dictionnaire", header.names().get(Locale.FRANCE));
    }


    @Test
    void readWords() throws DictionaryReadException {
        final URL dictionaryUrl = DictionaryReaderTest.class.getResource("/example.xml");
        final DictionaryReader reader = new DictionaryReader(dictionaryUrl::openStream);

        final List<String> words = reader.readWords().toList();

        assertEquals(List.of("Hello", "World"), words);
    }
}
