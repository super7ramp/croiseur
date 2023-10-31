/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.xml.codec;

import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Optional;

/**
 * Parses the header part of the dictionary: {@code <locale>}, {@code <name>} and {@code
 * <description>}.
 */
final class DictionaryHeaderReader {

    /**
     * Filters on {@code <locale>}, {@code <name>} and {@code <description>} element start events.
     * <p>
     * Also filters on {@code <words>} start event (i.e. the end of the header).
     */
    private static final StreamFilter DICTIONARY_HEADER_STREAM_FILTER =
            (final XMLStreamReader reader) -> {
                if (!reader.isStartElement()) {
                    return false;
                }
                final String elementName = reader.getLocalName();
                return ElementNames.LOCALE.equals(elementName)
                        || ElementNames.NAME.equals(elementName)
                        || ElementNames.DESCRIPTION.equals(elementName)
                        || ElementNames.WORDS.equals(elementName);
            };

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
    DictionaryHeaderReader(final XMLInputFactory xmlInputFactoryArg,
                           final InputStreamSupplier dictionaryStreamArg) {
        xmlInputFactory = xmlInputFactoryArg;
        dictionaryStream = dictionaryStreamArg;
    }

    /**
     * Parses a {@code <description>} element.
     *
     * @param xmlStreamReader the XML reader
     * @param headerBuilder   the header builder
     * @throws XMLStreamException if read fails
     */
    private static void parseDescription(final XMLStreamReader xmlStreamReader,
                                         final DictionaryHeader.Builder headerBuilder) throws XMLStreamException {
        final Locale lang =
                Optional.ofNullable(xmlStreamReader.getAttributeValue(0))
                        .map(Locale::forLanguageTag)
                        .orElse(Locale.ENGLISH);
        final String content = xmlStreamReader.getElementText();
        headerBuilder.addDescription(lang, content);
    }

    /**
     * Parses a {@code <name>} element.
     *
     * @param xmlStreamReader the XML reader
     * @param headerBuilder   the header builder
     * @throws XMLStreamException if read fails
     */
    private static void parseName(final XMLStreamReader xmlStreamReader,
                                  final DictionaryHeader.Builder headerBuilder) throws XMLStreamException {
        final Locale lang =
                Optional.ofNullable(xmlStreamReader.getAttributeValue(0))
                        .map(Locale::forLanguageTag)
                        .orElse(Locale.ENGLISH);
        final String content = xmlStreamReader.getElementText();
        headerBuilder.addName(lang, content);
    }

    /**
     * Parses the {@code <locale>} element.
     *
     * @param xmlStreamReader the XML reader
     * @param headerBuilder   the header builder
     * @throws XMLStreamException if read fails
     */
    private static void parseLocale(final XMLStreamReader xmlStreamReader,
                                    final DictionaryHeader.Builder headerBuilder) throws XMLStreamException {
        final Locale locale = Locale.forLanguageTag(xmlStreamReader.getElementText());
        headerBuilder.setLocale(locale);
    }

    /**
     * Reads the header of the dictionary and put them into a {@link DictionaryHeader}.
     *
     * @return the {@link DictionaryHeader}
     * @throws IOException        if input stream cannot be opened on given dictionary
     * @throws XMLStreamException if read fails
     */
    DictionaryHeader read() throws XMLStreamException, IOException {
        try (final InputStream is = dictionaryStream.get();
             final AutoCloseableXMLStreamReader reader = createReader(is)) {
            final DictionaryHeader.Builder headerBuilder = new DictionaryHeader.Builder();
            boolean headerEndReached = false;
            while (!headerEndReached && reader.hasNext()) {
                final String elementName = reader.getLocalName();
                switch (elementName) {
                    case ElementNames.LOCALE -> parseLocale(reader, headerBuilder);
                    case ElementNames.NAME -> parseName(reader, headerBuilder);
                    case ElementNames.DESCRIPTION -> parseDescription(reader, headerBuilder);
                    case ElementNames.WORDS -> {
                        headerEndReached = true;
                        // Short-circuit, don't let reader filter consume next events
                        continue;
                    }
                    default -> throw new XMLStreamException("Illegal element read");
                }
                reader.next();
            }
            return headerBuilder.build();
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
        final XMLStreamReader filteredReader = xmlInputFactory.createFilteredReader(baseReader,
                DICTIONARY_HEADER_STREAM_FILTER);
        return new AutoCloseableXMLStreamReader(filteredReader);
    }
}
