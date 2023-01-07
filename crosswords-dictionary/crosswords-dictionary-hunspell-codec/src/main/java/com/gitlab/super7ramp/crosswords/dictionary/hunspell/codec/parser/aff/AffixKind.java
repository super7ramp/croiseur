package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

/**
 * Kind of affix.
 */
public enum AffixKind {
    /** Prefix. */
    PFX,
    /** Suffix. */
    SFX;

    /**
     * Returns whether this instance is a prefix.
     *
     * @return {@code true} iff this instance is a prefix
     */
    public boolean isPrefix() {
        return this == PFX;
    }

    /**
     * Returns whether this instance is a suffix.
     *
     * @return {@code true} iff this instance is a suffix
     */
    public boolean isSuffix() {
        return this == SFX;
    }
}
