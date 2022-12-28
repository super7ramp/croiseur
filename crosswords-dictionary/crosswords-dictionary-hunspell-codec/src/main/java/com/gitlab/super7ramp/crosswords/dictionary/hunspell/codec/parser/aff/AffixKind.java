package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

/**
 * Kind of affix.
 */
public enum AffixKind {
    /** Prefix. */
    PFX,
    /** Suffix. */
    SFX;

    public boolean isPrefix() {
        return this == PFX;
    }

    public boolean isSuffix() {
        return this == SFX;
    }
}
