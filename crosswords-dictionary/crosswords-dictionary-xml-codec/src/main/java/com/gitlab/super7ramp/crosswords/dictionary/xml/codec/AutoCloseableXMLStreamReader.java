package com.gitlab.super7ramp.crosswords.dictionary.xml.codec;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

/**
 * An {@link AutoCloseable} {@link XMLStreamReader}.
 */
final class AutoCloseableXMLStreamReader extends StreamReaderDelegate implements AutoCloseable {

    /**
     * Constructs an instance.
     *
     * @param decorated the decorated reader
     */
    AutoCloseableXMLStreamReader(final XMLStreamReader decorated) {
        super(decorated);
    }
}