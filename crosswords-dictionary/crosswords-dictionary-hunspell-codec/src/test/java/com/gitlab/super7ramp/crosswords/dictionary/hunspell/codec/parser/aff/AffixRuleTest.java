package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link AffixRule}.
 */
final class AffixRuleTest {

    @Test
    void parseValidAffixRule() {
        final String affixRule = "SFX p+ er é/L'D'Q' er";

        final AffixRule parsedAffixRule = AffixRule.valueOf(affixRule);

        assertEquals(AffixKind.SFX, parsedAffixRule.kind());
        assertEquals("p+", parsedAffixRule.flag().identifier());
        assertEquals(Optional.of("er"), parsedAffixRule.strippingCharacters());
        assertEquals("é", parsedAffixRule.affix());
        assertTrue(parsedAffixRule.continuationClasses().isPresent());
        assertEquals("L'D'Q'", parsedAffixRule.continuationClasses().get());
        assertEquals(Optional.of("er"), parsedAffixRule.condition());
    }

    @Test
    void parseValidAffixRuleWithoutContinuation() {
        final String affixRule = "SFX p+ er é er";

        final AffixRule parsedAffixRule = AffixRule.valueOf(affixRule);

        assertEquals(AffixKind.SFX, parsedAffixRule.kind());
        assertEquals("p+", parsedAffixRule.flag().identifier());
        assertEquals(Optional.of("er"), parsedAffixRule.strippingCharacters());
        assertEquals("é", parsedAffixRule.affix());
        assertTrue(parsedAffixRule.continuationClasses().isEmpty());
        assertEquals(Optional.of("er"), parsedAffixRule.condition());
    }
}
