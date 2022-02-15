package com.gitlab.super7ramp.crosswords.cli.core.api.dictionary;

import java.util.Optional;

/**
 * The request to list entries for a specific dictionary.
 */
public interface ListDictionaryEntriesRequest {

    /**
     * Return the desired dictionary provider, if any.
     *
     * @return the desired dictionary provider, if any
     */
    Optional<String> dictionaryProvider();

    /**
     * Return the desired dictionary name.
     *
     * @return the desired dictionary name
     */
    String dictionaryName();
}
