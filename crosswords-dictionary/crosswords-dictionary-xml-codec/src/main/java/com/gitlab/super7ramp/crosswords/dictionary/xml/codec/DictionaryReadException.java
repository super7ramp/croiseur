package com.gitlab.super7ramp.crosswords.dictionary.xml.codec;

/**
 * Dictionary read exception.
 */
public final class DictionaryReadException extends Exception {

    /**
     * Constructs an instance.
     *
     * @param cause the cause (which is saved for later retrieval by the getCause() method). (A
     *              null value is permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     */
    DictionaryReadException(final Throwable cause) {
        super(cause);
    }
}
