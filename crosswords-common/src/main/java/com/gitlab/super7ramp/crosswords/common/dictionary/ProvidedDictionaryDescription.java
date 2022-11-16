package com.gitlab.super7ramp.crosswords.common.dictionary;

/**
 * Gathers provider and dictionary information.
 *
 * @param provider provider description
 * @param dictionary dictionary description
 */
public record ProvidedDictionaryDescription(DictionaryProviderDescription provider,
                                            DictionaryDescription dictionary) {
    // Nothing to add.
}
