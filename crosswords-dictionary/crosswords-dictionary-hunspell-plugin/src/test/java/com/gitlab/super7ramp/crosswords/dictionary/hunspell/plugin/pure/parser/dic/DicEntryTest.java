package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.dic;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.common.Flag;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.common.FlagType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class DicEntryTest {

    @Test
    void parseHello() throws InvalidDicEntryException {
        final DicEntry entry = DicEntry.valueOf("hello");

        assertFalse(entry.isForbidden());
        assertEquals("hello", entry.word());
        assertTrue(entry.flags().isEmpty());
    }

    @Test
    void parseForbiddenHello() throws InvalidDicEntryException {
        final DicEntry entry = DicEntry.valueOf("*hello");

        assertTrue(entry.isForbidden());
        assertEquals("hello", entry.word());
        assertTrue(entry.flags().isEmpty());
    }

    @Test
    void parseHelloWithFlags() throws InvalidDicEntryException {
        final DicEntry entry = DicEntry.valueOf("hello/ABC");

        assertFalse(entry.isForbidden());
        assertEquals("hello", entry.word());
        assertEquals(List.of(new Flag("A"), new Flag("B"), new Flag("C")), entry.flags());
    }

    @Test
    void parseHelloWithLongFlags() throws InvalidDicEntryException {
        final DicEntry entry = DicEntry.valueOf("hello/AaBbCc", FlagType.LONG_ASCII);

        assertFalse(entry.isForbidden());
        assertEquals("hello", entry.word());
        assertEquals(List.of(new Flag("Aa"), new Flag("Bb"), new Flag("Cc")), entry.flags());
    }
}
