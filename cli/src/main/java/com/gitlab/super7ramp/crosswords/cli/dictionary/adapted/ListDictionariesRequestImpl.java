package com.gitlab.super7ramp.crosswords.cli.dictionary.adapted;

import com.gitlab.super7ramp.crosswords.cli.core.api.dictionary.ListDictionariesRequest;

import java.util.Locale;
import java.util.Optional;

/**
 * Implementation of {@link ListDictionariesRequest}.
 */
public final class ListDictionariesRequestImpl implements ListDictionariesRequest {

    /** The required locale; can be null. */
    private final Locale locale;

    /** The required provider; can be null. */
    private final String provider;

    /**
     * Constructor.
     *
     * @param aLocale   the required locale; can be null
     * @param aProvider The required provider; can be null
     */
    public ListDictionariesRequestImpl(Locale aLocale, String aProvider) {
        locale = aLocale;
        provider = aProvider;
    }

    @Override
    public Optional<Locale> locale() {
        return Optional.ofNullable(locale);
    }

    @Override
    public Optional<String> provider() {
        return Optional.ofNullable(provider);
    }
}
