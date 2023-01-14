/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.xml.codec;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A stateless dictionary writer.
 */
public final class DictionaryWriter {

    /** Private constructor to prevent instantiation. */
    private DictionaryWriter() {
        // Nothing to do.
    }

    private static void writeHeader(final DictionaryHeader header, final XMLStreamWriter writer) throws XMLStreamException {
        writeLocale(header, writer);
        for (final Map.Entry<Locale, String> entry : header.names().entrySet()) {
            writeName(writer, entry.getKey(), entry.getValue());
        }
        for (final Map.Entry<Locale, String> entry : header.descriptions().entrySet()) {
            writeDescription(writer, entry.getKey(), entry.getValue());
        }
    }

    private static void writeDescription(final XMLStreamWriter writer, final Locale lang,
                                         final String description) throws XMLStreamException {
        writer.writeStartElement(ElementNames.DESCRIPTION);
        if (!lang.equals(Locale.ENGLISH)) {
            writer.writeAttribute("xml:lang", lang.toLanguageTag());
        }
        writer.writeCharacters(description);
        writer.writeEndElement();
    }

    private static void writeName(final XMLStreamWriter writer, final Locale lang,
                                  final String name) throws XMLStreamException {
        writer.writeStartElement(ElementNames.NAME);
        if (!lang.equals(Locale.ENGLISH)) {
            writer.writeAttribute("xml:lang", lang.toLanguageTag());
        }
        writer.writeCharacters(name);
        writer.writeEndElement();
    }

    private static void writeLocale(final DictionaryHeader header, final XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(ElementNames.LOCALE);
        writer.writeCharacters(header.locale().toLanguageTag());
        writer.writeEndElement();
    }

    private static void writeWord(final XMLStreamWriter writer, final String word) throws XMLStreamException {
        writer.writeStartElement(ElementNames.WORD);
        writer.writeCharacters(word);
        writer.writeEndElement();
    }

    private static void writeWords(final List<String> words, final XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(ElementNames.WORDS);
        for (final String word : words) {
            writeWord(writer, word);
        }
        writer.writeEndElement();
    }

    /**
     * Writes the given {@link Dictionary} to the given {@link OutputStream}.
     *
     * @param os         the output stream to write into
     * @param dictionary the dictionary to write
     * @throws DictionaryWriteException if write fails
     */
    public static void write(final OutputStream os, final Dictionary dictionary) throws DictionaryWriteException {
        try (final OutputStream bufferedOutputStream = new BufferedOutputStream(os)) {
            final XMLStreamWriter writer = createWriter(bufferedOutputStream);

            writer.writeStartDocument();

            writeDictionaryStartElement(writer);
            writeHeader(dictionary.header(), writer);
            writeWords(dictionary.words(), writer);
            writer.writeEndElement();

            writer.writeEndDocument();
            // TODO should be done in an AutoCloseable like it's done for reader; Needs a
            //  StreamWriterDelegate which is not present in StAX API, to create
            writer.flush();
            writer.close();
        } catch (final XMLStreamException | IOException e) {
            throw new DictionaryWriteException(e);
        }
    }

    private static void writeDictionaryStartElement(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("tns", "dictionary", "http://www.example.org/dictionary");
        writer.writeAttribute("xmlns", "", "tns", "http://www.example.org/dictionary");
        writer.writeAttribute("xmlns", "", "xsi", "http://www.w3.org/2001/XMLSchema-instance");
        writer.writeAttribute("xsi", "", "schemaLocation", "http://www.example.org/dictionary" +
                " dictionary.xsd");
    }

    private static XMLStreamWriter createWriter(OutputStream os) throws XMLStreamException {
        final XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        // TODO configuration?
        return xmlOutputFactory.createXMLStreamWriter(os);
    }
}
