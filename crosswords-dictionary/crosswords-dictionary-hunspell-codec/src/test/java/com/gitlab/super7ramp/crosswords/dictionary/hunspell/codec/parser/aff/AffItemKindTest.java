/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link AffItemKind}.
 */
final class AffItemKindTest {

    @Test
    void identifyValidKey() {
        final String key = "KEY azertyuiop|qsdfghjklmù|wxcvbn|aéz|yèu|iço|oàp|aqz|zse|edr|rft|tgy"
                + "|yhu|uji|iko|olpm|qws|sxd|dcf|fvg|gbh|hnj";

        final Optional<AffItemKind> optKind = AffItemKind.identify(key);

        assertTrue(optKind.isPresent());
        assertEquals(AffItemKind.SUGGESTION_KEY, optKind.get());
    }

    @Test
    void identifyValidWordChars() {
        final String wordChars = "WORDCHARS -’'1234567890.";

        final Optional<AffItemKind> optKind = AffItemKind.identify(wordChars);

        assertTrue(optKind.isPresent());
        assertEquals(AffItemKind.OTHERS_WORD_CHARS, optKind.get());
    }

    @Test
    void identifyMalformedAffixHeaderWithUnknownCrossProduct() {
        final String rule = "PFX a0 X 102";

        final Optional<AffItemKind> optKind = AffItemKind.identify(rule);

        assertTrue(optKind.isEmpty());
    }

    @Test
    void identifyMalformedAffixRuleUnknownOption() {
        final String rule = "XFX p+ er é/L'D'Q' er";

        final Optional<AffItemKind> optKind = AffItemKind.identify(rule);

        assertTrue(optKind.isEmpty());
    }

    @Test
    void identifyValidAffixHeaderWithNCrossProduct() {
        final String rule = "PFX a0 N 102";

        final Optional<AffItemKind> optKind = AffItemKind.identify(rule);

        assertTrue(optKind.isPresent());
        assertEquals(AffItemKind.AFFIX_HEADER, optKind.get());
    }

    @Test
    void identifyValidPrefixHeader() {
        final String rule = "PFX a0 Y 102";

        final Optional<AffItemKind> optKind = AffItemKind.identify(rule);

        assertTrue(optKind.isPresent());
        assertEquals(AffItemKind.AFFIX_HEADER, optKind.get());
    }

    @Test
    void identifyValidSuffixHeader() {
        final String rule = "SFX a0 Y 102";

        final Optional<AffItemKind> optKind = AffItemKind.identify(rule);

        assertTrue(optKind.isPresent());
        assertEquals(AffItemKind.AFFIX_HEADER, optKind.get());
    }


    @Test
    void identifyValidAffixRuleWithoutContinuation() {
        final String rule = "PFX p+ er é er";

        final Optional<AffItemKind> optKind = AffItemKind.identify(rule);

        assertTrue(optKind.isPresent());
        assertEquals(AffItemKind.AFFIX_RULE, optKind.get());
    }

    @Test
    void identifyValidPrefixRule() {
        final String rule = "PFX p+ er é/L'D'Q' er";

        final Optional<AffItemKind> optKind = AffItemKind.identify(rule);

        assertTrue(optKind.isPresent());
        assertEquals(AffItemKind.AFFIX_RULE, optKind.get());
    }

    @Test
    void identifyValidSuffixRule() {
        final String rule = "SFX p+ er é/L'D'Q' er";

        final Optional<AffItemKind> optKind = AffItemKind.identify(rule);

        assertTrue(optKind.isPresent());
        assertEquals(AffItemKind.AFFIX_RULE, optKind.get());
    }

    @Test
    void identifyValidComment() {
        final String rule = "# hello";

        final Optional<AffItemKind> optKind = AffItemKind.identify(rule);

        assertTrue(optKind.isPresent());
        assertEquals(AffItemKind.COMMENT, optKind.get());
    }

    @Test
    void identifyValidBlanks() {
        final String rule = "   ";

        final Optional<AffItemKind> optKind = AffItemKind.identify(rule);

        assertTrue(optKind.isPresent());
        assertEquals(AffItemKind.BLANK, optKind.get());
    }

    @Test
    void identifyValidEmptyLine() {
        final String rule = "";

        final Optional<AffItemKind> optKind = AffItemKind.identify(rule);

        assertTrue(optKind.isPresent());
        assertEquals(AffItemKind.BLANK, optKind.get());
    }
}
