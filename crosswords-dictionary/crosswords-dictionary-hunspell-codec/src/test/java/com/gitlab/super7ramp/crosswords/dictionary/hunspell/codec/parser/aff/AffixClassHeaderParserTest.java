/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.aff.AffixClassHeader;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.model.aff.AffixKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link AffixClassHeader}.
 */
final class AffixClassHeaderParserTest {

    @Test
    void validAffixHeader() {
        final String header = "SFX a0 Y 102";

        final AffixClassHeader parsedHeader = AffixClassHeaderParser.parse(header);

        assertEquals(AffixKind.SFX, parsedHeader.kind());
        assertEquals("a0", parsedHeader.flag().identifier());
        assertTrue(parsedHeader.crossProduct());
        assertEquals(102, parsedHeader.numberOfRules());
    }

    @Test
    void invalidAffixHeader() {
        final String header = "XFX a0 Y 102";

        assertThrows(IllegalArgumentException.class, () -> AffixClassHeaderParser.parse(header));
    }
}
