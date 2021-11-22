package com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.parser.common;

/**
 * An affix flag type.
 */
public enum FlagType {

    /** Affix name is composed of a single extended-ASCII character. */
    SINGLE_ASCII,
    /** Affix name is composed of 2 extended-ASCII characters. */
    LONG_ASCII,
    /** Affix name is composed of a number between 1 and 65000. */
    NUMERICAL,
    /** Affix name is a UTF-8 character. */
    UTF_8

}
