package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.common.FlagType;

import java.util.Collection;

/**
 * Represents a parsed ".aff" file.
 */
public record Aff(FlagType flagType, Collection<Affix> affixes) {
    // Nothing to add.
}