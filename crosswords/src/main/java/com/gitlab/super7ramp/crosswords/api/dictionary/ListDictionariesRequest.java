package com.gitlab.super7ramp.crosswords.api.dictionary;

import java.util.Locale;
import java.util.Optional;

/**
 * A request to list available dictionaries.
 */
public interface ListDictionariesRequest {

    /**
     * If present, only shows dictionaries for this locale.
     *
     * @return an optional locale
     */
    Optional<Locale> locale();

    /**
     * If present, only shows dictionaries from this provider.
     *
     * @return an optional provider
     */
    Optional<String> provider();
}
