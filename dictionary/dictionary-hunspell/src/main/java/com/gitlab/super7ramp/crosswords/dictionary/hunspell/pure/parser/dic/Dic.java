package com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.parser.dic;

import java.util.Collection;

/**
 * Represents a parsed ".dic" file.
 */
public record Dic(Collection<DicEntry> entries) {
    // Nothing to add.
}
