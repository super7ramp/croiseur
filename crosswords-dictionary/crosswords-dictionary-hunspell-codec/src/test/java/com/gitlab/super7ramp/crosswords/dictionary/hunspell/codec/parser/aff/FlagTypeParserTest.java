package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.FlagType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class FlagTypeParserTest {

    @Test
    void parseNumerical() {
        assertEquals(FlagType.NUMERICAL, FlagTypeParser.parse("FLAG num"));
    }

    @Test
    void parseLong() {
        assertEquals(FlagType.LONG_ASCII, FlagTypeParser.parse("FLAG long"));
    }

    @Test
    void parseUtf8() {
        assertEquals(FlagType.UTF_8, FlagTypeParser.parse("FLAG UTF-8"));
    }

    @Test
    void parseUnknown() {
        assertThrows(IllegalArgumentException.class, () -> FlagTypeParser.parse("FLAG unknown"));
    }
}
