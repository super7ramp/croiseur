/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.aff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import re.belv.croiseur.dictionary.hunspell.codec.parser.common.FlagType;

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
