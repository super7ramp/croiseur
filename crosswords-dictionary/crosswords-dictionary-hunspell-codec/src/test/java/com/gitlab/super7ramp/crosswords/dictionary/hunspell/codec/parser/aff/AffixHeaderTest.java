/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link AffixHeader}.
 */
final class AffixHeaderTest {

    @Test
    void validAffixHeader() {
        final String header = "SFX a0 Y 102";

        final AffixHeader parsedHeader = AffixHeader.valueOf(header);

        assertEquals(AffixKind.SFX, parsedHeader.kind());
        assertEquals("a0", parsedHeader.flag().identifier());
        assertTrue(parsedHeader.crossProduct());
        assertEquals(102, parsedHeader.numberOfRules());
    }

    @Test
    void invalidAffixHeader() {
        final String header = "XFX a0 Y 102";

        assertThrows(IllegalArgumentException.class, () -> AffixHeader.valueOf(header));
    }
}
