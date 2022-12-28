package com.gitlab.super7ramp.crosswords.dictionary.xml.codec;

import java.io.IOException;
import java.io.InputStream;

/**
 * Supplies an {@link InputStream}.
 */
public interface InputStreamSupplier {

    /**
     * Returns an {@link InputStream}.
     *
     * @return an {@link InputStream}
     * @throws IOException if {@link InputStream} cannot be supplied
     */
    InputStream get() throws IOException;
}
