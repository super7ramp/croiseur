package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import java.util.Collection;

/**
 * Represents a parsed affix.
 */
public record Affix(AffixHeader header, Collection<AffixRule> rules) {
    // Nothing to add.
}
