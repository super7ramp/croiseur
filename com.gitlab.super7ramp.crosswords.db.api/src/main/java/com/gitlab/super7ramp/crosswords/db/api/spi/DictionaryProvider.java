package com.gitlab.super7ramp.crosswords.db.api.spi;


import com.gitlab.super7ramp.crosswords.db.api.Dictionary;

/**
 * {@link Dictionary} provider.
 */
public interface DictionaryProvider {

    /**
     * @return an instance of {@link Dictionary}
     */
    Dictionary get();

}
