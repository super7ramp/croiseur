/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.dic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import re.belv.croiseur.dictionary.hunspell.codec.model.common.Flag;
import re.belv.croiseur.dictionary.hunspell.codec.model.dic.DicEntry;
import re.belv.croiseur.dictionary.hunspell.codec.parser.common.FlagType;

final class DicEntryParserTest {

    @Test
    void parseHello() throws InvalidDicEntryException {
        final DicEntry entry = DicEntryParser.parse("hello");

        assertFalse(entry.isForbidden());
        assertEquals("hello", entry.word());
        assertTrue(entry.flags().isEmpty());
    }

    @Test
    void parseForbiddenHello() throws InvalidDicEntryException {
        final DicEntry entry = DicEntryParser.parse("*hello");

        assertTrue(entry.isForbidden());
        assertEquals("hello", entry.word());
        assertTrue(entry.flags().isEmpty());
    }

    @Test
    void parseHelloWithFlags() throws InvalidDicEntryException {
        final DicEntry entry = DicEntryParser.parse("hello/ABC");

        assertFalse(entry.isForbidden());
        assertEquals("hello", entry.word());
        assertEquals(List.of(new Flag("A"), new Flag("B"), new Flag("C")), entry.flags());
    }

    @Test
    void parseHelloWithLongFlags() throws InvalidDicEntryException {
        final DicEntry entry = DicEntryParser.parse("hello/AaBbCc", FlagType.LONG_ASCII);

        assertFalse(entry.isForbidden());
        assertEquals("hello", entry.word());
        assertEquals(List.of(new Flag("Aa"), new Flag("Bb"), new Flag("Cc")), entry.flags());
    }

    @Test
    void parseMorphology() throws InvalidDicEntryException {
        final DicEntry entry = DicEntryParser.parse("Tetrapoda\tTaxonomic superclass", FlagType.SINGLE_ASCII);

        assertFalse(entry.isForbidden());
        assertEquals("Tetrapoda", entry.word());
        assertTrue(entry.flags().isEmpty());
    }

    /**
     * Tests that trailing space does not cause parsing failure.
     *
     * @throws InvalidDicEntryException if parsing fails, should not happen
     */
    @Test
    void parseTrailingBlank() throws InvalidDicEntryException {
        final DicEntry entry = DicEntryParser.parse("Araba ", FlagType.SINGLE_ASCII);

        assertFalse(entry.isForbidden());
        assertEquals("Araba", entry.word());
        assertTrue(entry.flags().isEmpty());
    }

    /**
     * Tests that trailing tab does not cause parsing failure.
     *
     * @throws InvalidDicEntryException if parsing fails, should not happen
     */
    @Test
    void parseTrailingTab() throws InvalidDicEntryException {
        final DicEntry entry = DicEntryParser.parse("Bieterkarte/N\t", FlagType.SINGLE_ASCII);

        assertFalse(entry.isForbidden());
        assertEquals("Bieterkarte", entry.word());
        assertEquals(1, entry.flags().size());
        Assertions.assertEquals("N", entry.flags().iterator().next().identifier());
    }
}
