package com.gitlab.super7ramp.crosswords.common.dictionary;

/**
 * Details about a dictionary provider.
 *
 * @param name        the name of this dictionary provider
 * @param description a short description of this dictionary provider
 */
public record DictionaryProviderDescription(String name, String description) {
    // Nothing to add.
}