package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common;

/**
 * Parser exception.
 */
public abstract class ParserException extends Exception {

    /**
     * Constructs an instance.
     *
     * @param message the detail message
     */
    protected ParserException(final String message) {
        super(message);
    }
}
