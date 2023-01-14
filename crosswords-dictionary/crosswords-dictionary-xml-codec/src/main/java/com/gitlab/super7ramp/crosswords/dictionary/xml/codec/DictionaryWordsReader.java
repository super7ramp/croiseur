/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.xml.codec;

import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

/**
 * Reader dedicated to the {@code <word>} elements.
 */
final class DictionaryWordsReader {

    /** Filter on {@code <word>} element start event. */
    private static final StreamFilter WORDS_STREAM_FILTER =
            reader -> reader.isStartElement() && ElementNames.WORD.equals(reader.getLocalName());

    /** The XML input factory. */
    private final XMLInputFactory xmlInputFactory;

    /** The dictionary input stream supplier. */
    private final InputStreamSupplier dictionaryStream;

    /**
     * Constructs an instance.
     *
     * @param xmlInputFactoryArg  the XML input factory
     * @param dictionaryStreamArg the dictionary input stream supplier
     */
    DictionaryWordsReader(final XMLInputFactory xmlInputFactoryArg,
                          final InputStreamSupplier dictionaryStreamArg) {
        xmlInputFactory = xmlInputFactoryArg;
        dictionaryStream = dictionaryStreamArg;
    }

    /**
     * Reads the words of the dictionary.
     *
     * @return the words of the dictionary as a {@link Stream}
     * @throws IOException        if input stream cannot be opened on given dictionary
     * @throws XMLStreamException if read failed
     */
    Stream<String> read() throws IOException, XMLStreamException {
        try (final InputStream inputStream = dictionaryStream.get();
             final AutoCloseableXMLStreamReader reader = createReader(inputStream)) {
            final Stream.Builder<String> streamBuilder = Stream.builder();
            while (reader.hasNext()) {
                final String word = reader.getElementText();
                streamBuilder.add(word);
                reader.next();
            }
            return streamBuilder.build();
        }
    }

    /**
     * Creates the appropriate XML reader.
     *
     * @return the XML reader
     * @throws XMLStreamException if read fails
     */
    private AutoCloseableXMLStreamReader createReader(final InputStream is) throws XMLStreamException {
        final XMLStreamReader baseReader =
                xmlInputFactory.createXMLStreamReader(is);
        final XMLStreamReader filteredReader =
                xmlInputFactory.createFilteredReader(baseReader, WORDS_STREAM_FILTER);
        return new AutoCloseableXMLStreamReader(filteredReader);
    }
}
