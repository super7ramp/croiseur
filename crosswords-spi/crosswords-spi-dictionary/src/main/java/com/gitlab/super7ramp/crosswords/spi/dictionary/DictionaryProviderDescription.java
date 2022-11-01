package com.gitlab.super7ramp.crosswords.spi.dictionary;

/**
 * Details about a dictionary provider.
 */
public interface DictionaryProviderDescription {

    /**
     * Returns the name of this dictionary provider.
     *
     * @return name of this dictionary provider
     */
    String name();

    /**
     * Returns a description of this dictionary provider.
     *
     * @return a description of this dictionary provider
     */
    String description();
}
