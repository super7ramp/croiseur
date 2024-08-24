/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.aff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.AffixKind;
import re.belv.croiseur.dictionary.hunspell.codec.model.aff.AffixRule;
import re.belv.croiseur.dictionary.hunspell.codec.parser.common.FlagType;

/** Tests for {@link AffixRule}. */
final class AffixRuleParserTest {

    @Test
    void parseValidAffixRule() {
        final String affixRule = "SFX p+ er é/L'D'Q' er";

        final AffixRule parsedAffixRule = AffixRuleParser.parse(affixRule, FlagType.LONG_ASCII);

        assertEquals(AffixKind.SFX, parsedAffixRule.kind());
        assertEquals("p+", parsedAffixRule.flag().identifier());
        assertEquals(Optional.of("er"), parsedAffixRule.strippingCharacters());
        assertEquals("é", parsedAffixRule.affix());
        assertEquals(3, parsedAffixRule.continuationClasses().size());
        assertEquals(Optional.of("er"), parsedAffixRule.condition());
    }

    @Test
    void parseValidAffixRuleWithoutContinuation() {
        final String affixRule = "SFX p+ er é er";

        final AffixRule parsedAffixRule = AffixRuleParser.parse(affixRule);

        assertEquals(AffixKind.SFX, parsedAffixRule.kind());
        assertEquals("p+", parsedAffixRule.flag().identifier());
        assertEquals(Optional.of("er"), parsedAffixRule.strippingCharacters());
        assertEquals("é", parsedAffixRule.affix());
        assertTrue(parsedAffixRule.continuationClasses().isEmpty());
        assertEquals(Optional.of("er"), parsedAffixRule.condition());
    }
}
